package admin.mealbuffet.com.mealnbuffetadmin.nav.orderdashboard

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.custom.DialogClickListener
import admin.mealbuffet.com.mealnbuffetadmin.custom.OrderStatusChangeDialog
import admin.mealbuffet.com.mealnbuffetadmin.model.BuffetOrder
import admin.mealbuffet.com.mealnbuffetadmin.nav.InternalActionListener
import admin.mealbuffet.com.mealnbuffetadmin.network.ResponseCallback
import admin.mealbuffet.com.mealnbuffetadmin.network.updateBuffetOrderStatus
import admin.mealbuffet.com.mealnbuffetadmin.util.PreferencesHelper
import admin.mealbuffet.com.mealnbuffetadmin.util.getBuffetOrderStatus
import admin.mealbuffet.com.mealnbuffetadmin.viewmodel.BuffetOrdersViewModel
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.mealbuffet.controller.BaseFragment
import kotlinx.android.synthetic.main.fragment_buffetdashboard.*

class BuffetOrderBoardFragment : BaseFragment(), InternalActionListener {
    private lateinit var buffetOrderItemsAdapter: BuffetOrderItemsAdapter
    private var buffetOrdersList = ArrayList<BuffetOrder>()
    private lateinit var buffetOrdersViewModel: BuffetOrdersViewModel

    override fun onAction(action: String, data: Any?) {
        when (action) {
            UPDATE_BUFFET_ORDER_STATUS -> displayOrderChangeDialog(data as BuffetOrder)
        }
    }

    private fun displayOrderChangeDialog(buffetOrder: BuffetOrder) {
        val dialog = OrderStatusChangeDialog.newInstance(String.format(getString(R.string.order_id, buffetOrder.orderId)),
                String.format(getString(R.string.present_status), getBuffetOrderStatus(requireContext(), buffetOrder.status)))
        dialog.setDialogActionListener(object : DialogClickListener {
            override fun onPositiveBanClick(data: Any?) {
                updateBuffetOrderStatus(buffetOrder.orderId, data as Int, object : ResponseCallback {
                    override fun onSuccess(data: Any?) {
                        dialog.dismiss()
                        fetchGetItemsList()
                    }

                    override fun onError(data: Any?) {
                        showNetworkError()
                    }
                })
            }

            override fun onNegativeBtnClick() {
                //Nothing to do
            }
        })
        dialog.show(activity?.supportFragmentManager, "ORDERSTATUSDIALOG")
    }

    override fun layoutResource(): Int = R.layout.fragment_buffetdashboard


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBuffetOrdersViewModel()
    }

    private fun initBuffetOrdersViewModel() {
        buffetOrdersViewModel = ViewModelProviders.of(this).get(BuffetOrdersViewModel::class.java)
        buffetOrdersViewModel.liveData.observe(this, Observer {
            if (it == null) {
                showNetworkError()
            } else {
                buffetOrdersList = it.buffetOrderList as ArrayList<BuffetOrder>
                renderFoodItemsView()
            }
        })
        fetchGetItemsList()
    }

    private fun fetchGetItemsList() {
        val restaurantId = PreferencesHelper.getRestaurantId(requireContext())
        buffetOrdersViewModel.getBuffetOrdersList(restaurantId)
    }

    private fun renderFoodItemsView() {
        buffetOrderItemsAdapter = BuffetOrderItemsAdapter(requireContext(), this as InternalActionListener)
        rc_buffet_dashboard.apply {
            adapter = buffetOrderItemsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        rc_buffet_dashboard.itemAnimator = DefaultItemAnimator()
        buffetOrderItemsAdapter.setData(buffetOrdersList)
        buffetOrderItemsAdapter.notifyDataSetChanged()
    }

    companion object {
        const val UPDATE_BUFFET_ORDER_STATUS: String = "UpdateBuffetOrderStatus"
    }
}
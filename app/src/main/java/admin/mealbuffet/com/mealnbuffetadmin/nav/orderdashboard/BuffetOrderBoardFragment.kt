package admin.mealbuffet.com.mealnbuffetadmin.nav.orderdashboard

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.custom.DialogClickListener
import admin.mealbuffet.com.mealnbuffetadmin.custom.OrderStatusChangeDialog
import admin.mealbuffet.com.mealnbuffetadmin.model.BuffetOrder
import admin.mealbuffet.com.mealnbuffetadmin.nav.InternalActionListener
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
    private lateinit var foodItemsAdapter: BuffetOrderItemsAdapter
    private var buffetOrdersList = ArrayList<BuffetOrder>()
    private lateinit var buffetOrdersViewModel: BuffetOrdersViewModel

    override fun onAction(action: String, data: Any?) {

        when (action) {
            UPDATE_BUFFET_ORDER_STATUS -> displayOrderChangeDialog(data as BuffetOrder)
        }
    }

    private fun displayOrderChangeDialog(buffetOrder: BuffetOrder) {
        val dialog = OrderStatusChangeDialog.newInstance(String.format(getString(R.string.order_id, buffetOrder.orderId)),
                String.format(getString(R.string.present_status), getBuffetOrderStatus(buffetOrder.status)))
        dialog.setDialogActionListener(object : DialogClickListener {
            override fun onPositiveBanClick() {
                //EmptyCart
                /* userAddedMeals.clear()
                 fileUserAddedMeals.clear()
                 JsonFileManager.deleteMealCartFile(requireContext())
                 cart_badge.text = EMPTY_STRING*/
            }

            override fun onNegativeBtnClick() {
                /*userAddedMeals = fileUserAddedMeals
                actionListener?.onAction(SHOW_CART, userAddedMeals)*/
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
        foodItemsAdapter = BuffetOrderItemsAdapter(requireContext(), this as InternalActionListener)
        rc_buffet_dashboard.apply {
            adapter = foodItemsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        rc_buffet_dashboard.itemAnimator = DefaultItemAnimator()
        foodItemsAdapter.setData(buffetOrdersList)
        foodItemsAdapter.notifyDataSetChanged()
    }

    companion object {
        const val UPDATE_BUFFET_ORDER_STATUS = "UpdateBuffetOrderStatus"
    }
}
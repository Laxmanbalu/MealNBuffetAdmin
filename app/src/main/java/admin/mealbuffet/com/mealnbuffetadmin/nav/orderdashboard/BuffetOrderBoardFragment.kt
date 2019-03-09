package admin.mealbuffet.com.mealnbuffetadmin.nav.orderdashboard

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.model.BuffetOrder
import admin.mealbuffet.com.mealnbuffetadmin.nav.InternalActionListener
import admin.mealbuffet.com.mealnbuffetadmin.util.PreferencesHelper
import admin.mealbuffet.com.mealnbuffetadmin.viewmodel.BuffetOrdersViewModel
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter
import android.view.View
import com.mealbuffet.controller.BaseFragment
import kotlinx.android.synthetic.main.fragment_buffetdashboard.*
import java.util.*

class BuffetOrderBoardFragment : BaseFragment(), InternalActionListener {

    private var buffetOrdersList = ArrayList<BuffetOrder>()
    private val buffetOrderedFragment by lazy { BuffetOrderedFragment() }
    private val buffetCompletedFragment by lazy { BuffetOrderCompletedFragment() }
    private val buffetOtherFragment by lazy { BuffetOrderOtherFragment() }
    private val pageAdapter by lazy { ViewPagerAdapter(childFragmentManager) }
    private lateinit var buffetOrdersViewModel: BuffetOrdersViewModel

    override fun onAction(action: String, data: Any?) {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        initBuffetOrdersViewModel()
    }

    override fun layoutResource(): Int = R.layout.fragment_buffetdashboard

    private fun setupViewPager() {

        val strBuffetOrderedTitle = String.format(getString(R.string.ordered), 0)
        val strBuffetCompletedTitle = String.format(getString(R.string.completed), 0)
        val strBuffetOtherTitle = String.format(getString(R.string.other), 0)

        pageAdapter.addFragment(buffetOrderedFragment, strBuffetOrderedTitle)
        pageAdapter.addFragment(buffetCompletedFragment, strBuffetCompletedTitle)
        pageAdapter.addFragment(buffetOtherFragment, strBuffetOtherTitle)

        buffet_orders_viewpager.adapter = pageAdapter
        buffet_orders_viewpager.offscreenPageLimit = 3
        buffet_orders_tab.setupWithViewPager(buffet_orders_viewpager)
        buffet_orders_viewpager.currentItem = 0
    }

    private fun initBuffetOrdersViewModel() {
        buffetOrdersViewModel = ViewModelProviders.of(this).get(BuffetOrdersViewModel::class.java)
        buffetOrdersViewModel.liveData.observe(this, Observer {
            if (it == null) {
                showNetworkError()
            } else {
                buffetOrdersList = it.buffetOrderList as ArrayList<BuffetOrder>

            }
        })
        fetchBuffetOrdersList()
    }

    private fun fetchBuffetOrdersList() {
        val restaurantId = PreferencesHelper.getRestaurantId(requireContext())
        buffetOrdersViewModel.getBuffetOrdersList(restaurantId)
    }

    internal inner class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getItemPosition(item: Any): Int {
            return PagerAdapter.POSITION_NONE
        }


        override fun getCount(): Int {
            return 3 //MAx Tabs 3
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitleList[position]
        }
    }

    /*private lateinit var buffetOrderItemsAdapter: BuffetOrderItemsAdapter
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
                        fetchBuffetOrdersList()
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
        swipeToRefresh.setOnRefreshListener {
            swipeToRefresh.isRefreshing = false
            fetchBuffetOrdersList()
        }
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
        fetchBuffetOrdersList()
    }

    private fun fetchBuffetOrdersList() {
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
    }*/

    companion object {
        const val UPDATE_BUFFET_ORDER_STATUS: String = "UpdateBuffetOrderStatus"
    }
}
package admin.mealbuffet.com.mealnbuffetadmin.nav.orderdashboard

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.model.BuffetOrder
import admin.mealbuffet.com.mealnbuffetadmin.util.PreferencesHelper
import admin.mealbuffet.com.mealnbuffetadmin.viewmodel.BuffetOrdersViewModel
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.mealbuffet.controller.BaseFragment
import kotlinx.android.synthetic.main.buffet_orders_page.*

abstract class BuffetOrderBaseFragment : BaseFragment() {
    override fun layoutResource(): Int = R.layout.buffet_orders_page
    private lateinit var buffetOrderItemsAdapter: BuffetOrderItemsAdapter
    abstract fun getBuffetOrdersHistory()
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && activity != null) {
            getBuffetOrdersHistory()
        }
    }

    fun getBuffetOrdersList(): ArrayList<BuffetOrder>? {
        val buffetOrdersHistory = ViewModelProviders.of(requireActivity()).get(BuffetOrdersViewModel::class.java).liveData.value
        return buffetOrdersHistory?.let { it.buffetOrderList as ArrayList<BuffetOrder> }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderRecyclerView()

        swipeToRefresh.setOnRefreshListener {
            val userId = context?.let { PreferencesHelper.getRestaurantId(it) }
            swipeToRefresh.isRefreshing = false
            userId?.let { ViewModelProviders.of(requireActivity()).get(BuffetOrdersViewModel::class.java).getBuffetOrdersList(it) }
        }
    }

    protected fun updateViews(mealOrdersPendingHistory: List<BuffetOrder>?) {
        if (mealOrdersPendingHistory?.isNotEmpty() == true) {
            rc_buffet_dashboard.visibility = View.VISIBLE
            history_emptyview.visibility = View.GONE
            buffetOrderItemsAdapter.setData(mealOrdersPendingHistory as ArrayList<BuffetOrder>)
            buffetOrderItemsAdapter.notifyDataSetChanged()
        } else {
            displayEmptyView()
        }
    }

    fun refreshView() {
        getBuffetOrdersHistory()
    }

    private fun renderRecyclerView() {
        buffetOrderItemsAdapter = BuffetOrderItemsAdapter(requireContext(), wrapActionListener())
        rc_buffet_dashboard.apply {
            adapter = buffetOrderItemsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = DefaultItemAnimator()
        }
    }

    private fun displayEmptyView() {
        history_emptyview.visibility = View.VISIBLE
    }
}
package admin.mealbuffet.com.mealnbuffetadmin.nav.orderdashboard.mealdashboard

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.model.MealOrders
import admin.mealbuffet.com.mealnbuffetadmin.nav.InternalActionListener
import admin.mealbuffet.com.mealnbuffetadmin.network.ResponseCallback
import admin.mealbuffet.com.mealnbuffetadmin.network.updateMealOrderStatus
import admin.mealbuffet.com.mealnbuffetadmin.util.PreferencesHelper
import admin.mealbuffet.com.mealnbuffetadmin.util.getNextStepOfMealOrderStatus
import admin.mealbuffet.com.mealnbuffetadmin.viewmodel.MealOrdersViewModel
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.mealbuffet.controller.BaseFragment
import kotlinx.android.synthetic.main.meal_orders_page.*

abstract class MealOrderBaseFragment : BaseFragment(), InternalActionListener {

    private lateinit var mealOrderItemsAdapter: MealOrderItemsAdapter

    override fun onAction(action: String, data: Any?) {
        when (action) {
            UPDATE_MEAL_ORDER_STATUS -> updateOrderStatus(data as MealOrders)
        }
    }

    private fun updateOrderStatus(mealOrder: MealOrders) {
        val nextStatus = getNextStepOfMealOrderStatus(mealOrder.status ?: 0)
        mealOrder.mealOrderId?.let {
            updateMealOrderStatus(it, nextStatus, object : ResponseCallback {
                override fun onSuccess(data: Any?) {
                    updateMealOrders()
                }

                override fun onError(data: Any?) {
                    showNetworkError()
                }
            })
        }
    }

    override fun layoutResource(): Int = R.layout.meal_orders_page

    abstract fun getMealOrdersHistory()
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && activity != null) {
            getMealOrdersHistory()
        }
    }

    fun getMealOrdersList(): ArrayList<MealOrders>? {
        val mealOrdersHistory = ViewModelProviders.of(requireActivity()).get(MealOrdersViewModel::class.java).liveData.value
        return mealOrdersHistory?.let { it.mealOrders as ArrayList<MealOrders> }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderRecyclerView()

        swipeToRefresh.setOnRefreshListener {
            swipeToRefresh.isRefreshing = false
            updateMealOrders()
        }
    }

    private fun updateMealOrders() {
        val userId = context?.let { PreferencesHelper.getRestaurantId(it) }
        userId?.let { ViewModelProviders.of(requireActivity()).get(MealOrdersViewModel::class.java).getMealOrdersList(it) }
    }

    protected fun updateViews(mealOrdersPendingHistory: List<MealOrders>?) {
        if (mealOrdersPendingHistory?.isNotEmpty() == true) {
            rc_meal_dashboard.visibility = View.VISIBLE
            history_emptyview.visibility = View.GONE
            mealOrderItemsAdapter.setData(mealOrdersPendingHistory as ArrayList<MealOrders>)
            mealOrderItemsAdapter.notifyDataSetChanged()
        } else {
            displayEmptyView()
        }
    }

    private fun renderRecyclerView() {
        mealOrderItemsAdapter = MealOrderItemsAdapter(requireContext(), this)
        rc_meal_dashboard.apply {
            adapter = mealOrderItemsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = DefaultItemAnimator()
        }
    }

    fun refreshView() {
        getMealOrdersHistory()
    }

    private fun displayEmptyView() {
        history_emptyview.visibility = View.VISIBLE
    }

    companion object {
        const val UPDATE_MEAL_ORDER_STATUS: String = "UpdateMealOrderStatus"
    }
}
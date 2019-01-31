package admin.mealbuffet.com.mealnbuffetadmin.nav.orderdashboard

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.model.MealOrders
import admin.mealbuffet.com.mealnbuffetadmin.util.PreferencesHelper
import admin.mealbuffet.com.mealnbuffetadmin.viewmodel.MealOrdersViewModel
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.mealbuffet.controller.BaseFragment
import kotlinx.android.synthetic.main.fragment_mealdashboard.*

class MealOrderBoardFragment : BaseFragment() {
    private lateinit var mealOrderItemsAdapter: MealOrderItemsAdapter
    private lateinit var mealOrdersViewModel: MealOrdersViewModel
    private lateinit var mealOrders: List<MealOrders>

    override fun layoutResource(): Int = R.layout.fragment_mealdashboard

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMealOrdersView()
    }

    private fun initMealOrdersView() {
        mealOrdersViewModel = ViewModelProviders.of(this).get(MealOrdersViewModel::class.java)
        mealOrdersViewModel.liveData.observe(this, Observer {
            if (it == null) {
                showNetworkError()
            } else {
                mealOrders = it.mealOrders
                renderMealsListView()
            }
        })
        fetchGetItemsList()
    }

    private fun renderMealsListView() {
        mealOrderItemsAdapter = MealOrderItemsAdapter(requireContext(), wrapActionListener())
        rc_meal_dashboard.apply {
            adapter = mealOrderItemsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        rc_meal_dashboard.itemAnimator = DefaultItemAnimator()
        mealOrderItemsAdapter.setData(mealOrders as ArrayList<MealOrders>)
        mealOrderItemsAdapter.notifyDataSetChanged()
    }

    private fun fetchGetItemsList() {
        val restaurantId = PreferencesHelper.getRestaurantId(requireContext())
        mealOrdersViewModel.getMealOrdersList(restaurantId)
    }

    companion object {
        const val UPDATE_MEAL_ORDER_STATUS: String = "UpdateMealStatus"
    }
}
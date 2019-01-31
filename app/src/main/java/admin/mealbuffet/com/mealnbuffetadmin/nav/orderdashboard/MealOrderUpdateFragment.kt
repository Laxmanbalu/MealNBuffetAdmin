package admin.mealbuffet.com.mealnbuffetadmin.nav.orderdashboard

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.model.MealOrders
import admin.mealbuffet.com.mealnbuffetadmin.network.ResponseCallback
import admin.mealbuffet.com.mealnbuffetadmin.network.updateMealOrderStatus
import android.os.Bundle
import android.util.Log
import android.view.View
import com.mealbuffet.controller.BaseFragment
import kotlinx.android.synthetic.main.fragment_mealorder_update.*
import kotlinx.android.synthetic.main.meal_item_data.view.*

class MealOrderUpdateFragment : BaseFragment() {
    private lateinit var selectedMeal: MealOrders
    override fun layoutResource(): Int = R.layout.fragment_mealorder_update

    fun setMealOrder(mealOrders: MealOrders) {
        selectedMeal = mealOrders
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderView()
        mealorder_proceed.setOnClickListener {
            updateOrderStatus()
        }
    }

    private fun renderView() {
        selectedMeal.mealList.forEach { listMealOrders ->

            listMealOrders.forEach {
                val childItemData = layoutInflater.inflate(R.layout.meal_item_data, null)
                childItemData.top = 100
                childItemData.history_item_name.text = it.item
                childItemData.history_item_qty.text = String.format(getString(R.string.item_qty), it.qty)
                meals_views.addView(childItemData)
            }
        }
    }

    private fun updateOrderStatus() {
        updateMealOrderStatus(selectedMeal.mealOrderId, 2, object : ResponseCallback {
            override fun onError(data: Any?) {
                showNetworkError()
            }

            override fun onSuccess(data: Any?) {
                Log.d("TEST123", "Sccuessful")
            }

        })
    }
}
package admin.mealbuffet.com.mealnbuffetadmin.nav.orderdashboard

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.model.MealOrders
import admin.mealbuffet.com.mealnbuffetadmin.network.ResponseCallback
import admin.mealbuffet.com.mealnbuffetadmin.network.updateMealOrderStatus
import admin.mealbuffet.com.mealnbuffetadmin.util.Constants.EMPTY_STRING
import admin.mealbuffet.com.mealnbuffetadmin.util.MealOrderStatus
import admin.mealbuffet.com.mealnbuffetadmin.util.getBuffetOrderStatus
import admin.mealbuffet.com.mealnbuffetadmin.util.getMealOrderStatusValue
import android.os.Bundle
import android.view.View
import com.mealbuffet.controller.BaseFragment
import kotlinx.android.synthetic.main.fragment_mealorder_update.*
import kotlinx.android.synthetic.main.meal_history_item_data.view.*
import kotlinx.android.synthetic.main.meal_history_item_header.view.*

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
        meal_orderid.text = String.format(getString(R.string.order_id), " " + selectedMeal.mealOrderId)
        meal_order_status.text = String.format(getString(R.string.present_status), getBuffetOrderStatus(requireContext(), selectedMeal.status!!))

        when (selectedMeal.status) {
            MealOrderStatus.IN_PROGRESS.status -> meal_order_status.setTextColor(requireContext().getColor(R.color.color_yellow))
            MealOrderStatus.READY_TO_PICKUP.status -> meal_order_status.setTextColor(requireContext().getColor(R.color.color_yellow))
            MealOrderStatus.COMPLETED.status -> meal_order_status.setTextColor(requireContext().getColor(R.color.orange_app))
            MealOrderStatus.REJECTED.status -> meal_order_status.setTextColor(requireContext().getColor(R.color.color_red))
            else -> meal_order_status.setTextColor(requireContext().getColor(R.color.color_brown))
        }

        selectedMeal.mealList?.forEachIndexed { index, listMealOrders ->
            val childHeader = layoutInflater.inflate(R.layout.meal_history_item_header, null)
            childHeader.cart_meal_name.text = String.format(getString(R.string.meal_id), index + 1)
            meals_views.addView(childHeader)

            listMealOrders?.forEach {
                val childItemData = layoutInflater.inflate(R.layout.meal_history_item_data, null)
                childItemData.top = 100
                childItemData.history_item_name.text = it?.item ?: EMPTY_STRING
                childItemData.history_item_qty.text = String.format(getString(R.string.item_qty), it?.qty)
                meals_views.addView(childItemData)
            }
        }
    }


    private fun updateOrderStatus() {
        val orderStatus: String = meal_order_status_spinner.selectedItem.toString()
        if (orderStatus == getString(R.string.choose_order_status_default)) {
            showCustomError(R.string.select_status_change)
            return
        }

        val orderStatusValue = getMealOrderStatusValue(requireContext(), orderStatus)
        selectedMeal.mealOrderId?.let {
            updateMealOrderStatus(it, orderStatusValue, object : ResponseCallback {
                override fun onError(data: Any?) {
                    showNetworkError()
                }

                override fun onSuccess(data: Any?) {
                    wrapActionListener().onAction(MEAL_ORDER_UPDATE_SUCCESS)
                }
            })
        }
    }

    companion object {
        const val MEAL_ORDER_UPDATE_SUCCESS: String = "MealOrderUpdateStatus"
    }
}
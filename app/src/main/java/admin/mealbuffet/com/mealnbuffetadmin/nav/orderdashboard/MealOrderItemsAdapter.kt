package admin.mealbuffet.com.mealnbuffetadmin.nav.orderdashboard

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.model.MealOrders
import admin.mealbuffet.com.mealnbuffetadmin.nav.orderdashboard.MealOrderBoardFragment.Companion.UPDATE_MEAL_ORDER_STATUS
import admin.mealbuffet.com.mealnbuffetadmin.util.MealOrderStatus
import admin.mealbuffet.com.mealnbuffetadmin.util.getMealOrderStatus
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mealbuffet.controller.ActionListener
import kotlinx.android.synthetic.main.meal_order_item_view.view.*
import kotlinx.android.synthetic.main.view_holder_meal_order_item.view.*


class MealOrderItemsAdapter(private val requireContext: Context, private val wrapActionListener: ActionListener) : RecyclerView.Adapter<MealOrderItemsAdapter.BuffetOrderItemViewHolder>() {

    private var ordersLst: ArrayList<MealOrders>? = null

    fun setData(argBuffetOrdersLst: ArrayList<MealOrders>) {
        ordersLst?.clear()
        this.ordersLst = argBuffetOrdersLst
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuffetOrderItemViewHolder {
        val view = LayoutInflater.from(requireContext).inflate(R.layout.view_holder_meal_order_item, parent, false)
        return BuffetOrderItemViewHolder(view, wrapActionListener, requireContext)
    }

    override fun getItemCount(): Int {
        return ordersLst?.size ?: 0
    }

    override fun onBindViewHolder(buffetOrderItemViewHolder: BuffetOrderItemViewHolder, position: Int) {
        ordersLst?.get(position)?.let { buffetOrderItemViewHolder.setData(it) }
    }

    class BuffetOrderItemViewHolder(itemView: View, private val internalActionListener: ActionListener, private val requireContext: Context) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.meal_order_update.setOnClickListener {
                if (itemView.swipeLayout.isOpened) {
                    itemView.swipeLayout.closeSecondaryView(true)
                    internalActionListener.onAction(UPDATE_MEAL_ORDER_STATUS, it.tag as MealOrders)
                }
            }
        }

        fun setData(mealOrders: MealOrders) {
            itemView.tag = mealOrders
            itemView.meal_order_update.tag = mealOrders
            itemView.meal_orderid.text = String.format(requireContext.getString(R.string.order_id), mealOrders.mealOrderId)
            itemView.meal_billedamount.text = String.format(requireContext.getString(R.string.bill_amount), mealOrders.billedAmount)
            itemView.meal_order_status.text = getMealOrderStatus(requireContext, mealOrders.status!!)
            itemView.total_meals_num.text = String.format(requireContext.getString(R.string.total_num_meal), mealOrders.mealList?.size)
            when (mealOrders.status) {
                MealOrderStatus.IN_PROGRESS.status -> itemView.meal_order_status.setTextColor(requireContext.getColor(R.color.color_yellow))
                MealOrderStatus.READY_TO_PICKUP.status -> itemView.meal_order_status.setTextColor(requireContext.getColor(R.color.color_yellow))
                MealOrderStatus.COMPLETED.status -> {
                    itemView.swipeLayout.setLockDrag(true)
                    itemView.meal_order_status.setTextColor(requireContext.getColor(R.color.orange_app))
                }
                MealOrderStatus.REJECTED.status -> {
                    itemView.swipeLayout.setLockDrag(true)
                    itemView.meal_order_status.setTextColor(requireContext.getColor(R.color.color_red))
                }
                else -> itemView.meal_order_status.setTextColor(requireContext.getColor(R.color.color_brown))
            }
        }
    }
}
package admin.mealbuffet.com.mealnbuffetadmin.nav.orderdashboard.mealdashboard

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.model.MealOrders
import admin.mealbuffet.com.mealnbuffetadmin.nav.InternalActionListener
import admin.mealbuffet.com.mealnbuffetadmin.util.MealOrderStatus
import admin.mealbuffet.com.mealnbuffetadmin.util.getMealOrderStatus
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.meal_order_item_view.view.*


class MealOrderItemsAdapter(private val requireContext: Context, private val wrapActionListener: InternalActionListener) : RecyclerView.Adapter<MealOrderItemsAdapter.MealOrderItemViewHolder>() {

    private var ordersLst: ArrayList<MealOrders>? = null

    fun setData(argBuffetOrdersLst: ArrayList<MealOrders>) {
        ordersLst?.clear()
        this.ordersLst = argBuffetOrdersLst
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealOrderItemViewHolder {
        val view = LayoutInflater.from(requireContext).inflate(R.layout.view_holder_meal_order_item, parent, false)
        return MealOrderItemViewHolder(view, wrapActionListener, requireContext)
    }

    override fun getItemCount(): Int {
        return ordersLst?.size ?: 0
    }

    override fun onBindViewHolder(buffetOrderItemViewHolder: MealOrderItemViewHolder, position: Int) {
        ordersLst?.get(position)?.let { buffetOrderItemViewHolder.setData(it) }
    }

    class MealOrderItemViewHolder(itemView: View, private val internalActionListener: InternalActionListener, private val requireContext: Context) : RecyclerView.ViewHolder(itemView) {
        init {

        }

        fun setData(mealOrders: MealOrders) {
            itemView.tag = mealOrders
            itemView.meal_orderid.text = mealOrders.mealOrderId.toString()
            itemView.meal_billed_amt.text = String.format(requireContext.getString(R.string.cost), mealOrders.billedAmount)
            itemView.meal_order_status.text = getMealOrderStatus(requireContext, mealOrders.status!!)
            itemView.meal_qty_val.text = mealOrders.mealList?.size.toString()
            when (mealOrders.status) {
                MealOrderStatus.ORDERED.status -> {
                    itemView.meal_order_status.setTextColor(requireContext.getColor(R.color.color_yellow))
                    itemView.btn_meal_status_update.visibility = View.VISIBLE
                    itemView.btn_meal_status_update.text = "InProgress"
                    itemView.meal_order_status.text = "Ordered"
                }
                MealOrderStatus.IN_PROGRESS.status -> {
                    itemView.meal_order_status.setTextColor(requireContext.getColor(R.color.color_yellow))
                    itemView.btn_meal_status_update.visibility = View.VISIBLE
                    itemView.btn_meal_status_update.text = "ReadyToPickUp"
                    itemView.meal_order_status.text = "InProgress"
                }
                MealOrderStatus.READY_TO_PICKUP.status -> {
                    itemView.meal_order_status.setTextColor(requireContext.getColor(R.color.color_yellow))
                    itemView.btn_meal_status_update.visibility = View.VISIBLE
                    itemView.btn_meal_status_update.text = "Completed"
                    itemView.meal_order_status.text = "ReadyToPickUp"
                }
                else -> {
                    itemView.meal_order_status.setTextColor(requireContext.getColor(R.color.orange_app))
                    itemView.btn_meal_status_update.visibility = View.GONE
                }
            }
        }
    }
}
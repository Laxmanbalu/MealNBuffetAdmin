package admin.mealbuffet.com.mealnbuffetadmin.nav.orderdashboard.mealdashboard

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.model.MealOrders
import admin.mealbuffet.com.mealnbuffetadmin.nav.InternalActionListener
import admin.mealbuffet.com.mealnbuffetadmin.nav.orderdashboard.mealdashboard.MealOrderBaseFragment.Companion.UPDATE_MEAL_ORDER_STATUS
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
            itemView.btn_meal_status_update.setOnClickListener {
                val mealOrders = it.tag as MealOrders
                internalActionListener.onAction(UPDATE_MEAL_ORDER_STATUS, mealOrders)
            }
        }

        fun setData(mealOrder: MealOrders) {
            itemView.tag = mealOrder
            itemView.btn_meal_status_update.tag = mealOrder
            itemView.meal_orderid.text = mealOrder.mealOrderId.toString()
            itemView.meal_billed_amt.text = String.format(requireContext.getString(R.string.cost), mealOrder.billedAmount)
            itemView.meal_order_status.text = getMealOrderStatus(requireContext, mealOrder.status!!)
            itemView.meal_qty_val.text = mealOrder.mealList?.size.toString()
            itemView.meal_order_date.text = mealOrder.date
            when (mealOrder.status) {
                MealOrderStatus.ORDERED.status -> {
                    itemView.meal_order_status.setTextColor(requireContext.getColor(R.color.color_yellow))
                    itemView.btn_meal_status_update.visibility = View.VISIBLE
                    itemView.btn_meal_status_update.text = requireContext.getString(R.string.inprogress)
                    itemView.meal_order_status.text = requireContext.getString(R.string.str_ordered)
                }
                MealOrderStatus.IN_PROGRESS.status -> {
                    itemView.meal_order_status.setTextColor(requireContext.getColor(R.color.color_yellow))
                    itemView.btn_meal_status_update.visibility = View.VISIBLE
                    itemView.btn_meal_status_update.text = requireContext.getString(R.string.ready_to_pickUp)
                    itemView.meal_order_status.text = requireContext.getString(R.string.inprogress)
                }
                MealOrderStatus.READY_TO_PICKUP.status -> {
                    itemView.meal_order_status.setTextColor(requireContext.getColor(R.color.color_yellow))
                    itemView.btn_meal_status_update.visibility = View.VISIBLE
                    itemView.btn_meal_status_update.text = requireContext.getString(R.string.complete)
                    itemView.meal_order_status.text = requireContext.getString(R.string.ready_to_pickUp)
                }
                else -> {
                    itemView.meal_order_status.setTextColor(requireContext.getColor(R.color.orange_app))
                    itemView.btn_meal_status_update.visibility = View.GONE
                }
            }
        }
    }
}
package admin.mealbuffet.com.mealnbuffetadmin.nav.orderdashboard

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.model.BuffetOrder
import admin.mealbuffet.com.mealnbuffetadmin.nav.InternalActionListener
import admin.mealbuffet.com.mealnbuffetadmin.nav.orderdashboard.BuffetOrderBaseFragment.Companion.UPDATE_BUFFET_ORDER_STATUS
import admin.mealbuffet.com.mealnbuffetadmin.util.BuffetOrderStatus
import admin.mealbuffet.com.mealnbuffetadmin.util.getBuffetOrderStatus
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.buffet_order_item_view.view.*


class BuffetOrderItemsAdapter(private val requireContext: Context, private val wrapActionListener: InternalActionListener) : RecyclerView.Adapter<BuffetOrderItemsAdapter.BuffetOrderItemViewHolder>() {

    private var buffetOrdersLst: ArrayList<BuffetOrder>? = null

    fun setData(argBuffetOrdersLst: ArrayList<BuffetOrder>) {
        buffetOrdersLst?.clear()
        this.buffetOrdersLst = argBuffetOrdersLst
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuffetOrderItemViewHolder {
        val view = LayoutInflater.from(requireContext).inflate(R.layout.view_holder_buffet_order_item, parent, false)
        return BuffetOrderItemViewHolder(view, wrapActionListener, requireContext)
    }

    override fun getItemCount(): Int {
        return buffetOrdersLst?.size ?: 0
    }

    override fun onBindViewHolder(buffetOrderItemViewHolder: BuffetOrderItemViewHolder, position: Int) {
        buffetOrdersLst?.get(position)?.let { buffetOrderItemViewHolder.setData(it) }
    }

    class BuffetOrderItemViewHolder(itemView: View, private val internalActionListener: InternalActionListener, private val requireContext: Context) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.btn_buffet_complete.setOnClickListener {
                val completeBuffetOrder = it.tag as BuffetOrder
                internalActionListener.onAction(UPDATE_BUFFET_ORDER_STATUS, completeBuffetOrder)
            }
        }

        fun setData(buffetOrder: BuffetOrder) {
            itemView.tag = buffetOrder
            itemView.buffet_orderid.text = buffetOrder.orderId
            itemView.buffet_amt.text = String.format(requireContext.getString(R.string.cost), buffetOrder.billedAmount)
            itemView.adults_count.text = buffetOrder.numerOfAudults.toString()
            itemView.kids_count.text = buffetOrder.numberOfKids.toString()
            itemView.order_date.text = buffetOrder.date
            itemView.buffet_order_status.text = getBuffetOrderStatus(requireContext, buffetOrder.status)
            when (buffetOrder.status) {
                BuffetOrderStatus.ORDERED.status -> {
                    itemView.buffet_order_status.setTextColor(requireContext.getColor(R.color.color_green))
                    itemView.btn_buffet_complete.visibility = View.VISIBLE
                    itemView.btn_buffet_complete.tag = buffetOrder
                }
                BuffetOrderStatus.COMPLETED.status -> {
                    itemView.buffet_order_status.setTextColor(requireContext.getColor(R.color.orange_app))
                    itemView.btn_buffet_complete.visibility = View.GONE
                }
                else -> {
                    itemView.buffet_order_status.setTextColor(requireContext.getColor(R.color.color_red))
                    itemView.btn_buffet_complete.visibility = View.GONE
                }
            }
        }
    }
}
package admin.mealbuffet.com.mealnbuffetadmin.nav.orderdashboard

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.model.BuffetOrder
import admin.mealbuffet.com.mealnbuffetadmin.nav.InternalActionListener
import admin.mealbuffet.com.mealnbuffetadmin.nav.orderdashboard.BuffetOrderBoardFragment.Companion.UPDATE_BUFFET_ORDER_STATUS
import admin.mealbuffet.com.mealnbuffetadmin.util.BuffetOrderStatus
import admin.mealbuffet.com.mealnbuffetadmin.util.getBuffetOrderStatus
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.buffet_order_item_view.view.*
import kotlinx.android.synthetic.main.view_holder_buffet_order_item.view.*


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
            itemView.buffet_order_update.setOnClickListener {
                if (itemView.swipeLayout.isOpened) {
                    internalActionListener.onAction(UPDATE_BUFFET_ORDER_STATUS, it.tag as BuffetOrder)
                }
            }
        }

        fun setData(buffetOrder: BuffetOrder) {
            itemView.tag = buffetOrder
            itemView.buffet_order_update.tag = buffetOrder
            itemView.buffet_orderid.text = buffetOrder.orderId
            itemView.buffet_billedamount.text = String.format(requireContext.getString(R.string.bill_amount), buffetOrder.billedAmount)
            itemView.buffet_adults.text = String.format(requireContext.getString(R.string.number_adults), buffetOrder.numerOfAudults)
            itemView.buffet_kids.text = String.format(requireContext.getString(R.string.number_kids), buffetOrder.numberOfKids)
            itemView.buffet_order_status.text = getBuffetOrderStatus(buffetOrder.status)
            when (buffetOrder.status) {
                BuffetOrderStatus.ACCEPTED.status -> itemView.buffet_order_status.setTextColor(requireContext.getColor(R.color.color_green))
                BuffetOrderStatus.COMPLETED.status -> itemView.buffet_order_status.setTextColor(requireContext.getColor(R.color.orange_app))
                BuffetOrderStatus.REJECTED.status -> itemView.buffet_order_status.setTextColor(requireContext.getColor(R.color.color_red))
            }
        }
    }
}
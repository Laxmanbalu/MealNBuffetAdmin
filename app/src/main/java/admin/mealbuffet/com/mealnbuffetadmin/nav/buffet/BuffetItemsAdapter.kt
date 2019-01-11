package admin.mealbuffet.com.mealnbuffetadmin.nav.buffet

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.model.BuffetItem
import admin.mealbuffet.com.mealnbuffetadmin.nav.InternalActionListener
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.buffet_item_view.view.*
import kotlinx.android.synthetic.main.view_holder_add_item.view.*


class BuffetItemsAdapter(private val requireContext: Context, private val wrapActionListener: InternalActionListener) : RecyclerView.Adapter<BuffetItemsAdapter.BuffetItemViewHolder>() {

    private var buffetItemsLst: ArrayList<BuffetItem>? = null

    fun setData(argFoodItemsList: ArrayList<BuffetItem>) {
        buffetItemsLst?.clear()
        this.buffetItemsLst = argFoodItemsList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuffetItemViewHolder {
        val view = LayoutInflater.from(requireContext).inflate(R.layout.view_holder_buffet_item, parent, false)
        return BuffetItemViewHolder(view, wrapActionListener, requireContext)
    }

    override fun getItemCount(): Int {
        return buffetItemsLst?.size ?: 0
    }

    override fun onBindViewHolder(foodItemViewHolder: BuffetItemViewHolder, position: Int) {
        buffetItemsLst?.get(position)?.let { foodItemViewHolder.setData(it) }
    }

    class BuffetItemViewHolder(itemView: View, private val internalActionListener: InternalActionListener, private val requireContext: Context) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.delete.setOnClickListener {
                //                deleteSelectedItem(it.tag as FoodItem)
            }
        }

        /*private fun deleteSelectedItem(foodItem: FoodItem) {
            foodItem.itemCode?.let { it ->
                deleteFoodItems(it, object : ResponseCallback {
                    override fun onSuccess(data: Any?) {
                        internalActionListener.onAction(DELETE_ITEM_SUCCESSFULLY)
                    }

                    override fun onError(data: Any?) {
                        internalActionListener.onAction(DELETE_ITEM_FAILED)
                    }
                })
            }
        }*/

        fun setData(buffetItem: BuffetItem) {
            itemView.tag = buffetItem
            itemView.delete.tag = buffetItem

            itemView.buffet_item_name.text = buffetItem.buffetName
            itemView.buffet_items_desc.text = buffetItem.typeDesc
            itemView.buffet_items_displayName.text = buffetItem.displayName
            itemView.buffet_time_view.buffet_starttime_value.text = buffetItem.startTime
            itemView.buffet_time_view.buffet_endTime_value.text = buffetItem.endTime
            itemView.buffet_price_view.buffet_adult_price.text = buffetItem.adultPrice.toString()
            itemView.buffet_price_view.buffet_kids_price.text = buffetItem.kidsPrice.toString()

            if (buffetItem.activeFlag) {
                itemView.buffet_item_status_icon.setImageResource(R.drawable.ic_active)
            } else {
                itemView.buffet_item_status_icon.setImageResource(R.drawable.ic_inactive)
            }
        }
    }
}
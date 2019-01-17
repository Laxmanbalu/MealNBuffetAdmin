package admin.mealbuffet.com.mealnbuffetadmin.nav.buffet

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.model.BuffetItem
import admin.mealbuffet.com.mealnbuffetadmin.nav.InternalActionListener
import admin.mealbuffet.com.mealnbuffetadmin.nav.buffet.BuffetListFragment.Companion.BUFFET_EDIT
import admin.mealbuffet.com.mealnbuffetadmin.nav.buffet.BuffetListFragment.Companion.DELETED_BUFFET_FAILED
import admin.mealbuffet.com.mealnbuffetadmin.nav.buffet.BuffetListFragment.Companion.DELETED_BUFFET_SUCCESSFULLY
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.PUBLISH_BUFFET
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.UNPUBLISH_BUFFET
import admin.mealbuffet.com.mealnbuffetadmin.network.ResponseCallback
import admin.mealbuffet.com.mealnbuffetadmin.network.changeSelectedItemPublishTypeService
import admin.mealbuffet.com.mealnbuffetadmin.network.deleteBuffetItem
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.buffet_item_view.view.*
import kotlinx.android.synthetic.main.food_item_swipe_view_publish.view.*
import kotlinx.android.synthetic.main.view_holder_buffet_item.view.*


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
                if(itemView.swipeLayout.isOpened) {
                    deleteSelectedItem(it.tag as BuffetItem)
                }
            }

            itemView.publish.setOnClickListener {
                if(itemView.swipeLayout.isOpened) {
                    changePublishTypeOfBuffet(it.tag as BuffetItem)
                }
            }

            itemView.edit.setOnClickListener {
                if(itemView.swipeLayout.isOpened) {
                    internalActionListener.onAction(BUFFET_EDIT, it.tag as BuffetItem)
                }
            }
        }

        private fun changePublishTypeOfBuffet(buffetItem: BuffetItem) {
            if (buffetItem.buffetId == null) {
                internalActionListener.onAction(DELETED_BUFFET_FAILED)
                return
            }
            var requestUrl = if (buffetItem.activeFlag) {
                UNPUBLISH_BUFFET
            } else {
                PUBLISH_BUFFET
            }
            requestUrl = String.format(requestUrl, buffetItem.restaurantId, buffetItem.buffetId)

            changeSelectedItemPublishTypeService(requestUrl, object : ResponseCallback {
                override fun onSuccess(data: Any?) {
                    internalActionListener.onAction(BuffetListFragment.PUBLISHED_BUFFET_SUCCESSFULLY)
                }

                override fun onError(data: Any?) {
                    internalActionListener.onAction(BuffetListFragment.PUBLISHED_BUFFET_FAILED)
                }
            })
        }

        private fun deleteSelectedItem(buffetItem: BuffetItem) {
            if (buffetItem.buffetId == null) {
                internalActionListener.onAction(DELETED_BUFFET_FAILED)
                return
            }
            val requestUrl = String.format(MealAdminUrls.DELETE_BUFFET_ITEM, buffetItem.buffetId)
            deleteBuffetItem(requestUrl, object : ResponseCallback {
                override fun onSuccess(data: Any?) {
                    internalActionListener.onAction(DELETED_BUFFET_SUCCESSFULLY)
                }

                override fun onError(data: Any?) {
                    internalActionListener.onAction(DELETED_BUFFET_FAILED)
                }
            })
        }

        fun setData(buffetItem: BuffetItem) {
            itemView.tag = buffetItem
            itemView.delete.tag = buffetItem
            itemView.publish.tag = buffetItem
            itemView.edit.tag = buffetItem

            itemView.buffet_item_name.text = buffetItem.buffetName
            itemView.buffet_items_desc.text = buffetItem.typeDesc
            itemView.buffet_items_displayName.text = buffetItem.displayName
            itemView.buffet_time_view.buffet_starttime_value.text = buffetItem.startTime
            itemView.buffet_time_view.buffet_endTime_value.text = buffetItem.endTime
            itemView.buffet_price_view.buffet_adult_price.text = buffetItem.adultPrice.toString()
            itemView.buffet_price_view.buffet_kids_price.text = buffetItem.kidsPrice.toString()


            if (buffetItem.activeFlag) {
                itemView.buffet_item_status.setTextColor(requireContext.getColor(R.color.color_green))
                itemView.buffet_item_status.text = requireContext.getString(R.string.published)
                itemView.swipeLayout.publish.tv_publish.text = requireContext.getString(R.string.unpublish)
            } else {
                itemView.buffet_item_status.setTextColor(requireContext.getColor(R.color.color_red))
                itemView.buffet_item_status.text = requireContext.getString(R.string.unpublished)
                itemView.swipeLayout.publish.tv_publish.text = requireContext.getString(R.string.publish)
            }
        }
    }
}
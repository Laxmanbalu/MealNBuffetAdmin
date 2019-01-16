package admin.mealbuffet.com.mealnbuffetadmin.nav.meal

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.model.MealItem
import admin.mealbuffet.com.mealnbuffetadmin.nav.InternalActionListener
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.food_item_swipe_view_publish.view.*
import kotlinx.android.synthetic.main.meal_item_view.view.*
import kotlinx.android.synthetic.main.view_holder_meal_item.view.*

class MealItemAdapter(private val requireContext: Context, private val wrapActionListener: InternalActionListener) : RecyclerView.Adapter<MealItemAdapter.MealItemViewHolder>() {

    private var mealItemsLst: ArrayList<MealItem>? = null

    fun setData(argFoodItemsList: ArrayList<MealItem>) {
        mealItemsLst?.clear()
        this.mealItemsLst = argFoodItemsList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealItemAdapter.MealItemViewHolder {
        val view = LayoutInflater.from(requireContext).inflate(R.layout.view_holder_meal_item, parent, false)
        return MealItemAdapter.MealItemViewHolder(view, wrapActionListener, requireContext)
    }

    override fun getItemCount(): Int {
        return mealItemsLst?.size ?: 0
    }

    override fun onBindViewHolder(mealItemViewHolder: MealItemAdapter.MealItemViewHolder, position: Int) {
        mealItemsLst?.get(position)?.let { mealItemViewHolder.setData(it) }
    }


    class MealItemViewHolder(itemView: View, private val internalActionListener: InternalActionListener, private val requireContext: Context) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.delete.setOnClickListener {
                if (itemView.swipeLayout.isOpened) {
                    deleteSelectedItem(it.tag as MealItem)
                }
            }

            itemView.publish.setOnClickListener {
                if (itemView.swipeLayout.isOpened) {
                    changePublishTypeOfBuffet(it.tag as MealItem)
                }
            }

            itemView.edit.setOnClickListener {
                if (itemView.swipeLayout.isOpened) {
//                    internalActionListener.onAction(BuffetListFragment.BUFFET_EDIT, it.tag as MealItem)
                }
            }
        }

        private fun changePublishTypeOfBuffet(buffetItem: MealItem) {
            /*if (buffetItem.mealId == null) {
                internalActionListener.onAction(BuffetListFragment.DELETED_BUFFET_FAILED)
                return
            }
            var requestUrl = if (buffetItem.activeFlag) {
                MealAdminUrls.UNPUBLISH_BUFFET
            } else {
                MealAdminUrls.PUBLISH_BUFFET
            }*/
//            requestUrl = String.format(requestUrl, buffetItem.restaurantId, buffetItem.buffetId)

            /* changeBuffetPublishTypeService(requestUrl, object : ResponseCallback {
                 override fun onSuccess(data: Any?) {
                     internalActionListener.onAction(BuffetListFragment.PUBLISHED_BUFFET_SUCCESSFULLY)
                 }

                 override fun onError(data: Any?) {
                     internalActionListener.onAction(BuffetListFragment.PUBLISHED_BUFFET_FAILED)
                 }
             })*/
        }

        private fun deleteSelectedItem(mealItem: MealItem) {
            if (mealItem.mealId == null) {
//                internalActionListener.onAction(BuffetListFragment.DELETED_BUFFET_FAILED)
                return
            }
            /*deleteBuffetItem(buffetItem.buffetId, object : ResponseCallback {
                override fun onSuccess(data: Any?) {
                    internalActionListener.onAction(BuffetListFragment.DELETED_BUFFET_SUCCESSFULLY)
                }

                override fun onError(data: Any?) {
                    internalActionListener.onAction(BuffetListFragment.DELETED_BUFFET_FAILED)
                }
            })*/
        }


        fun setData(mealItem: MealItem) {
            itemView.tag = mealItem
            itemView.delete.tag = mealItem
            itemView.publish.tag = mealItem
            itemView.edit.tag = mealItem

            itemView.meal_item_name.text = mealItem.mealName
            itemView.meal_items_desc.text = mealItem.complimentory
            itemView.meal_items_maxqty.text = String.format(requireContext.getString(R.string.max_meal), mealItem.itemsQty)

            if (mealItem.activeFlag) {
                itemView.meal_item_status.setTextColor(requireContext.getColor(R.color.color_green))
                itemView.meal_item_status.text = requireContext.getString(R.string.published)
                itemView.swipeLayout.publish.tv_publish.text = requireContext.getString(R.string.unpublish)
            } else {
                itemView.meal_item_status.setTextColor(requireContext.getColor(R.color.color_red))
                itemView.meal_item_status.text = requireContext.getString(R.string.unpublished)
                itemView.meal_item_status.publish.tv_publish.text = requireContext.getString(R.string.publish)
            }
        }

    }

}
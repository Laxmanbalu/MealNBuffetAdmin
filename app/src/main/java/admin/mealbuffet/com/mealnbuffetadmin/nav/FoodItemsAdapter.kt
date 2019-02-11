package admin.mealbuffet.com.mealnbuffetadmin.nav

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.model.FoodItem
import admin.mealbuffet.com.mealnbuffetadmin.nav.ItemsListFragment.Companion.DELETE_ITEM_FAILED
import admin.mealbuffet.com.mealnbuffetadmin.nav.ItemsListFragment.Companion.DELETE_ITEM_SUCCESSFULLY
import admin.mealbuffet.com.mealnbuffetadmin.nav.ItemsListFragment.Companion.EDIT_FOOD_ITEM
import admin.mealbuffet.com.mealnbuffetadmin.network.ResponseCallback
import admin.mealbuffet.com.mealnbuffetadmin.network.deleteFoodItems
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fooditem_view.view.*
import kotlinx.android.synthetic.main.view_holder_add_item.view.*


class FoodItemsAdapter(private val requireContext: Context, private val wrapActionListener: InternalActionListener) : RecyclerView.Adapter<FoodItemsAdapter.FoodItemViewHolder>() {

    private var foodItemsLst: ArrayList<FoodItem>? = null

    fun setData(argFoodItemsList: ArrayList<FoodItem>) {
        foodItemsLst?.clear()
        this.foodItemsLst = argFoodItemsList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodItemViewHolder {
        val view = LayoutInflater.from(requireContext).inflate(R.layout.view_holder_add_item, parent, false)
        return FoodItemViewHolder(view, wrapActionListener, requireContext)
    }

    override fun getItemCount(): Int {
        return foodItemsLst?.size ?: 0
    }

    override fun onBindViewHolder(foodItemViewHolder: FoodItemViewHolder, position: Int) {
        foodItemsLst?.get(position)?.let { foodItemViewHolder.setData(it) }
        updateFoodItemImage(foodItemViewHolder, position)
    }

    private fun updateFoodItemImage(foodItemViewHolder: FoodItemViewHolder, position: Int) {
        Glide.with(requireContext)
                .load(foodItemsLst?.get(position)?.image)
                .into(foodItemViewHolder.itemView.food_item_icon)
    }

    class FoodItemViewHolder(itemView: View, private val internalActionListener: InternalActionListener, private val requireContext: Context) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.foodItemMainView.setOnClickListener {
                val foodItem = itemView.tag as FoodItem
                if (foodItem.checked == false) {
                    itemView.food_item_check.visibility = View.VISIBLE
                    foodItem.checked = true
                } else {
                    itemView.food_item_check.visibility = View.GONE
                    foodItem.checked = false
                }
            }

            itemView.delete.setOnClickListener {
                if (itemView.swipeLayout.isOpened) {
                    deleteSelectedItem(it.tag as FoodItem)
                }
            }

            itemView.edit.setOnClickListener {
                if (itemView.swipeLayout.isOpened) {
                    internalActionListener.onAction(EDIT_FOOD_ITEM, it.tag as FoodItem)
                }
            }
        }

        private fun deleteSelectedItem(foodItem: FoodItem) {
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
        }

        fun setData(foodItem: FoodItem) {
            itemView.tag = foodItem
            itemView.edit.tag = foodItem
            itemView.delete.tag = foodItem
            itemView.food_items_desc.text = foodItem.desc
            itemView.food_item_name.text = foodItem.item
            itemView.food_items_category.text = foodItem.categoryId
            itemView.food_items_price.text = String.format(requireContext.getString(R.string.item_price), foodItem.price)
            if (foodItem.checked == false) {
                itemView.food_item_check.visibility = View.GONE
            } else {
                itemView.food_item_check.visibility = View.VISIBLE
            }
            if (foodItem.status.equals(FOODITEM_STATUS_ACTIVE)) {
                itemView.food_item_status.setTextColor(requireContext.getColor(R.color.color_green))
                itemView.food_item_status.text = requireContext.getString(R.string.published)
            } else {
                itemView.food_item_status.setTextColor(requireContext.getColor(R.color.color_red))
                itemView.food_item_status.text = requireContext.getString(R.string.unpublished)
            }
        }

        companion object {
            const val FOODITEM_STATUS_ACTIVE = "Active"
        }
    }
}
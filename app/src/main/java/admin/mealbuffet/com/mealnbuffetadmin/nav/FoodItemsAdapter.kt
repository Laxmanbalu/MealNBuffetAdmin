package admin.mealbuffet.com.mealnbuffetadmin.nav

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.model.FoodItem
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.mealbuffet.controller.ActionListener
import kotlinx.android.synthetic.main.fooditem_view.view.*


class FoodItemsAdapter(private val requireContext: Context, private val wrapActionListener: ActionListener) : RecyclerView.Adapter<FoodItemsAdapter.FoodItemViewHolder>() {

    private var foodItemsLst: ArrayList<FoodItem>? = null

    fun setData(arrayList: ArrayList<FoodItem>) {
        this.foodItemsLst = arrayList
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

    class FoodItemViewHolder(itemView: View, wrapActionListener: ActionListener, private val requireContext: Context) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                val foodItem = itemView.tag as FoodItem
                if (foodItem.checked == false) {
                    itemView.food_item_check.visibility = View.VISIBLE
                    foodItem.checked = true
                } else {
                    itemView.food_item_check.visibility = View.GONE
                    foodItem.checked = false
                }
            }
        }

        fun setData(foodItem: FoodItem) {
            itemView.tag = foodItem
            itemView.food_items_desc.text = foodItem.desc
            itemView.food_item_name.text = foodItem.item
            itemView.food_items_category.text = foodItem.categoryId
            itemView.food_items_price.text = String.format(requireContext.getString(R.string.item_price), foodItem.price)
            if (foodItem.checked == false) {
                itemView.food_item_check.visibility = View.GONE
            } else {
                itemView.food_item_check.visibility = View.VISIBLE
            }
        }
    }
}
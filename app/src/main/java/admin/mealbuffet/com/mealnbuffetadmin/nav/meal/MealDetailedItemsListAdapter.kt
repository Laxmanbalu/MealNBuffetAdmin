package admin.mealbuffet.com.mealnbuffetadmin.nav.meal

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.model.BMFoodItem
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.buffet_food_item_view.view.*
import kotlinx.android.synthetic.main.buffet_fooditem_expand_view.view.*
import java.util.*

class MealDetailedItemsListAdapter(var context: Context) : BaseExpandableListAdapter() {

    private var expandableListDetail: HashMap<String, List<BMFoodItem>>? = null
    private var expandableListTitle: List<String>? = null

    fun setData(argExpandableListDetail: HashMap<String, List<BMFoodItem>>) {
        expandableListDetail?.clear()
        expandableListDetail = argExpandableListDetail
        expandableListTitle = ArrayList(argExpandableListDetail.keys)
    }

    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
        return this.expandableListDetail?.get(this.expandableListTitle?.get(listPosition))!![expandedListPosition]
    }

    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }

    override fun getChildView(listPosition: Int, expandedListPosition: Int,
                              isLastChild: Boolean, convertView: View?, parent: ViewGroup): View {

        var convertView = convertView
        val foodItemDetails = getChild(listPosition, expandedListPosition) as BMFoodItem
        if (convertView == null) {
            val layoutInflater = this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.buffet_food_item_view, null)
        }
        convertView?.food_item_name?.text = foodItemDetails.item
        convertView?.food_items_desc?.text = foodItemDetails.desc
        convertView?.food_icon?.let { updateRestaurantImage(it, foodItemDetails) }
        convertView?.tag = foodItemDetails
        return convertView!!
    }

    private fun updateRestaurantImage(food_icon: ImageView, foodItemDetails: BMFoodItem) {
        Glide.with(context)
                .load(foodItemDetails.image)
                .into(food_icon)
    }


    override fun getChildrenCount(listPosition: Int): Int {
        return this.expandableListDetail?.get(this.expandableListTitle?.get(listPosition))!!.size
    }

    override fun getGroup(listPosition: Int): Any {
        return this.expandableListTitle?.get(listPosition)!!
    }

    override fun getGroupCount(): Int {
        return this.expandableListTitle?.size ?: 0
    }

    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }

    override fun getGroupView(listPosition: Int, isExpanded: Boolean,
                              convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val listTitle = getGroup(listPosition) as String
        if (convertView == null) {
            val layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.buffet_fooditem_expand_view, null)
        }
        convertView?.expand_view_title?.setTypeface(null, Typeface.BOLD)
        convertView?.expand_view_title?.text = listTitle
        return convertView!!
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }
}
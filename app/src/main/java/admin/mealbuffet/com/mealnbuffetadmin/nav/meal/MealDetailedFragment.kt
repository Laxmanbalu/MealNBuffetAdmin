package admin.mealbuffet.com.mealnbuffetadmin.nav.meal

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.model.FoodItem
import admin.mealbuffet.com.mealnbuffetadmin.model.MealItem
import android.os.Bundle
import android.view.View
import com.mealbuffet.controller.BaseFragment
import kotlinx.android.synthetic.main.fragment_buffet_fooditems.*

class MealDetailedFragment : BaseFragment() {
    private lateinit var expandableListDetail: HashMap<String, List<FoodItem>>
    private lateinit var expandableListTitle: List<String>
    private lateinit var expandableListAdapter: MealDetailedItemsListAdapter
    lateinit var mealItem: MealItem

    override fun layoutResource(): Int = R.layout.fragment_meal_detailed_items

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderBuffetExpandableListView()
    }

    private fun renderBuffetExpandableListView() {
        expandableListTitle = ArrayList(mealItem.items?.keys)
        expandableListAdapter = MealDetailedItemsListAdapter(requireContext())
        mealItem.items?.let { expandableListAdapter.setData(it) }
        expandableListView.setAdapter(expandableListAdapter)
        expandableListTitle.forEachIndexed { index, s ->
            expandableListView.expandGroup(index)
        }
    }
}
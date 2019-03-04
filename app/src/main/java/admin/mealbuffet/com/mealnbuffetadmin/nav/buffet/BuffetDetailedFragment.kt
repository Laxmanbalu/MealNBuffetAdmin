package admin.mealbuffet.com.mealnbuffetadmin.nav.buffet

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.model.BuffetItem
import admin.mealbuffet.com.mealnbuffetadmin.model.FoodItem
import android.os.Bundle
import android.view.View
import com.mealbuffet.controller.BaseFragment
import kotlinx.android.synthetic.main.fragment_buffet_fooditems.*

class BuffetDetailedFragment : BaseFragment() {
    lateinit var buffetItem: BuffetItem
    private lateinit var expandableListAdapter: BuffetDetailedItemsListAdapter
    private lateinit var expandableListTitle: List<String>

    override fun layoutResource(): Int = R.layout.fragment_buffet_detailed_items

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderBuffetExpandableListView()
    }


    private fun renderBuffetExpandableListView() {
        expandableListTitle = ArrayList(buffetItem.items?.keys)
        expandableListAdapter = BuffetDetailedItemsListAdapter(requireContext())
        buffetItem.items?.let { expandableListAdapter.setData(it) }
        expandableListView.setAdapter(expandableListAdapter)
        expandableListTitle.forEachIndexed { index, s ->
            expandableListView.expandGroup(index)
        }
    }

}
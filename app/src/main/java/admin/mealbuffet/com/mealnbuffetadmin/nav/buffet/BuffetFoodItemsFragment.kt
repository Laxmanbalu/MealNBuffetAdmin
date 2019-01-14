package admin.mealbuffet.com.mealnbuffetadmin.nav.buffet

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.model.FoodItem
import admin.mealbuffet.com.mealnbuffetadmin.network.ResponseCallback
import admin.mealbuffet.com.mealnbuffetadmin.network.getActiveItemsList
import admin.mealbuffet.com.mealnbuffetadmin.util.PreferencesHelper
import com.mealbuffet.controller.BaseFragment
import kotlinx.android.synthetic.main.fragment_buffet_fooditems.*

class BuffetFoodItemsFragment : BaseFragment() {

    private lateinit var expandableListDetail: HashMap<String, List<FoodItem>>
    private lateinit var expandableListTitle: List<String>
    private lateinit var expandableListAdapter: BuffetCustomExpandableListAdapter

    override fun layoutResource(): Int = R.layout.fragment_buffet_fooditems

    override fun onResume() {
        super.onResume()
        fetchActiveFoodItems()
    }

    private fun fetchActiveFoodItems() {
        val restaurantId = PreferencesHelper.getRestaurantId(requireContext())
        getActiveItemsList(restaurantId, object : ResponseCallback {
            override fun onSuccess(data: Any?) {
                expandableListDetail = data as HashMap<String, List<FoodItem>>
                renderBuffetExpandableListView()
            }

            override fun onError(data: Any?) {
                showNetworkError()
            }
        })
    }

    private fun renderBuffetExpandableListView() {
        expandableListTitle = ArrayList(expandableListDetail.keys)
        expandableListAdapter = BuffetCustomExpandableListAdapter(requireContext())
        expandableListAdapter.setData(expandableListDetail)
        expandableListView.setAdapter(expandableListAdapter)
        expandableListTitle.forEachIndexed { index, s ->
            expandableListView.expandGroup(index)
        }
    }
}
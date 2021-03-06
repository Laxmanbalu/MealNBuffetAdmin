package admin.mealbuffet.com.mealnbuffetadmin.nav.buffet

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.custom.InfoDialog
import admin.mealbuffet.com.mealnbuffetadmin.model.CreateBuffetItem
import admin.mealbuffet.com.mealnbuffetadmin.model.EditBuffetData
import admin.mealbuffet.com.mealnbuffetadmin.model.FoodItem
import admin.mealbuffet.com.mealnbuffetadmin.network.ResponseCallback
import admin.mealbuffet.com.mealnbuffetadmin.network.getActiveItemsList
import admin.mealbuffet.com.mealnbuffetadmin.network.updateSelectedBuffetItem
import admin.mealbuffet.com.mealnbuffetadmin.util.Constants.EMPTY_STRING
import admin.mealbuffet.com.mealnbuffetadmin.util.PreferencesHelper
import android.os.Bundle
import android.view.View
import com.mealbuffet.controller.BaseFragment
import kotlinx.android.synthetic.main.fragment_edit_buffet_fooditems.*

class EditBuffetFoodItemsFragment : BaseFragment() {

    private lateinit var expandableListDetail: HashMap<String, List<FoodItem>>
    private lateinit var expandableListTitle: List<String>
    private lateinit var expandableListAdapter: BuffetCustomExpandableListAdapter
    private lateinit var editBuffetBasicData: EditBuffetData

    override fun layoutResource(): Int = R.layout.fragment_edit_buffet_fooditems

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_editbuffet.setOnClickListener {
            updateBuffetItem()
        }
    }

    private fun updateBuffetItem() {
        val arrayList = ArrayList<String>()
        for (key in expandableListDetail.keys) {
            val data = expandableListDetail.get(key) as List<FoodItem>
            val selectedFoodItems = data.filter { it.checked == true }
            selectedFoodItems.forEach {
                it.itemCode?.let { it1 -> arrayList.add(it1) }
            }
        }

        if (arrayList.isEmpty()) {
            showFoodSelectionError(getString(R.string.buffet_fooditems_select))
            return
        }
        val buffetItem = CreateBuffetItem(status = 1, typeDes = EMPTY_STRING, buffetBasicData = editBuffetBasicData.buffetBasicData, itemsList = arrayList)
        updateSelectedBuffetItem(buffetItem, editBuffetBasicData.buffetItem.buffetId, object : ResponseCallback {
            override fun onSuccess(data: Any?) {
                wrapActionListener().onAction(BUFFET_ADDED_SUCCESSFULLY)
            }

            override fun onError(data: Any?) {
                showNetworkError()
            }
        })
    }

    private fun showFoodSelectionError(message: String) {
        val dialog = InfoDialog.newInstance(message)
        dialog.show(activity?.supportFragmentManager, message)
    }

    override fun onResume() {
        super.onResume()
        fetchActiveFoodItems()
    }

    private fun fetchActiveFoodItems() {
        val restaurantId = PreferencesHelper.getRestaurantId(requireContext())
        getActiveItemsList(restaurantId, object : ResponseCallback {
            override fun onSuccess(data: Any?) {
                expandableListDetail = data as HashMap<String, List<FoodItem>>
                updateSelectedFoddItems()
                renderBuffetExpandableListView()
            }

            override fun onError(data: Any?) {
                showNetworkError()
            }
        })
    }

    private fun updateSelectedFoddItems() {
        for (key in expandableListDetail.keys) {
            val data = expandableListDetail.get(key) as List<FoodItem>
            data.forEach { foodItem ->
                val buffetItems = editBuffetBasicData.buffetItem.items
                for (buffetKey in buffetItems?.keys!!) {
                    val buffetFoodItems = buffetItems.get(buffetKey)
                    buffetFoodItems?.forEach { bmFoodItem ->
                        if (bmFoodItem.itemCode == foodItem.itemCode) {
                            foodItem.checked = true

                        }
                    }
                }
            }
        }
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

    fun setBasicBuffetData(argBuffetBasicData: EditBuffetData) {
        editBuffetBasicData = argBuffetBasicData
    }

    companion object {
        const val BUFFET_ADDED_SUCCESSFULLY = "BuffetAddedSuccessfully"
    }
}
package admin.mealbuffet.com.mealnbuffetadmin.nav.buffet

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.custom.InfoDialog
import admin.mealbuffet.com.mealnbuffetadmin.model.BuffetBasicData
import admin.mealbuffet.com.mealnbuffetadmin.model.CreateBuffetItem
import admin.mealbuffet.com.mealnbuffetadmin.model.FoodItem
import admin.mealbuffet.com.mealnbuffetadmin.network.ResponseCallback
import admin.mealbuffet.com.mealnbuffetadmin.network.addNewBuffet
import admin.mealbuffet.com.mealnbuffetadmin.network.getActiveItemsList
import admin.mealbuffet.com.mealnbuffetadmin.util.Constants.EMPTY_STRING
import admin.mealbuffet.com.mealnbuffetadmin.util.PreferencesHelper
import android.os.Bundle
import android.view.View
import com.mealbuffet.controller.BaseFragment
import kotlinx.android.synthetic.main.fragment_buffet_fooditems.*

class BuffetFoodItemsFragment : BaseFragment() {

    private lateinit var expandableListDetail: HashMap<String, List<FoodItem>>
    private lateinit var expandableListTitle: List<String>
    private lateinit var expandableListAdapter: BuffetCustomExpandableListAdapter
    private lateinit var buffetBasicData: BuffetBasicData

    override fun layoutResource(): Int = R.layout.fragment_buffet_fooditems

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_createbuffet.setOnClickListener {
            createNewBuffet()
        }
    }

    private fun createNewBuffet() {
        val arrayList = ArrayList<String>()
        for (key in expandableListDetail.keys) {
            val data = expandableListDetail.get(key) as List<FoodItem>
            val selectedFoodItems = data.filter { it.checked == true }
            selectedFoodItems.forEach {
                it.itemCode?.let { it1 -> arrayList.add(it1) }
            }
        }

        if (arrayList.isEmpty()) {
            showItemSelectionError(getString(R.string.buffet_fooditems_select))
            return
        }

        val buffetItem = CreateBuffetItem(status = 1, typeDes = EMPTY_STRING, buffetBasicData = buffetBasicData, itemsList = arrayList)
        addNewBuffet(buffetItem, object : ResponseCallback {
            override fun onSuccess(data: Any?) {
                wrapActionListener().onAction(BUFFET_ADDED_SUCCESSFULLY)
            }

            override fun onError(data: Any?) {
                showNetworkError()
            }
        })
    }


    private fun showItemSelectionError(message: String) {
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

    fun setBasicBuffetData(argBuffetBasicData: BuffetBasicData) {
        buffetBasicData = argBuffetBasicData
    }

    companion object {
        const val BUFFET_ADDED_SUCCESSFULLY = "BuffetAddedSuccessfully"
    }
}
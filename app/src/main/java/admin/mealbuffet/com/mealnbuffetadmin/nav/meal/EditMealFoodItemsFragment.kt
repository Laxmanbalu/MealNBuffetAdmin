package admin.mealbuffet.com.mealnbuffetadmin.nav.meal

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.custom.InfoDialog
import admin.mealbuffet.com.mealnbuffetadmin.model.CreateMealItem
import admin.mealbuffet.com.mealnbuffetadmin.model.EditMealData
import admin.mealbuffet.com.mealnbuffetadmin.model.FoodItem
import admin.mealbuffet.com.mealnbuffetadmin.network.ResponseCallback
import admin.mealbuffet.com.mealnbuffetadmin.network.getActiveItemsList
import admin.mealbuffet.com.mealnbuffetadmin.network.updateSelectedMeal
import admin.mealbuffet.com.mealnbuffetadmin.util.Constants.EMPTY_STRING
import admin.mealbuffet.com.mealnbuffetadmin.util.PreferencesHelper
import android.os.Bundle
import android.view.View
import com.mealbuffet.controller.BaseFragment
import kotlinx.android.synthetic.main.fragment_edit_buffet_fooditems.*

class EditMealFoodItemsFragment : BaseFragment() {

    private lateinit var expandableListDetail: HashMap<String, List<FoodItem>>
    private lateinit var expandableListTitle: List<String>
    private lateinit var expandableListAdapter: MealCustomExpandableListAdapter
    private lateinit var editMealData: EditMealData

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
        val mealItem = CreateMealItem(status = 1, typeDes = EMPTY_STRING, mealBasicData = editMealData.mealBasicData, itemsList = arrayList)
        /*updateSelectedBuffetItem(buffetItem, editMealData.mealItem.mealId, object : ResponseCallback {
            override fun onSuccess(data: Any?) {
                wrapActionListener().onAction(BUFFET_ADDED_SUCCESSFULLY)
            }

            override fun onError(data: Any?) {
                showNetworkError()
            }
        })*/

        updateSelectedMeal(mealItem, editMealData.mealItem.mealId, object : ResponseCallback {
            override fun onSuccess(data: Any?) {
                wrapActionListener().onAction(MEAL_UPDATED_SUCCESSFULLY)
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
                val buffetItems = editMealData.mealItem.items
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
        expandableListAdapter = MealCustomExpandableListAdapter(requireContext())
        expandableListAdapter.setData(expandableListDetail)
        expandableListView.setAdapter(expandableListAdapter)
        expandableListTitle.forEachIndexed { index, s ->
            expandableListView.expandGroup(index)
        }
    }

    fun setMealData(argMealData: EditMealData) {
        editMealData = argMealData
    }

    companion object {
        const val MEAL_UPDATED_SUCCESSFULLY = "MealUpdatedSuccessfully"
    }
}
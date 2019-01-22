package admin.mealbuffet.com.mealnbuffetadmin.nav.meal

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.custom.InfoDialog
import admin.mealbuffet.com.mealnbuffetadmin.model.CreateMealItem
import admin.mealbuffet.com.mealnbuffetadmin.model.FoodItem
import admin.mealbuffet.com.mealnbuffetadmin.model.MealBasicData
import admin.mealbuffet.com.mealnbuffetadmin.network.ResponseCallback
import admin.mealbuffet.com.mealnbuffetadmin.network.createMealService
import admin.mealbuffet.com.mealnbuffetadmin.network.getActiveItemsList
import admin.mealbuffet.com.mealnbuffetadmin.util.Constants.EMPTY_STRING
import admin.mealbuffet.com.mealnbuffetadmin.util.PreferencesHelper
import android.os.Bundle
import android.view.View
import com.mealbuffet.controller.BaseFragment
import kotlinx.android.synthetic.main.fragment_buffet_fooditems.*

class MealFoodItemsFragment : BaseFragment() {

    private lateinit var expandableListDetail: HashMap<String, List<FoodItem>>
    private lateinit var expandableListTitle: List<String>
    private lateinit var expandableListAdapter: MealCustomExpandableListAdapter
    private lateinit var mealBasicData: MealBasicData

    override fun layoutResource(): Int = R.layout.fragment_buffet_fooditems

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_createbuffet.text = getString(R.string.create_meal)
        btn_createbuffet.setOnClickListener {
            createMeal()
        }
    }

    private fun createMeal() {
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

        val buffetItem = CreateMealItem(status = 1, typeDes = EMPTY_STRING, mealBasicData = mealBasicData, itemsList = arrayList)
        createMealService(buffetItem, object : ResponseCallback {
            override fun onSuccess(data: Any?) {
                wrapActionListener().onAction(MEAL_ADDED_SUCCESSFULLY)
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
        expandableListAdapter = MealCustomExpandableListAdapter(requireContext())
        expandableListAdapter.setData(expandableListDetail)
        expandableListView.setAdapter(expandableListAdapter)
        expandableListTitle.forEachIndexed { index, s ->
            expandableListView.expandGroup(index)
        }
    }

    fun setMealBasicData(argBuffetBasicData: MealBasicData) {
        mealBasicData = argBuffetBasicData
    }

    companion object {
        const val MEAL_ADDED_SUCCESSFULLY = "MEALAddedSuccessfully"
    }
}
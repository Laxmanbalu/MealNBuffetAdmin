package admin.mealbuffet.com.mealnbuffetadmin.nav.meal

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.model.MealItem
import admin.mealbuffet.com.mealnbuffetadmin.network.ResponseCallback
import admin.mealbuffet.com.mealnbuffetadmin.network.getMealsList
import admin.mealbuffet.com.mealnbuffetadmin.util.PreferencesHelper
import android.os.Bundle
import android.util.Log
import android.view.View
import com.mealbuffet.controller.BaseFragment

class MealListFragment : BaseFragment() {
    override fun layoutResource(): Int = R.layout.fragment_meallist

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchMealsList()
    }

    private fun fetchMealsList() {
        val restaurantId = PreferencesHelper.getRestaurantId(requireContext())
        getMealsList(restaurantId, object : ResponseCallback {
            override fun onSuccess(data: Any?) {
                if (requireActivity() != null) {
                    val resultData = data as MealItem
                    Log.d("TEST123", "MealData: " + resultData.toString())
                }
            }

            override fun onError(data: Any?) {
                Log.d("TEST123", "something wrong: ")
            }

        })
    }
}
package admin.mealbuffet.com.mealnbuffetadmin.nav

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.util.PreferencesHelper
import admin.mealbuffet.com.mealnbuffetadmin.viewmodel.FoodItemListViewModel
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import com.mealbuffet.controller.BaseFragment

class ItemsListFragment : BaseFragment() {
    override fun layoutResource(): Int = R.layout.fragment_itemslist
    private lateinit var foodItemListViewModel: FoodItemListViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFoodItemsListViewModel()
    }

    private fun initFoodItemsListViewModel() {
        foodItemListViewModel = ViewModelProviders.of(this).get(FoodItemListViewModel::class.java)
        foodItemListViewModel.liveData.observe(this, Observer {
            hideProgress()
        })
        showProgress()
        val restaurantId = PreferencesHelper.getRestaurantId(requireContext())
        foodItemListViewModel.getFoodItemsData(restaurantId)
    }
}
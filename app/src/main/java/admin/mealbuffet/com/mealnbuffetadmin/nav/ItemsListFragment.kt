package admin.mealbuffet.com.mealnbuffetadmin.nav

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.R.id.menu_list_add_item
import admin.mealbuffet.com.mealnbuffetadmin.model.FoodItem
import admin.mealbuffet.com.mealnbuffetadmin.util.PreferencesHelper
import admin.mealbuffet.com.mealnbuffetadmin.viewmodel.FoodItemListViewModel
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.mealbuffet.controller.BaseFragment
import kotlinx.android.synthetic.main.fragment_itemslist.*

class ItemsListFragment : BaseFragment() {
    override fun layoutResource(): Int = R.layout.fragment_itemslist
    private lateinit var foodItemListViewModel: FoodItemListViewModel
    private lateinit var foodItemsAdapter: FoodItemsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFoodItemsListViewModel()
        setHasOptionsMenu(true)
    }

    private fun initFoodItemsListViewModel() {
        foodItemListViewModel = ViewModelProviders.of(this).get(FoodItemListViewModel::class.java)
        foodItemListViewModel.liveData.observe(this, Observer {
            if (it == null) {
                showNetworkError()
            } else {
                renderFoodItemsView(it)
            }
        })
        val restaurantId = PreferencesHelper.getRestaurantId(requireContext())
        foodItemListViewModel.getFoodItemsData(restaurantId)
    }

    private fun renderFoodItemsView(foodItemsLst: ArrayList<FoodItem>) {
        foodItemsAdapter = FoodItemsAdapter(requireContext(), wrapActionListener())
        foodItems_recyclerView.apply {
            adapter = foodItemsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        foodItems_recyclerView.itemAnimator = DefaultItemAnimator()
        foodItemsAdapter.setData(foodItemsLst)
        foodItemsAdapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_add_item, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            menu_list_add_item -> {
                wrapActionListener().onAction(ADD_FOOD_ITEM)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        val ADD_FOOD_ITEM = "addFoodItem"
    }
}
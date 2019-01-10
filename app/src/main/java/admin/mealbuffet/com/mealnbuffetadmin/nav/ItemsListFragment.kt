package admin.mealbuffet.com.mealnbuffetadmin.nav

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.R.id.menu_list_add_item
import admin.mealbuffet.com.mealnbuffetadmin.model.FoodItem
import admin.mealbuffet.com.mealnbuffetadmin.network.ResponseCallback
import admin.mealbuffet.com.mealnbuffetadmin.network.publishItems
import admin.mealbuffet.com.mealnbuffetadmin.network.unpublishItems
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

class ItemsListFragment : BaseFragment(), View.OnClickListener, InternalActionListener {
    override fun onAction(action: String, data: Any?) {
        when (action) {
            DELETE_ITEM_FAILED -> showNetworkError()
            DELETE_ITEM_SUCCESSFULLY -> {
                showCustomError(getString(R.string.deleted_item_successfully))
                fetchGetItemsList()
            }
        }
    }

    private fun fetchGetItemsList() {
        val restaurantId = PreferencesHelper.getRestaurantId(requireContext())
        foodItemListViewModel.getFoodItemsData(restaurantId)
    }

    override fun layoutResource(): Int = R.layout.fragment_itemslist
    private lateinit var foodItemListViewModel: FoodItemListViewModel
    private lateinit var foodItemsAdapter: FoodItemsAdapter
    private var foodItemsLst = ArrayList<FoodItem>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFoodItemsListViewModel()
        setHasOptionsMenu(true)
        unpublish_items.setOnClickListener(this)
        publish_items.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.publish_items -> publishSelectedItems()
            R.id.unpublish_items -> unPublishSelectedItems()
        }
    }

    private fun unPublishSelectedItems() {
        val filteredList = foodItemsLst.filter { it.checked == true }
        val arrayList = ArrayList<String>()
        filteredList.forEach {
            it.itemCode?.let { it1 -> arrayList.add(it1) }
        }

        unpublishItems(arrayList, object : ResponseCallback {
            override fun onSuccess(data: Any?) {
                fetchGetItemsList()
            }

            override fun onError(data: Any?) {
                showNetworkError()
            }
        })
    }

    private fun publishSelectedItems() {
        val filteredList = foodItemsLst.filter { it.checked == true }
        val arrayList = ArrayList<String>()
        filteredList.forEach {
            it.itemCode?.let { it1 -> arrayList.add(it1) }
        }

        publishItems(arrayList, object : ResponseCallback {
            override fun onSuccess(data: Any?) {
                fetchGetItemsList()
            }

            override fun onError(data: Any?) {
                showNetworkError()
            }
        })
    }

    private fun initFoodItemsListViewModel() {
        foodItemListViewModel = ViewModelProviders.of(this).get(FoodItemListViewModel::class.java)
        foodItemListViewModel.liveData.observe(this, Observer {
            if (it == null) {
                showNetworkError()
            } else {
                foodItemsLst = it
                renderFoodItemsView()
            }
        })
        fetchGetItemsList()
    }

    private fun renderFoodItemsView() {
        foodItemsAdapter = FoodItemsAdapter(requireContext(), this as InternalActionListener)
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
        const val ADD_FOOD_ITEM: String = "addFoodItem"
        const val DELETE_ITEM_SUCCESSFULLY: String = "DeletedSuccessfully"
        const val DELETE_ITEM_FAILED: String = "DeleteItemFailed"
    }
}
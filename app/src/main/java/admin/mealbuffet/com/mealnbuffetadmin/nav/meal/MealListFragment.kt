package admin.mealbuffet.com.mealnbuffetadmin.nav.meal

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.model.MealItem
import admin.mealbuffet.com.mealnbuffetadmin.nav.InternalActionListener
import admin.mealbuffet.com.mealnbuffetadmin.network.ResponseCallback
import admin.mealbuffet.com.mealnbuffetadmin.network.getMealsList
import admin.mealbuffet.com.mealnbuffetadmin.util.PreferencesHelper
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.mealbuffet.controller.BaseFragment
import kotlinx.android.synthetic.main.fragment_meallist.*

class MealListFragment : BaseFragment(), InternalActionListener {
    override fun onAction(action: String, data: Any?) {
        when (action) {
            DELETED_MEAL_SUCCESSFULLY -> {
                showCustomError(getString(R.string.deleted_item_successfully))
                fetchMealsList()
            }
            DELETED_MEAL_FAILED -> showNetworkError()
            PUBLISHED_MEAL_SUCCESSFULLY -> {
                showCustomError(getString(R.string.publish_item_successfully))
                fetchMealsList()
            }
            PUBLISHED_MEAL_FAILED -> showCustomError(data as String)
            MEAL_EDIT -> wrapActionListener().onAction(MealListFragment.MEAL_EDIT, data)
            SHOW_MEAL_DETAILED -> wrapActionListener().onAction(SHOW_MEAL_DETAILED, data)
        }
    }

    private lateinit var mealItemAdapter: MealItemAdapter
    private var mealItemsList = ArrayList<MealItem>()

    override fun layoutResource(): Int = R.layout.fragment_meallist

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        fetchMealsList()
    }

    private fun fetchMealsList() {
        val restaurantId = PreferencesHelper.getRestaurantId(requireContext())
        getMealsList(restaurantId, object : ResponseCallback {
            override fun onSuccess(data: Any?) {
                if (requireActivity() != null) {
                    mealItemsList = data as ArrayList<MealItem>
                    renderMealItems()
                }
            }

            override fun onError(data: Any?) {
                showNetworkError()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_add_buffet, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_list_add_buffet -> {
                wrapActionListener().onAction(ADD_MEAL)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun renderMealItems() {
        mealItemAdapter = MealItemAdapter(requireContext(), this as InternalActionListener)
        meallist_recyclerView.apply {
            adapter = mealItemAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        meallist_recyclerView.itemAnimator = DefaultItemAnimator()
        mealItemAdapter.setData(mealItemsList)
        mealItemAdapter.notifyDataSetChanged()
    }

    companion object {
        const val DELETED_MEAL_SUCCESSFULLY: String = "DeletedMealItemSuccessfully"
        const val DELETED_MEAL_FAILED: String = "DeletedMealItemFailed"
        const val PUBLISHED_MEAL_SUCCESSFULLY: String = "PublishedMealItemSuccessfully"
        const val MEAL_EDIT: String = "EditMealItem"
        const val PUBLISHED_MEAL_FAILED: String = "PublishedMealItemFailed"
        const val ADD_MEAL: String = "addMeal"
        const val SHOW_MEAL_DETAILED = "ShowMealItemsDetaild"
    }
}
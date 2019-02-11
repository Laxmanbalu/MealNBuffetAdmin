package admin.mealbuffet.com.mealnbuffetadmin.base

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.model.BuffetBasicData
import admin.mealbuffet.com.mealnbuffetadmin.model.BuffetItem
import admin.mealbuffet.com.mealnbuffetadmin.model.EditBuffetData
import admin.mealbuffet.com.mealnbuffetadmin.model.EditMealData
import admin.mealbuffet.com.mealnbuffetadmin.model.FoodItem
import admin.mealbuffet.com.mealnbuffetadmin.model.MealBasicData
import admin.mealbuffet.com.mealnbuffetadmin.model.MealItem
import admin.mealbuffet.com.mealnbuffetadmin.model.MealOrders
import admin.mealbuffet.com.mealnbuffetadmin.nav.AddItemFragment
import admin.mealbuffet.com.mealnbuffetadmin.nav.EditItemFragment
import admin.mealbuffet.com.mealnbuffetadmin.nav.FragmentUpdateRestaurant
import admin.mealbuffet.com.mealnbuffetadmin.nav.ItemsListFragment
import admin.mealbuffet.com.mealnbuffetadmin.nav.ItemsListFragment.Companion.EDIT_FOOD_ITEM
import admin.mealbuffet.com.mealnbuffetadmin.nav.buffet.AddBuffetFragment
import admin.mealbuffet.com.mealnbuffetadmin.nav.buffet.BuffetFoodItemsFragment
import admin.mealbuffet.com.mealnbuffetadmin.nav.buffet.BuffetListFragment
import admin.mealbuffet.com.mealnbuffetadmin.nav.buffet.EditBuffetFoodItemsFragment
import admin.mealbuffet.com.mealnbuffetadmin.nav.buffet.EditBuffetFragment
import admin.mealbuffet.com.mealnbuffetadmin.nav.meal.AddMealFragment
import admin.mealbuffet.com.mealnbuffetadmin.nav.meal.EditMealFoodItemsFragment
import admin.mealbuffet.com.mealnbuffetadmin.nav.meal.EditMealFragment
import admin.mealbuffet.com.mealnbuffetadmin.nav.meal.MealFoodItemsFragment
import admin.mealbuffet.com.mealnbuffetadmin.nav.meal.MealListFragment
import admin.mealbuffet.com.mealnbuffetadmin.nav.orderdashboard.BuffetOrderBoardFragment
import admin.mealbuffet.com.mealnbuffetadmin.nav.orderdashboard.MealOrderBoardFragment
import admin.mealbuffet.com.mealnbuffetadmin.nav.orderdashboard.MealOrderUpdateFragment
import admin.mealbuffet.com.mealnbuffetadmin.nav.orderdashboard.MealOrderUpdateFragment.Companion.MEAL_ORDER_UPDATE_SUCCESS
import android.view.MenuItem
import com.mealbuffet.controller.BaseActivity

abstract class NavigationSupportActivity : BaseActivity() {
    protected lateinit var addItemFragment: AddItemFragment
    protected lateinit var editItemFragment: EditItemFragment
    protected lateinit var addBuffetFragment: AddBuffetFragment
    protected lateinit var editBuffetFragment: EditBuffetFragment

    protected val buffetFoodItemsFragment: BuffetFoodItemsFragment by lazy { BuffetFoodItemsFragment() }
    protected val mealFoodItemsFragment: MealFoodItemsFragment by lazy { MealFoodItemsFragment() }
    protected val editBuffetFoodItemsFragment: EditBuffetFoodItemsFragment by lazy { EditBuffetFoodItemsFragment() }
    protected val itemsListFragment: ItemsListFragment by lazy { ItemsListFragment() }
    protected val buffetsListFragment: BuffetListFragment by lazy { BuffetListFragment() }
    //    protected val editBuffetFragment: EditBuffetFragment by lazy { EditBuffetFragment() }
    protected val mealListFragment: MealListFragment by lazy { MealListFragment() }
    protected val addMealFragment: AddMealFragment by lazy { AddMealFragment() }
    protected val editMealFragment: EditMealFragment by lazy { EditMealFragment() }
    protected val editMealFoodItemsFragment: EditMealFoodItemsFragment by lazy { EditMealFoodItemsFragment() }
    protected val buffetOrderDashboardFragment: BuffetOrderBoardFragment by lazy { BuffetOrderBoardFragment() }
    private val mealOrderBoardFragment: MealOrderBoardFragment by lazy { MealOrderBoardFragment() }
    protected val mealOrderUpdateFragment: MealOrderUpdateFragment by lazy { MealOrderUpdateFragment() }
    protected val fragmentUpdateRestaurant: FragmentUpdateRestaurant by lazy { FragmentUpdateRestaurant() }

    override fun handleNavigationItemSelected(item: MenuItem) {
        when (item.itemId) {
            R.id.leftNavHome -> showHomepageFragment()
            R.id.leftNavBuffetList -> showBuffetItemsFragment()
            R.id.leftNavMealList -> showMealListFragment()
            R.id.leftNavBuffetOrderDashboard -> showBuffetOrderDashBoardFragment()
            R.id.leftNavMealOrderDashboard -> showMealOrderDashBoardFragment()
            R.id.leftNavUpdateDetails -> showRestaurantDetailsFragment()
        }
    }

    private fun showRestaurantDetailsFragment() {
        menuItemId = R.id.leftNavUpdateDetails
        title = getString(R.string.title_update_details)
        setHomeIcon(R.drawable.ic_menu_white)
        showFragment(fragmentUpdateRestaurant)
    }

    private fun showMealOrderDashBoardFragment() {
        menuItemId = R.id.leftNavMealOrderDashboard
        title = getString(R.string.menu_meal_dashboard)
        setHomeIcon(R.drawable.ic_menu_white)
        showFragment(mealOrderBoardFragment)
    }

    protected fun showMealOrderUpdateFragment(mealOrders: MealOrders) {
        title = getString(R.string.menu_meal_dashboard)
        setHomeIcon(R.drawable.ic_arrow_back_white)
        mealOrderUpdateFragment.setMealOrder(mealOrders)
        showFragment(mealOrderUpdateFragment)
    }

    protected fun showMealListFragment() {
        menuItemId = R.id.leftNavMealList
        title = getString(R.string.menu_meal_list)
        setHomeIcon(R.drawable.ic_menu_white)
        showFragment(mealListFragment)
    }

    /*protected fun showItemsListFragment() {
        menuItemId = R.id.leftNavItemsList
        title = getString(R.string.menu_items_list)
        setHomeIcon(R.drawable.ic_menu_white)
        showFragment(itemsListFragment)
    }
*/
    protected fun showBuffetFoodItemsFragment(buffetBasicData: BuffetBasicData) {
        title = getString(R.string.menu_add_buffet)
        setHomeIcon(R.drawable.ic_arrow_back_white)
        buffetFoodItemsFragment.setBasicBuffetData(buffetBasicData)
        showFragment(buffetFoodItemsFragment)
    }

    protected fun showMealFoodItemsFragment(mealBasicData: MealBasicData) {
        title = getString(R.string.menu_add_meal)
        setHomeIcon(R.drawable.ic_arrow_back_white)
        mealFoodItemsFragment.setMealBasicData(mealBasicData)
        showFragment(mealFoodItemsFragment)
    }

    protected fun showBuffetEditFoodItemsFragment(buffetBasicData: EditBuffetData) {
        title = getString(R.string.menu_edit_buffet)
        setHomeIcon(R.drawable.ic_arrow_back_white)
        editBuffetFoodItemsFragment.setBasicBuffetData(buffetBasicData)
        showFragment(editBuffetFoodItemsFragment)
    }

    protected fun showMealEditFoodItemsFragment(mealEditData: EditMealData) {
        title = getString(R.string.menu_edit_buffet)
        setHomeIcon(R.drawable.ic_arrow_back_white)
        editMealFoodItemsFragment.setMealData(mealEditData)
        showFragment(editMealFoodItemsFragment)
    }

    protected fun showEditBuffet(selectedBuffetItem: BuffetItem, create: Boolean = false) {
        title = getString(R.string.menu_edit_buffet)
        setHomeIcon(R.drawable.ic_arrow_back_white)
        if (create) {
            editBuffetFragment = EditBuffetFragment()
        }
        editBuffetFragment.setSelectedBuffetData(selectedBuffetItem)
        showFragment(editBuffetFragment)
    }

    protected fun showEditMeal(selectedMealItem: MealItem) {
        title = getString(R.string.menu_edit_buffet)
        setHomeIcon(R.drawable.ic_arrow_back_white)
        editMealFragment.setSelectedMealItem(selectedMealItem)
        showFragment(editMealFragment)
    }

    protected fun showAddItemFragment() {
        title = getString(R.string.menu_add_item)
        setHomeIcon(R.drawable.ic_arrow_back_white)
        addItemFragment = AddItemFragment()
        showFragment(addItemFragment)
    }

    protected fun showEditItemFragment(foodItem: FoodItem) {
        title = getString(R.string.menu_edit_item)
        setHomeIcon(R.drawable.ic_arrow_back_white)
        editItemFragment = EditItemFragment()
        editItemFragment.setSelectedFoodItem(foodItem)
        showFragment(editItemFragment)
    }

    protected fun showAddMealPage() {
        title = getString(R.string.menu_add_meal)
        setHomeIcon(R.drawable.ic_arrow_back_white)
        showFragment(addMealFragment)
    }

    protected fun showAddBuffet(create: Boolean = false) {
        title = getString(R.string.menu_add_buffet)
        setHomeIcon(R.drawable.ic_arrow_back_white)
        if (create) {
            addBuffetFragment = AddBuffetFragment()
        }
        showFragment(addBuffetFragment)
    }

    protected fun showBuffetItemsFragment() {
        menuItemId = R.id.leftNavBuffetList
        title = getString(R.string.menu_buffets_list)
        setHomeIcon(R.drawable.ic_menu_white)
        showFragment(buffetsListFragment)
    }


    protected fun showBuffetOrderDashBoardFragment() {
        menuItemId = R.id.leftNavBuffetOrderDashboard
        title = getString(R.string.menu_buffet_dashboard)
        setHomeIcon(R.drawable.ic_menu_white)
        showFragment(buffetOrderDashboardFragment)
    }

    abstract fun showHomepageFragment()

    override fun onPerformAction(action: String, data: Any?): Boolean {
        when (action) {
            EditBuffetFragment.EDIT_BUFFET_MOVE_NEXT -> {
                showBuffetEditFoodItemsFragment(data as EditBuffetData)
                true
            }

            MEAL_ORDER_UPDATE_SUCCESS -> {
                showMealOrderDashBoardFragment()
                true
            }

            EDIT_FOOD_ITEM -> {
                showEditItemFragment(data as FoodItem)
            }
        }
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when {
        ::addItemFragment.isInitialized && addItemFragment.isVisible && item.itemId == android.R.id.home -> {
            onBackPressed()
            true
        }
        ::editItemFragment.isInitialized && editItemFragment.isVisible && item.itemId == android.R.id.home -> {
            onBackPressed()
            true
        }
        ::addBuffetFragment.isInitialized && addBuffetFragment.isVisible && item.itemId == android.R.id.home -> {
            onBackPressed()
            true
        }
        ::editBuffetFragment.isInitialized && editBuffetFragment.isVisible && item.itemId == android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        when {
            ::editItemFragment.isInitialized && editItemFragment.isVisible -> showHomepageFragment()
            ::addItemFragment.isInitialized && addItemFragment.isVisible -> showHomepageFragment()
            ::addBuffetFragment.isInitialized && addBuffetFragment.isVisible -> showBuffetItemsFragment()
            ::editBuffetFragment.isInitialized && editBuffetFragment.isVisible -> showBuffetItemsFragment()
            mealOrderUpdateFragment.isVisible -> showMealOrderDashBoardFragment()
            else -> super.onBackPressed()
        }
    }

}
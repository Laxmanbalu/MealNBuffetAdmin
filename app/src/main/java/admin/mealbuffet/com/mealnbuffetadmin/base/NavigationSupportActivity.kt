package admin.mealbuffet.com.mealnbuffetadmin.base

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.model.*
import admin.mealbuffet.com.mealnbuffetadmin.nav.AddItemFragment
import admin.mealbuffet.com.mealnbuffetadmin.nav.EditItemFragment
import admin.mealbuffet.com.mealnbuffetadmin.nav.FragmentUpdateRestaurant
import admin.mealbuffet.com.mealnbuffetadmin.nav.ItemsListFragment
import admin.mealbuffet.com.mealnbuffetadmin.nav.ItemsListFragment.Companion.EDIT_FOOD_ITEM
import admin.mealbuffet.com.mealnbuffetadmin.nav.buffet.*
import admin.mealbuffet.com.mealnbuffetadmin.nav.meal.*
import admin.mealbuffet.com.mealnbuffetadmin.nav.orderdashboard.buffetdashboard.BuffetOrderDashBoardFragment
import admin.mealbuffet.com.mealnbuffetadmin.nav.orderdashboard.mealdashboard.MealOrderDashBoardFragment
import admin.mealbuffet.com.mealnbuffetadmin.nav.orderdashboard.mealdashboard.MealOrderUpdateFragment
import admin.mealbuffet.com.mealnbuffetadmin.nav.orderdashboard.mealdashboard.MealOrderUpdateFragment.Companion.MEAL_ORDER_UPDATE_SUCCESS
import admin.mealbuffet.com.mealnbuffetadmin.report.ReportFragment
import android.view.MenuItem
import com.mealbuffet.controller.BaseActivity

abstract class NavigationSupportActivity : BaseActivity() {
    protected lateinit var addItemFragment: AddItemFragment
    protected lateinit var editItemFragment: EditItemFragment
    protected lateinit var addBuffetFragment: AddBuffetFragment
    protected lateinit var editBuffetFragment: EditBuffetFragment

    protected val buffetDetailedFragment: BuffetDetailedFragment by lazy { BuffetDetailedFragment() }
    protected val mealDetailedFragment: MealDetailedFragment by lazy { MealDetailedFragment() }
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
    protected val buffetOrderDashDashboardFragment: BuffetOrderDashBoardFragment by lazy { BuffetOrderDashBoardFragment() }
    private val mealOrderDashBoardFragment: MealOrderDashBoardFragment by lazy { MealOrderDashBoardFragment() }
    protected val mealOrderUpdateFragment: MealOrderUpdateFragment by lazy { MealOrderUpdateFragment() }
    protected val fragmentUpdateRestaurant: FragmentUpdateRestaurant by lazy { FragmentUpdateRestaurant() }
    protected val reportFragment: ReportFragment by lazy { ReportFragment() }

    override fun handleNavigationItemSelected(item: MenuItem) {
        when (item.itemId) {
            R.id.leftNavHome -> showHomepageFragment()
            R.id.leftNavBuffetList -> showBuffetsListFragment()
            R.id.leftNavMealList -> showMealListFragment()
            R.id.leftNavBuffetOrderDashboard -> showBuffetOrderDashBoardFragment()
            R.id.leftNavMealOrderDashboard -> showMealOrderDashBoardFragment()
            R.id.leftNavUpdateDetails -> showRestaurantDetailsFragment()
            R.id.leftNavReport -> showReportFragment()
            R.id.leftNavSignOut -> showSignOutDialog()
        }
    }

    private fun showReportFragment() {
        menuItemId = R.id.leftNavReport
        title = getString(R.string.menu_report)
        setHomeIcon(R.drawable.ic_menu_white)
        showFragment(reportFragment)
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
        showFragment(mealOrderDashBoardFragment)
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

    protected fun showBuffetsListFragment() {
        menuItemId = R.id.leftNavBuffetList
        title = getString(R.string.menu_buffets_list)
        setHomeIcon(R.drawable.ic_menu_white)
        showFragment(buffetsListFragment)
    }


    protected fun showBuffetOrderDashBoardFragment() {
        menuItemId = R.id.leftNavBuffetOrderDashboard
        title = getString(R.string.menu_buffet_dashboard)
        setHomeIcon(R.drawable.ic_menu_white)
        showFragment(buffetOrderDashDashboardFragment)
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
            ::addBuffetFragment.isInitialized && addBuffetFragment.isVisible -> showBuffetsListFragment()
            ::editBuffetFragment.isInitialized && editBuffetFragment.isVisible -> showBuffetsListFragment()
            mealOrderUpdateFragment.isVisible -> showMealOrderDashBoardFragment()
            buffetDetailedFragment.isVisible -> showBuffetsListFragment()
            mealDetailedFragment.isVisible -> showMealListFragment()
            reportFragment.isVisible -> showHomepageFragment()
            else -> super.onBackPressed()
        }
    }

}
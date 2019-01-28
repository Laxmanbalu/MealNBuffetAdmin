package admin.mealbuffet.com.mealnbuffetadmin.base

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.model.*
import admin.mealbuffet.com.mealnbuffetadmin.nav.AddItemFragment
import admin.mealbuffet.com.mealnbuffetadmin.nav.ItemsListFragment
import admin.mealbuffet.com.mealnbuffetadmin.nav.buffet.*
import admin.mealbuffet.com.mealnbuffetadmin.nav.meal.*
import admin.mealbuffet.com.mealnbuffetadmin.nav.orderdashboard.BuffetOrderBoardFragment
import admin.mealbuffet.com.mealnbuffetadmin.nav.orderdashboard.MealOrderBoardFragment
import android.view.MenuItem
import com.mealbuffet.controller.BaseActivity

abstract class NavigationSupportActivity : BaseActivity() {
    protected lateinit var addItemFragment: AddItemFragment
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
    protected val mealOrderBoardFragment: MealOrderBoardFragment by lazy { MealOrderBoardFragment() }

    override fun handleNavigationItemSelected(item: MenuItem) {
        when (item.itemId) {
            R.id.leftNavHome -> showHomepageFragment()
            R.id.leftNavBuffetList -> showBuffetItemsFragment()
            R.id.leftNavMealList -> showMealListFragment()
            R.id.leftNavBuffetOrderDashboard -> showBuffetOrderDashBoardFragment()
            R.id.leftNavMealOrderDashboard -> showMealOrderDashBoardFragment()
        }
    }

    private fun showMealOrderDashBoardFragment() {
        menuItemId = R.id.leftNavMealOrderDashboard
        title = getString(R.string.menu_meal_dashboard)
        setHomeIcon(R.drawable.ic_menu_white)
        showFragment(mealOrderBoardFragment)
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
        }
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when {
        ::addItemFragment.isInitialized && addItemFragment.isVisible && item.itemId == android.R.id.home -> {
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
            ::addItemFragment.isInitialized && addItemFragment.isVisible -> showHomepageFragment()
            ::addBuffetFragment.isInitialized && addBuffetFragment.isVisible -> showBuffetItemsFragment()
            ::editBuffetFragment.isInitialized && editBuffetFragment.isVisible -> showBuffetItemsFragment()
            else -> super.onBackPressed()
        }
    }

}
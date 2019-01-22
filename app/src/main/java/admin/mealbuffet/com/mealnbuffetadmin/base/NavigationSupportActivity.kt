package admin.mealbuffet.com.mealnbuffetadmin.base

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.model.*
import admin.mealbuffet.com.mealnbuffetadmin.nav.AddItemFragment
import admin.mealbuffet.com.mealnbuffetadmin.nav.ItemsListFragment
import admin.mealbuffet.com.mealnbuffetadmin.nav.buffet.*
import admin.mealbuffet.com.mealnbuffetadmin.nav.meal.*
import android.view.MenuItem
import com.mealbuffet.controller.BaseActivity

abstract class NavigationSupportActivity : BaseActivity() {
    protected val addItemFragment: AddItemFragment by lazy { AddItemFragment() }
    protected val addBuffetFragment: AddBuffetFragment by lazy { AddBuffetFragment() }
    protected val buffetFoodItemsFragment: BuffetFoodItemsFragment by lazy { BuffetFoodItemsFragment() }
    protected val mealFoodItemsFragment: MealFoodItemsFragment by lazy { MealFoodItemsFragment() }
    protected val editBuffetFoodItemsFragment: EditBuffetFoodItemsFragment by lazy { EditBuffetFoodItemsFragment() }
    protected val itemsListFragment: ItemsListFragment by lazy { ItemsListFragment() }
    protected val buffetsListFragment: BuffetListFragment by lazy { BuffetListFragment() }
    protected val editBuffetFragment: EditBuffetFragment by lazy { EditBuffetFragment() }
    protected val mealListFragment: MealListFragment by lazy { MealListFragment() }
    protected val addMealFragment: AddMealFragment by lazy { AddMealFragment() }
    protected val editMealFragment: EditMealFragment by lazy { EditMealFragment() }
    protected val editMealFoodItemsFragment: EditMealFoodItemsFragment by lazy { EditMealFoodItemsFragment() }

    override fun handleNavigationItemSelected(item: MenuItem) {
        when (item.itemId) {
            R.id.leftNavHome -> showHomepageFragment()
            R.id.leftNavBuffetList -> showBuffetItemsFragment()
            R.id.leftNavMealList -> showMealListFragment()
        }
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

    protected fun showEditBuffet(selectedBuffetItem: BuffetItem) {
        title = getString(R.string.menu_edit_buffet)
        setHomeIcon(R.drawable.ic_arrow_back_white)
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
        showFragment(addItemFragment)
    }

    protected fun showAddMealPage() {
        title = getString(R.string.menu_add_meal)
        setHomeIcon(R.drawable.ic_arrow_back_white)
        showFragment(addMealFragment)
    }

    protected fun showAddBuffet() {
        title = getString(R.string.menu_add_buffet)
        setHomeIcon(R.drawable.ic_arrow_back_white)
        showFragment(addBuffetFragment)
    }

    protected fun showBuffetItemsFragment() {
        menuItemId = R.id.leftNavBuffetList
        title = getString(R.string.menu_buffets_list)
        setHomeIcon(R.drawable.ic_menu_white)
        showFragment(buffetsListFragment)
    }

    abstract fun showHomepageFragment()

}
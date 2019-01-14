package admin.mealbuffet.com.mealnbuffetadmin.base

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.nav.AddItemFragment
import admin.mealbuffet.com.mealnbuffetadmin.nav.ItemsListFragment
import admin.mealbuffet.com.mealnbuffetadmin.nav.buffet.AddBuffetFragment
import admin.mealbuffet.com.mealnbuffetadmin.nav.buffet.BuffetFoodItemsFragment
import admin.mealbuffet.com.mealnbuffetadmin.nav.buffet.BuffetListFragment
import android.view.MenuItem
import com.mealbuffet.controller.BaseActivity

abstract class NavigationSupportActivity : BaseActivity() {
    protected val addItemFragment: AddItemFragment by lazy { AddItemFragment() }
    protected val addBuffetFragment: AddBuffetFragment by lazy { AddBuffetFragment() }
    protected val buffetFoodItemsFragment: BuffetFoodItemsFragment by lazy { BuffetFoodItemsFragment() }
    protected val itemsListFragment: ItemsListFragment by lazy { ItemsListFragment() }
    protected val buffetsListFragment: BuffetListFragment by lazy { BuffetListFragment() }

    override fun handleNavigationItemSelected(item: MenuItem) {
        when (item.itemId) {
            R.id.leftNavHome -> showHomepageFragment()
            R.id.leftNavItemsList -> showItemsListFragment()
            R.id.leftNavBuffetList -> showBuffetItemsFragment()
        }
    }

    protected fun showItemsListFragment() {
        menuItemId = R.id.leftNavItemsList
        title = getString(R.string.menu_items_list)
        setHomeIcon(R.drawable.ic_arrow_back_white)
        showFragment(itemsListFragment)
    }

    protected fun showBuffetFoodItemsFragment() {
        menuItemId = R.id.leftNavItemsList
        title = getString(R.string.menu_add_buffet)
        setHomeIcon(R.drawable.ic_arrow_back_white)
        showFragment(buffetFoodItemsFragment)
    }

    protected fun showAddItemFragment() {
        title = getString(R.string.menu_add_item)
        setHomeIcon(R.drawable.ic_arrow_back_white)
        showFragment(addItemFragment)
    }

    protected fun showAddBuffet() {
        title = getString(R.string.menu_add_buffet)
        setHomeIcon(R.drawable.ic_arrow_back_white)
        showFragment(addBuffetFragment)
    }

    protected fun showBuffetItemsFragment() {
        title = getString(R.string.menu_buffets_list)
        setHomeIcon(R.drawable.ic_arrow_back_white)
        showFragment(buffetsListFragment)
    }

    abstract fun showHomepageFragment()

}
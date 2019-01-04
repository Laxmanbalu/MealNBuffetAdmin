package admin.mealbuffet.com.mealnbuffetadmin.base

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.nav.AddItemFragment
import admin.mealbuffet.com.mealnbuffetadmin.nav.ItemsListFragment
import android.view.MenuItem
import com.mealbuffet.controller.BaseActivity

abstract class NavigationSupportActivity : BaseActivity() {
    protected val addItemFragment: AddItemFragment by lazy { AddItemFragment() }
    protected val itemsListFragment: ItemsListFragment by lazy { ItemsListFragment() }

    override fun handleNavigationItemSelected(item: MenuItem) {
        when (item.itemId) {
            R.id.leftNavHome -> showHomepageFragment()
            R.id.leftNavAddItem -> showAddItemFragment()
            R.id.leftNavItemsList -> showItemsListFragment()
        }
    }

    private fun showItemsListFragment() {
        menuItemId = R.id.leftNavItemsList
        title = getString(R.string.menu_items_list)
        setHomeIcon(R.drawable.ic_arrow_back_white)
        showFragment(itemsListFragment)
    }

    private fun showAddItemFragment() {
        menuItemId = R.id.leftNavAddItem
        title = getString(R.string.menu_add_item)
        setHomeIcon(R.drawable.ic_arrow_back_white)
        showFragment(addItemFragment)
    }

    abstract fun showHomepageFragment()

}
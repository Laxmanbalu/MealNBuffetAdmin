package admin.mealbuffet.com.mealnbuffetadmin.base

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.nav.AddItemFragment
import android.view.MenuItem
import com.mealbuffet.controller.BaseActivity

abstract class NavigationSupportActivity : BaseActivity() {
    protected val addItemFragment: AddItemFragment by lazy { AddItemFragment() }

    override fun handleNavigationItemSelected(item: MenuItem) {
        when (item.itemId) {
            R.id.leftNavHome -> showHomepageFragment()
            R.id.leftNavAddItem -> showAddItemFragment()
        }
    }

    private fun showAddItemFragment() {
        menuItemId = R.id.leftNavAddItem
        title = getString(R.string.menu_add_item)
        setHomeIcon(R.drawable.ic_arrow_back_white)
        showFragment(addItemFragment)
    }

    abstract fun showHomepageFragment()

}
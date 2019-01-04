package admin.mealbuffet.com.mealnbuffetadmin.home

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.base.NavigationSupportActivity
import android.os.Bundle
import android.view.MenuItem

class HomeActivity : NavigationSupportActivity() {
    private val homePageFragment = HomeFragment()
    override var menuItemId = R.id.leftNavHome

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showHomepageFragment()
    }

    override fun showHomepageFragment() {
        menuItemId = R.id.leftNavHome
        title = getSpannableTitle()
        setHomeIcon(R.drawable.ic_menu_white)
        showFragment(homePageFragment)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when {
        homePageFragment.isVisible -> super.onOptionsItemSelected(item)
        addItemFragment.isVisible && item.itemId == android.R.id.home -> {
            onBackPressed()
            true
        }
        itemsListFragment.isVisible && item.itemId == android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        when {
            addItemFragment.isVisible -> showHomepageFragment()
            itemsListFragment.isVisible -> showHomepageFragment()
            else -> super.onBackPressed()
        }
    }
}

package admin.mealbuffet.com.mealnbuffetadmin.home

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.base.NavigationSupportActivity
import android.os.Bundle

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
}

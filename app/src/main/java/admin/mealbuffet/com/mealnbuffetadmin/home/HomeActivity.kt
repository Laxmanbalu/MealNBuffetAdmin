package admin.mealbuffet.com.mealnbuffetadmin.home

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.base.NavigationSupportActivity
import admin.mealbuffet.com.mealnbuffetadmin.model.BuffetBasicData
import admin.mealbuffet.com.mealnbuffetadmin.nav.ItemsListFragment
import admin.mealbuffet.com.mealnbuffetadmin.nav.buffet.AddBuffetFragment
import admin.mealbuffet.com.mealnbuffetadmin.nav.buffet.BuffetFoodItemsFragment.Companion.BUFFET_ADDED_SUCCESSFULLY
import admin.mealbuffet.com.mealnbuffetadmin.nav.buffet.BuffetListFragment
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

    override fun onPerformAction(action: String, data: Any?): Boolean {
        when (action) {
            ItemsListFragment.ADD_FOOD_ITEM -> {
                showAddItemFragment()
                true
            }
            BuffetListFragment.ADD_BUFFET -> {
                showAddBuffet()
                true
            }
            AddBuffetFragment.BUFFET_MOVE_NEXT -> {
                showBuffetFoodItemsFragment(data as BuffetBasicData)
                true
            }

            BUFFET_ADDED_SUCCESSFULLY -> {
                showBuffetItemsFragment()
                true
            }
        }
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when {
        homePageFragment.isVisible -> super.onOptionsItemSelected(item)
        addItemFragment.isVisible && item.itemId == android.R.id.home -> {
            onBackPressed()
            true
        }
        addBuffetFragment.isVisible && item.itemId == android.R.id.home -> {
            onBackPressed()
            true
        }
        buffetFoodItemsFragment.isVisible && item.itemId == android.R.id.home -> {
            onBackPressed()
            true
        }
        itemsListFragment.isVisible && item.itemId == android.R.id.home -> {
            onBackPressed()
            true
        }
        buffetsListFragment.isVisible && item.itemId == android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        when {
            addItemFragment.isVisible -> showItemsListFragment()
            itemsListFragment.isVisible -> showHomepageFragment()
            buffetFoodItemsFragment.isVisible -> showAddBuffet()
            buffetsListFragment.isVisible -> showHomepageFragment()
            addBuffetFragment.isVisible -> showBuffetItemsFragment()
            else -> super.onBackPressed()
        }
    }
}

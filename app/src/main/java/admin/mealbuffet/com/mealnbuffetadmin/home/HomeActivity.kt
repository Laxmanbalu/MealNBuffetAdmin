package admin.mealbuffet.com.mealnbuffetadmin.home

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.base.NavigationSupportActivity
import admin.mealbuffet.com.mealnbuffetadmin.model.BuffetBasicData
import admin.mealbuffet.com.mealnbuffetadmin.model.BuffetItem
import admin.mealbuffet.com.mealnbuffetadmin.model.EditBuffetData
import admin.mealbuffet.com.mealnbuffetadmin.nav.ItemsListFragment
import admin.mealbuffet.com.mealnbuffetadmin.nav.buffet.AddBuffetFragment
import admin.mealbuffet.com.mealnbuffetadmin.nav.buffet.BuffetFoodItemsFragment.Companion.BUFFET_ADDED_SUCCESSFULLY
import admin.mealbuffet.com.mealnbuffetadmin.nav.buffet.BuffetListFragment
import admin.mealbuffet.com.mealnbuffetadmin.nav.buffet.EditBuffetFragment
import android.os.Bundle
import android.view.MenuItem

class HomeActivity : NavigationSupportActivity() {
    private val homePageFragment = HomeFragment()
    private lateinit var selectedBuffetItem: BuffetItem
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

            BuffetListFragment.BUFFET_EDIT -> {
                if (data != null) {
                    selectedBuffetItem = data as BuffetItem
                    showEditBuffet(selectedBuffetItem)
                }
                true
            }

            EditBuffetFragment.EDIT_BUFFET_MOVE_NEXT -> {
                showBuffetEditFoodItemsFragment(data as EditBuffetData)
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
        editBuffetFoodItemsFragment.isVisible && item.itemId == android.R.id.home -> {
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
        editBuffetFragment.isVisible && item.itemId == android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        when {
            addItemFragment.isVisible -> showItemsListFragment()
            itemsListFragment.isVisible -> showHomepageFragment()
            buffetsListFragment.isVisible -> showHomepageFragment()
            addBuffetFragment.isVisible -> showBuffetItemsFragment()
            buffetFoodItemsFragment.isVisible -> showAddBuffet()
            editBuffetFragment.isVisible -> showBuffetItemsFragment()
            editBuffetFoodItemsFragment.isVisible -> showEditBuffet(selectedBuffetItem)
            else -> super.onBackPressed()
        }
    }
}

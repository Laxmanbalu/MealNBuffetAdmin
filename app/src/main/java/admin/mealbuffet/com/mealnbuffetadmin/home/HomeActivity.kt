package admin.mealbuffet.com.mealnbuffetadmin.home

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.base.NavigationSupportActivity
import admin.mealbuffet.com.mealnbuffetadmin.model.*
import admin.mealbuffet.com.mealnbuffetadmin.nav.AddItemFragment
import admin.mealbuffet.com.mealnbuffetadmin.nav.ItemsListFragment
import admin.mealbuffet.com.mealnbuffetadmin.nav.buffet.AddBuffetFragment
import admin.mealbuffet.com.mealnbuffetadmin.nav.buffet.BuffetFoodItemsFragment.Companion.BUFFET_ADDED_SUCCESSFULLY
import admin.mealbuffet.com.mealnbuffetadmin.nav.buffet.BuffetListFragment
import admin.mealbuffet.com.mealnbuffetadmin.nav.meal.AddMealFragment.Companion.MEAL_MOVE_NEXT
import admin.mealbuffet.com.mealnbuffetadmin.nav.meal.EditMealFoodItemsFragment
import admin.mealbuffet.com.mealnbuffetadmin.nav.meal.EditMealFragment
import admin.mealbuffet.com.mealnbuffetadmin.nav.meal.MealFoodItemsFragment
import admin.mealbuffet.com.mealnbuffetadmin.nav.meal.MealListFragment
import admin.mealbuffet.com.mealnbuffetadmin.nav.meal.MealListFragment.Companion.SHOW_MEAL_DETAILED
import android.os.Bundle
import android.view.MenuItem

class HomeActivity : NavigationSupportActivity() {
    private lateinit var selectedBuffetItem: BuffetItem
    private lateinit var selectedMealItem: MealItem
    override var menuItemId = R.id.leftNavHome

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showHomepageFragment()
    }

    override fun showHomepageFragment() {
        menuItemId = R.id.leftNavHome
        title = getSpannableTitle()
        setHomeIcon(R.drawable.ic_menu_white)
        showFragment(itemsListFragment)
    }

    override fun onPerformAction(action: String, data: Any?): Boolean {
        when (action) {
            ItemsListFragment.ADD_FOOD_ITEM -> {
                showAddItemFragment()
                true
            }
            AddItemFragment.ADDED_ITEM_SUCCESSFULLY -> {
                showHomepageFragment()
                true
            }
            BuffetListFragment.ADD_BUFFET -> {
                showAddBuffet(true)
                true
            }
            MealListFragment.ADD_MEAL -> {
                showAddMealPage()
                true
            }

            AddBuffetFragment.BUFFET_MOVE_NEXT -> {
                showBuffetFoodItemsFragment(data as BuffetBasicData)
                true
            }

            MEAL_MOVE_NEXT -> {
                showMealFoodItemsFragment(data as MealBasicData)
            }

            BUFFET_ADDED_SUCCESSFULLY -> {
                showBuffetsListFragment()
                true
            }

            MealFoodItemsFragment.MEAL_ADDED_SUCCESSFULLY -> {
                showMealListFragment()
                true
            }

            EditMealFoodItemsFragment.MEAL_UPDATED_SUCCESSFULLY -> {
                showMealListFragment()
                true
            }

            BuffetListFragment.BUFFET_EDIT -> {
                if (data != null) {
                    selectedBuffetItem = data as BuffetItem
                    showEditBuffet(selectedBuffetItem, true)
                }
                true
            }

            MealListFragment.MEAL_EDIT -> {
                if (data != null) {
                    selectedMealItem = data as MealItem
                    showEditMeal(selectedMealItem)
                }
                true
            }


            EditMealFragment.MEAL_EDIT_MOVE_NEXT -> {
                showMealEditFoodItemsFragment(data as EditMealData)
                true
            }


            BuffetListFragment.SHOW_BUFFET_ITEMS -> {
                showBuffetDetailedFragment(data as BuffetItem)
                true
            }
            SHOW_MEAL_DETAILED -> {
                showMealDetailedFragment(data as MealItem)

            }
            else -> {
                super.onPerformAction(action, data)
                true
            }
        }
        return false
    }

    private fun showBuffetDetailedFragment(buffetItem: BuffetItem) {
        title = buffetItem.buffetName
        setHomeIcon(R.drawable.ic_arrow_back_white)
        buffetDetailedFragment.buffetItem = buffetItem
        showFragment(buffetDetailedFragment)

    }

    private fun showMealDetailedFragment(mealItem: MealItem) {
        title = mealItem.mealName
        setHomeIcon(R.drawable.ic_arrow_back_white)
        mealDetailedFragment.mealItem = mealItem
        showFragment(mealDetailedFragment)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean = when {
        itemsListFragment.isVisible -> super.onOptionsItemSelected(item)
        buffetsListFragment.isVisible -> super.onOptionsItemSelected(item)
        buffetOrderDashDashboardFragment.isVisible -> super.onOptionsItemSelected(item)
        addMealFragment.isVisible && item.itemId == android.R.id.home -> {
            onBackPressed()
            true
        }
        buffetFoodItemsFragment.isVisible && item.itemId == android.R.id.home -> {
            onBackPressed()
            true
        }
        mealFoodItemsFragment.isVisible && item.itemId == android.R.id.home -> {
            onBackPressed()
            true
        }
        editBuffetFoodItemsFragment.isVisible && item.itemId == android.R.id.home -> {
            onBackPressed()
            true
        }
        editMealFoodItemsFragment.isVisible && item.itemId == android.R.id.home -> {
            onBackPressed()
            true
        }

        editMealFragment.isVisible && item.itemId == android.R.id.home -> {
            onBackPressed()
            true
        }
        mealOrderUpdateFragment.isVisible && item.itemId == android.R.id.home -> {
            onBackPressed()
            true
        }
        buffetDetailedFragment.isVisible && item.itemId == android.R.id.home -> {
            onBackPressed()
            true
        }

        mealDetailedFragment.isVisible && item.itemId == android.R.id.home -> {
            onBackPressed()
            true
        }

        else -> super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        when {
            buffetsListFragment.isVisible -> showHomepageFragment()
            addMealFragment.isVisible -> showMealListFragment()
            buffetFoodItemsFragment.isVisible -> showAddBuffet()
            mealFoodItemsFragment.isVisible -> showAddMealPage()
            editBuffetFoodItemsFragment.isVisible -> showEditBuffet(selectedBuffetItem)
            editMealFragment.isVisible -> showMealListFragment()
            editMealFoodItemsFragment.isVisible -> showEditMeal(selectedMealItem)
            buffetOrderDashDashboardFragment.isVisible -> showHomepageFragment()
            else -> super.onBackPressed()
        }
    }
}

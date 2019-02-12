package admin.mealbuffet.com.mealnbuffetadmin.nav

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.model.AddItem
import admin.mealbuffet.com.mealnbuffetadmin.nav.orderdashboard.ItemBaseFragment
import admin.mealbuffet.com.mealnbuffetadmin.network.ResponseCallback
import admin.mealbuffet.com.mealnbuffetadmin.network.addItemToServer
import admin.mealbuffet.com.mealnbuffetadmin.util.Constants.EMPTY_STRING
import admin.mealbuffet.com.mealnbuffetadmin.util.PreferencesHelper
import kotlinx.android.synthetic.main.fragment_additem.*

class AddItemFragment : ItemBaseFragment() {
    override fun setDefaultSpinnerItem() {

    }

    override fun sendFoodItemDataToService() {
        if (isValidEntry(et_additem_name, R.string.add_item_name_warning)) return
        if (isValidEntry(et_additem_price, R.string.add_item_price_warning)) return
        if (isValidEntry(et_additem_desc, R.string.add_item_desc_warning)) return

        val itemName = et_additem_name.text.toString()
        val price = et_additem_price.text.toString().toFloat()
        val desc = et_additem_desc.text.toString()
        val categoryId = categoryLst[additem_category_spinner.selectedItemId.toInt()].id
                ?: EMPTY_STRING
        val foodType = additem_foodtype_spinner.selectedItem.toString()
        val addItem = AddItem(itemName, price, foodType, desc, categoryId, filePath)
        val restaurantId = PreferencesHelper.getRestaurantId(requireContext())
        addItemToServer(addItem, restaurantId, object : ResponseCallback {
            override fun onError(data: Any?) {
                showNetworkError()
            }

            override fun onSuccess(data: Any?) {
                showCustomError(R.string.item_added_successfully)
                wrapActionListener().onAction(ADDED_ITEM_SUCCESSFULLY)
            }
        })
    }

    companion object {
        const val ADDED_ITEM_SUCCESSFULLY: String = "AddedItemSuccessfully"
    }
}

package admin.mealbuffet.com.mealnbuffetadmin.network

class MealAdminUrls {
    companion object {
        const val ADD_ITEM = "http://13.250.63.91:8080/admin/addItem"
        const val GET_CATEGORIES = "http://13.250.63.91:8080/mealnbuffet/getCategories"

        const val PARAM_ADD_ITEM = "item"
        const val PARAM_ADD_ITEM_RESTAURANT_ID= "restaurantId"
        const val PARAM_ADD_ITEM_NAME = "item"
        const val PARAM_ADD_ITEM_DESC = "desc"
        const val PARAM_ADD_ITEM_PRICE = "price"
        const val PARAM_ADD_ITEM_TYPE = "type"
        const val PARAM_ADD_ITEM_STATUS= "status"
        const val PARAM_ADD_ITEM_FILE = "file"
        const val PARAM_ADD_ITEM_CATEGORY_ID = "categoryId"

    }
}
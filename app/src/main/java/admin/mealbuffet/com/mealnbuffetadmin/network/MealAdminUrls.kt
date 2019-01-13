
package admin.mealbuffet.com.mealnbuffetadmin.network

class MealAdminUrls {
    companion object {
        const val ADD_ITEM = "http://13.250.63.91:8080/admin/addItem"
        const val DELETE_ITEM = "http://13.250.63.91:8080/admin/deleteItem/%s"
        const val GET_CATEGORIES = "http://13.250.63.91:8080/mealnbuffet/getCategories"
        const val GET_FOOD_ITEMS_LIST = "http://13.250.63.91:8080/admin/getItems/%s"
        const val UNPUBLISH_ITEM = "http://13.250.63.91:8080/admin/unPublishItem"
        const val PUBLISH_ITEM = "http://13.250.63.91:8080/admin/publishItem"
        const val AUTH_USER = "http://13.250.63.91:8080/mealnbuffet/authenticateUser"
        const val GET_USER = "http://13.250.63.91:8080/mealnbuffet/getUserById/%s"
        const val GET_BUFFETS_LIST = "http://13.250.63.91:8080/admin/getAllBuffets/%s"
        const val DELETE_BUFFET_ITEM = "http://13.250.63.91:8080/admin/deleteBuffet/%s"
        const val PUBLISH_BUFFET = "http://13.250.63.91:8080/admin/publishBuffet/%s/%s"
        const val UNPUBLISH_BUFFET = "http://13.250.63.91:8080/admin/unPublishBuffet/%s/%s"

        const val PARAM_ADD_ITEM_RESTAURANT_ID= "restaurantId"
        const val PARAM_ADD_ITEM_NAME = "item"
        const val PARAM_ADD_ITEM_DESC = "desc"
        const val PARAM_ADD_ITEM_PRICE = "price"
        const val PARAM_ADD_ITEM_TYPE = "type"
        const val PARAM_ADD_ITEM_STATUS= "status"
        const val PARAM_ADD_ITEM_FILE = "file"
        const val PARAM_ADD_ITEM_CATEGORY_ID = "categoryId"
        const val PARAM_AUTH_PASSWORD = "password"
        const val PARAM_AUTH_ROLE = "role"
        const val PARAM_AUTH_USERID = "userId"
    }
}
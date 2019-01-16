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
        const val GET_ACTIVE_FOOD_ITEMS_LIST = "http://13.250.63.91:8080/admin/getActiveItems/%s"
        const val ADD_BUFFET = "http://13.250.63.91:8080/admin/addBuffet"
        const val UPDATE_BUFFET = "http://13.250.63.91:8080/admin/updateBuffet"
        const val GET_MEALS_LIST = "http://13.250.63.91:8080/admin/getMealByRestaurantId/%s"

        const val PARAM_RESTAURANT_ID = "restaurantId"
        const val PARAM_BUFFET_ID = "buffetId"
        const val PARAM_ADD_ITEM_NAME = "item"
        const val PARAM_ADD_ITEM_DESC = "desc"
        const val PARAM_ADD_ITEM_PRICE = "price"
        const val PARAM_ADD_ITEM_TYPE = "type"
        const val PARAM_ADD_ITEM_STATUS = "status"
        const val PARAM_ADD_ITEM_FILE = "file"
        const val PARAM_ADD_ITEM_CATEGORY_ID = "categoryId"
        const val PARAM_AUTH_PASSWORD = "password"
        const val PARAM_AUTH_ROLE = "role"
        const val PARAM_AUTH_USERID = "userId"

        const val PARAM_ADULT_PRICE = "adultPrice"
        const val PARAM_ACTIVE_FLAG = "activeFlag"
        const val PARAM_BUFFET_NAME = "buffetName"
        const val PARAM_DISPLAY_NAME = "displayName"
        const val PARAM_END_TIME = "endTime"
        const val PARAM_KIDS_PRICE = "kidsPrice"
        const val PARAM_START_TIME = "startTime"
        const val PARAM_STATUS = "status"
        const val PARAM_TYPE_DESC = "typeDesc"
        const val PARAM_TYPE = "type"
        const val PARAM_BUFFET_ITEMS = "buffetItems"
        const val PARAM_ITEMS_LST = "itemsList"
        const val PARAM_ITEMS = "items"
    }
}
package admin.mealbuffet.com.mealnbuffetadmin.network

import admin.mealbuffet.com.mealnbuffetadmin.MealNBuffetApplication
import admin.mealbuffet.com.mealnbuffetadmin.model.*
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.ADD_BUFFET
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.ADD_ITEM
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.AUTH_USER
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.CREATE_MEAL
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.DELETE_ITEM
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.GET_ACTIVE_FOOD_ITEMS_LIST
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.GET_BUFFETS_LIST
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.GET_CATEGORIES
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.GET_FOOD_ITEMS_LIST
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.GET_MEALS_LIST
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.GET_USER
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.PARAM_ACTIVE_FLAG
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.PARAM_ADD_ITEM_CATEGORY_ID
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.PARAM_ADD_ITEM_DESC
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.PARAM_ADD_ITEM_FILE
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.PARAM_ADD_ITEM_NAME
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.PARAM_ADD_ITEM_PRICE
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.PARAM_ADD_ITEM_STATUS
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.PARAM_ADULT_PRICE
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.PARAM_AUTH_PASSWORD
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.PARAM_AUTH_ROLE
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.PARAM_AUTH_USERID
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.PARAM_BUFFET_CUTOFFTIME
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.PARAM_BUFFET_CUTOFFTIME_FLAG
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.PARAM_BUFFET_ID
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.PARAM_BUFFET_ITEMS
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.PARAM_BUFFET_NAME
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.PARAM_COMP_MSG
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.PARAM_DISPLAY_NAME
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.PARAM_END_TIME
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.PARAM_ITEMS_LST
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.PARAM_KIDS_PRICE
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.PARAM_MAX_ITEMS_QTY
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.PARAM_MEAL_ID
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.PARAM_MEAL_ITEMS
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.PARAM_MEAL_NAME
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.PARAM_RESTAURANT_ID
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.PARAM_START_TIME
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.PARAM_STATUS
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.PARAM_TYPE
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.PARAM_TYPE_DESC
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.PUBLISH_ITEM
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.UNPUBLISH_ITEM
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.UPDATE_BUFFET
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.UPDATE_ITEM
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.UPDATE_MEAL
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.Response.ErrorListener
import com.android.volley.error.AuthFailureError
import com.android.volley.request.JsonArrayRequest
import com.android.volley.request.JsonObjectRequest
import com.android.volley.request.SimpleMultiPartRequest
import com.android.volley.request.StringRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONObject


fun addItemToServer(addItem: AddItem, restaurantId: String, responseCallBack: ResponseCallback) {
    val requestQueue = MealNBuffetApplication.instance?.getVolleyRequestObject()

    val addItemObject = JSONObject()
    addItemObject.put(PARAM_RESTAURANT_ID, restaurantId)
    addItemObject.put(PARAM_ADD_ITEM_NAME, addItem.itemName)
    addItemObject.put(PARAM_ADD_ITEM_DESC, addItem.desc)
    addItemObject.put(PARAM_ADD_ITEM_PRICE, addItem.price.toString())
//    addItemObject.put(PARAM_ADD_ITEM_TYPE, addItem.foodType)
    addItemObject.put(PARAM_ADD_ITEM_STATUS, "Active")
    addItemObject.put(PARAM_ADD_ITEM_CATEGORY_ID, addItem.category)

    val smr = object : SimpleMultiPartRequest(Request.Method.POST, ADD_ITEM,
            Response.Listener<String> { response ->
                responseCallBack.onSuccess()
            }, ErrorListener {
        responseCallBack.onError()
    }) {
        override fun getParams(): MutableMap<String, String> {
            val params = HashMap<String, String>()
            params["Accept"] = "application/json"
            headers["Content-Type"] = "application/json; charset=utf-8"
            return params
        }
    }
    smr.addFile(PARAM_ADD_ITEM_FILE, addItem.imagePath)
    smr.addMultipartParam("item", "application/json", addItemObject.toString())
    smr.isFixedStreamingMode = true
    requestQueue?.add(smr)
}

fun updateItem(updateFoodItem: updateFoodItem, restaurantId: String, responseCallBack: ResponseCallback) {
    val requestQueue = MealNBuffetApplication.instance?.getVolleyRequestObject()

    val addItemObject = JSONObject()
    addItemObject.put(PARAM_RESTAURANT_ID, restaurantId)
    addItemObject.put(PARAM_ADD_ITEM_NAME, updateFoodItem.itemName)
    addItemObject.put(PARAM_ADD_ITEM_DESC, updateFoodItem.desc)
    addItemObject.put(PARAM_ADD_ITEM_PRICE, updateFoodItem.price.toString())
    addItemObject.put(PARAM_ADD_ITEM_STATUS, "Active")
    addItemObject.put(PARAM_ADD_ITEM_CATEGORY_ID, updateFoodItem.category)
    addItemObject.put("itemCode", updateFoodItem.itemCode)

    val smr = object : SimpleMultiPartRequest(Request.Method.POST, UPDATE_ITEM,
            Response.Listener<String> { _ ->
                responseCallBack.onSuccess()
            }, ErrorListener {
        val networkResponse = it.networkResponse
        responseCallBack.onError()
    }) {
        override fun getParams(): MutableMap<String, String> {
            val params = HashMap<String, String>()
            params["Accept"] = "application/json"
            headers["Content-Type"] = "application/json; charset=utf-8"
            return params
        }
    }
    if (updateFoodItem.imagePath.isNotEmpty()) {
        smr.addFile(PARAM_ADD_ITEM_FILE, updateFoodItem.imagePath)
    }
    smr.addMultipartParam("item", "application/json", addItemObject.toString())
    smr.isFixedStreamingMode = true
    requestQueue?.add(smr)
}

fun updateRestaurantInformation(resDetails: UpdateRestaurantDetails, responseCallBack: ResponseCallback) {
    val requestQueue = MealNBuffetApplication.instance?.getVolleyRequestObject()

    val restaurantDetailsObject = JSONObject()
    restaurantDetailsObject.put(MealAdminUrls.PARAM_ID, resDetails._id)
    restaurantDetailsObject.put(MealAdminUrls.PARAM_RESTAURANT_ID, resDetails.restaurantId)
    restaurantDetailsObject.put(MealAdminUrls.PARAM_CITY, resDetails.city)
    restaurantDetailsObject.put(MealAdminUrls.PARAM_IS_BUFFET_AVAILABLE, resDetails.isBuffetAvailable)
    restaurantDetailsObject.put(MealAdminUrls.PARAM_IS_MEAL_AVAILABLE, resDetails.mealAvailable)
    restaurantDetailsObject.put(MealAdminUrls.PARAM_RESTAURANT_NAME, resDetails.restaurantName)
    restaurantDetailsObject.put(MealAdminUrls.PARAM_STREET, resDetails.street)
    restaurantDetailsObject.put(MealAdminUrls.PARAM_STATE, resDetails.state)
    restaurantDetailsObject.put(MealAdminUrls.PARAM_ZIP_CODE, resDetails.zipCode)
    restaurantDetailsObject.put(MealAdminUrls.PARAM_TAX_ONE, resDetails.tax1)
    restaurantDetailsObject.put(MealAdminUrls.PARAM_TAX_TWO, resDetails.tax2)
    restaurantDetailsObject.put(MealAdminUrls.PARAM_PHONENUMBER, resDetails.phoneNumber)
    restaurantDetailsObject.put(MealAdminUrls.PARAM_TIMEZONE, resDetails.timeZone)

    val foodTypes = JSONArray()
    resDetails.foodType.forEach {
        foodTypes.put(it)
    }
    restaurantDetailsObject.put(MealAdminUrls.PARAM_TYPE, foodTypes)


    val smr = object : SimpleMultiPartRequest(Request.Method.POST, MealAdminUrls.RESTAURANT_UPDATE_DETAILS,
            Response.Listener<String> { _ ->
                responseCallBack.onSuccess()
            }, ErrorListener {
        val networkResponse = it.networkResponse
        if (networkResponse?.data != null) {
            val jsonError = String(networkResponse.data)
            val listType = object : TypeToken<StandardResponse>() {}.type
            val response = Gson().fromJson<StandardResponse>(jsonError, listType)
            responseCallBack.onError(response.shortDescription)
        } else {
            responseCallBack.onError("Something Went wrong try again")
        }

    }) {
        override fun getParams(): MutableMap<String, String> {
            val params = HashMap<String, String>()
            params["Accept"] = "application/json"
            headers["Content-Type"] = "application/json; charset=utf-8"
            return params
        }
    }
    if (resDetails.icon.isNotEmpty()) {
        smr.addFile(PARAM_ADD_ITEM_FILE, resDetails.icon)
    }
    smr.addMultipartParam("restaurant", "application/json", restaurantDetailsObject.toString())
    smr.isFixedStreamingMode = true
    requestQueue?.add(smr)
}

fun getCategoriesList(responseCallBack: ResponseCallback) {
    val requestQueue = MealNBuffetApplication.instance?.getVolleyRequestObject()
    val arrayRequest = JsonArrayRequest(Request.Method.GET,
            GET_CATEGORIES, null, Response.Listener<JSONArray> {
        val listType = object : TypeToken<List<Category>>() {}.type
        val categoriesLst = Gson().fromJson<List<Category>>(it.toString(), listType)
        responseCallBack.onSuccess(categoriesLst)
    }, ErrorListener {
        responseCallBack.onError()
    })
    requestQueue?.add(arrayRequest)
}

fun getFoodItemsList(restaurantId: String, responseCallBack: ResponseCallback) {
    val requestUrl = String.format(GET_FOOD_ITEMS_LIST, restaurantId)
    val requestQueue = MealNBuffetApplication.instance?.getVolleyRequestObject()
    val arrayRequest = JsonArrayRequest(Request.Method.GET,
            requestUrl, null, Response.Listener<JSONArray> {
        val listType = object : TypeToken<List<FoodItem>>() {}.type
        val categoriesLst = Gson().fromJson<List<FoodItem>>(it.toString(), listType)
        responseCallBack.onSuccess(categoriesLst)
    }, ErrorListener {
        responseCallBack.onError()
    })
    arrayRequest.setShouldCache(false)
    requestQueue?.add(arrayRequest)
}


fun getBuffetsList(restaurantId: String, responseCallBack: ResponseCallback) {
    val requestUrl = String.format(GET_BUFFETS_LIST, restaurantId)
    val requestQueue = MealNBuffetApplication.instance?.getVolleyRequestObject()
    val arrayRequest = JsonArrayRequest(Request.Method.GET,
            requestUrl, null, Response.Listener<JSONArray> {
        val listType = object : TypeToken<List<BuffetItem>>() {}.type
        val buffetItemsList = Gson().fromJson<List<BuffetItem>>(it.toString(), listType)
        responseCallBack.onSuccess(buffetItemsList)
    }, ErrorListener {
        responseCallBack.onError()
    })
    arrayRequest.setShouldCache(false)
    requestQueue?.add(arrayRequest)
}

fun deleteFoodItems(itemId: String, responseCallBack: ResponseCallback) {
    val requestQueue = MealNBuffetApplication.instance?.getVolleyRequestObject()
    val requestUrl = String.format(DELETE_ITEM, itemId)
    val deleteObjectRequest = JsonObjectRequest(Request.Method.DELETE,
            requestUrl, null, Response.Listener<JSONObject> {
        val listType = object : TypeToken<StandardResponse>() {}.type
        val buffetRawData = Gson().fromJson<StandardResponse>(it.toString(), listType)
        responseCallBack.onSuccess(buffetRawData)
    }, ErrorListener {
        responseCallBack.onError(it)
    })
    requestQueue?.add(deleteObjectRequest)
}

fun unpublishItems(array: ArrayList<String>, responseCallBack: ResponseCallback) {
    val requestQueue = MealNBuffetApplication.instance?.getVolleyRequestObject()
    val listOfIds = JSONArray(array)

    val stringRequest = object : JsonObjectRequest(Request.Method.POST,
            UNPUBLISH_ITEM, null, Response.Listener<JSONObject> {
        val listType = object : TypeToken<StandardResponse>() {}.type
        val response = Gson().fromJson<StandardResponse>(it.toString(), listType)
        responseCallBack.onSuccess(response.shortDescription)
    }, ErrorListener {
        responseCallBack.onError(it)
    }) {
        override fun getBodyContentType(): String {
            return "application/json"
        }

        @Throws(AuthFailureError::class)
        override fun getBody(): ByteArray {
            return listOfIds.toString().toByteArray()
        }
    }
    requestQueue?.add(stringRequest)
}


fun publishItems(array: ArrayList<String>, responseCallBack: ResponseCallback) {
    val requestQueue = MealNBuffetApplication.instance?.getVolleyRequestObject()
    val listOfIds = JSONArray(array)

    val stringRequest = object : JsonObjectRequest(Request.Method.POST,
            PUBLISH_ITEM, null, Response.Listener<JSONObject> {
        val listType = object : TypeToken<StandardResponse>() {}.type
        val response = Gson().fromJson<StandardResponse>(it.toString(), listType)
        responseCallBack.onSuccess(response.shortDescription)
    }, ErrorListener {
        responseCallBack.onError(it)
    }) {
        override fun getBodyContentType(): String {
            return "application/json"
        }

        @Throws(AuthFailureError::class)
        override fun getBody(): ByteArray {
            return listOfIds.toString().toByteArray()
        }
    }
    requestQueue?.add(stringRequest)
}

fun authenticateUser(user: User, responseCallBack: ResponseCallback) {
    val requestQueue = MealNBuffetApplication.instance?.getVolleyRequestObject()
    val requestUrl = AUTH_USER
    val jsonBody = JSONObject()
    try {
        jsonBody.put(PARAM_AUTH_USERID, user.userId)
        jsonBody.put(PARAM_AUTH_PASSWORD, user.password)
        jsonBody.put(PARAM_AUTH_ROLE, user.role)
    } catch (e: org.json.JSONException) {
    }

    val stringRequest = object : StringRequest(Request.Method.POST,
            requestUrl, Response.Listener<String> {
        responseCallBack.onSuccess(it)
    }, ErrorListener {
        responseCallBack.onError(it)
    }) {
        override fun getBodyContentType(): String {
            return "application/json"
        }

        @Throws(AuthFailureError::class)
        override fun getBody(): ByteArray {
            return jsonBody.toString().toByteArray()
        }
    }
    requestQueue?.add(stringRequest)
}

fun deleteBuffetItem(requestUrl: String, responseCallBack: ResponseCallback) {
    val requestQueue = MealNBuffetApplication.instance?.getVolleyRequestObject()
    val deleteObjectRequest = JsonObjectRequest(Request.Method.DELETE,
            requestUrl, null, Response.Listener<JSONObject> {
        val listType = object : TypeToken<StandardResponse>() {}.type
        val buffetRawData = Gson().fromJson<StandardResponse>(it.toString(), listType)
        responseCallBack.onSuccess(buffetRawData)
    }, ErrorListener {
        responseCallBack.onError(it)
    })
    requestQueue?.add(deleteObjectRequest)
}

fun getUserDetails(userId: String, responseCallBack: ResponseCallback) {
    val requestQueue = MealNBuffetApplication.instance?.getVolleyRequestObject()
    val requestUrl = String.format(GET_USER, userId)
    val objectRequest = JsonObjectRequest(Request.Method.GET,
            requestUrl, null, Response.Listener<JSONObject> {
        val listType = object : TypeToken<User>() {}.type
        val userDetails = Gson().fromJson<User>(it.toString(), listType)
        responseCallBack.onSuccess(userDetails)
    }, ErrorListener {
        responseCallBack.onError(it)
    })
    requestQueue?.add(objectRequest)
}

fun changeSelectedItemPublishTypeService(requestUrl: String, responseCallBack: ResponseCallback) {
    val requestQueue = MealNBuffetApplication.instance?.getVolleyRequestObject()
    val stringRequest = object : JsonObjectRequest(Request.Method.POST,
            requestUrl, null, Response.Listener<JSONObject> {
        val listType = object : TypeToken<StandardResponse>() {}.type
        val response = Gson().fromJson<StandardResponse>(it.toString(), listType)
        responseCallBack.onSuccess(response.shortDescription)
    }, ErrorListener { error ->
        val networkResponse = error.networkResponse
        if (networkResponse?.data != null) {
            val jsonError = String(networkResponse.data)
            val listType = object : TypeToken<StandardResponse>() {}.type
            val response = Gson().fromJson<StandardResponse>(jsonError.toString(), listType)
            responseCallBack.onError(response.shortDescription)
        }
    }) {
        override fun getBodyContentType(): String {
            return "application/json"
        }
    }
    requestQueue?.add(stringRequest)
}

fun getActiveItemsList(restaurantId: String, responseCallBack: ResponseCallback) {
    val requestUrl = String.format(GET_ACTIVE_FOOD_ITEMS_LIST, restaurantId)
    val requestQueue = MealNBuffetApplication.instance?.getVolleyRequestObject()
    val arrayRequest = JsonObjectRequest(Request.Method.GET,
            requestUrl, null, Response.Listener<JSONObject> {
        val listType = object : TypeToken<HashMap<String, List<FoodItem>>>() {}.type
        val activeFoodItems = Gson().fromJson<HashMap<String, Any>>(it.toString(), listType)
        responseCallBack.onSuccess(activeFoodItems)
    }, ErrorListener {
        responseCallBack.onError()
    })
    arrayRequest.setShouldCache(false)
    requestQueue?.add(arrayRequest)
}

fun addNewBuffet(buffetItem: CreateBuffetItem, responseCallBack: ResponseCallback) {
    val requestQueue = MealNBuffetApplication.instance?.getVolleyRequestObject()

    val addBuffet = JSONObject()
    addBuffet.put(PARAM_RESTAURANT_ID, buffetItem.buffetBasicData.restaurantId)
    addBuffet.put(PARAM_ACTIVE_FLAG, true)
    addBuffet.put(PARAM_ADULT_PRICE, buffetItem.buffetBasicData.adultPrice)
    addBuffet.put(PARAM_BUFFET_NAME, buffetItem.buffetBasicData.buffetName)
    addBuffet.put(PARAM_DISPLAY_NAME, buffetItem.buffetBasicData.displayName)
    addBuffet.put(PARAM_END_TIME, buffetItem.buffetBasicData.endTime)
    addBuffet.put(PARAM_KIDS_PRICE, buffetItem.buffetBasicData.kidsPrice)
    addBuffet.put(PARAM_START_TIME, buffetItem.buffetBasicData.startTime)
    addBuffet.put(PARAM_STATUS, 1)
    addBuffet.put(PARAM_BUFFET_CUTOFFTIME, buffetItem.buffetBasicData.buffetCutOffTime)
    addBuffet.put(PARAM_BUFFET_CUTOFFTIME_FLAG, true)
    addBuffet.put(PARAM_TYPE_DESC, buffetItem.buffetBasicData.desc)
    addBuffet.put(PARAM_TYPE, buffetItem.buffetBasicData.type)

    val buffetItemsObject = JSONObject()
    val array = JSONArray()
    buffetItem.itemsList.forEach {
        array.put(it)
    }
    buffetItemsObject.put(PARAM_ITEMS_LST, array)
    addBuffet.put(PARAM_BUFFET_ITEMS, buffetItemsObject)

    val stringRequest = object : JsonObjectRequest(Request.Method.POST,
            ADD_BUFFET, null, Response.Listener<JSONObject> {
        val listType = object : TypeToken<StandardResponse>() {}.type
        val response = Gson().fromJson<StandardResponse>(it.toString(), listType)
        responseCallBack.onSuccess(response.shortDescription)
    }, ErrorListener {
        responseCallBack.onError(it)
    }) {
        override fun getBodyContentType(): String {
            return "application/json"
        }

        @Throws(AuthFailureError::class)
        override fun getBody(): ByteArray {
            return addBuffet.toString().toByteArray()
        }
    }
    requestQueue?.add(stringRequest)
}


fun updateSelectedBuffetItem(buffetItem: CreateBuffetItem, buffetId: String, responseCallBack: ResponseCallback) {
    val requestQueue = MealNBuffetApplication.instance?.getVolleyRequestObject()

    val updateBuffet = JSONObject()
    updateBuffet.put(PARAM_RESTAURANT_ID, buffetItem.buffetBasicData.restaurantId)
    updateBuffet.put(PARAM_BUFFET_ID, buffetId)
    updateBuffet.put(PARAM_ACTIVE_FLAG, true)
    updateBuffet.put(PARAM_ADULT_PRICE, buffetItem.buffetBasicData.adultPrice)
    updateBuffet.put(PARAM_BUFFET_NAME, buffetItem.buffetBasicData.buffetName)
    updateBuffet.put(PARAM_DISPLAY_NAME, buffetItem.buffetBasicData.displayName)
    updateBuffet.put(PARAM_END_TIME, buffetItem.buffetBasicData.endTime)
    updateBuffet.put(PARAM_KIDS_PRICE, buffetItem.buffetBasicData.kidsPrice)
    updateBuffet.put(PARAM_START_TIME, buffetItem.buffetBasicData.startTime)
    updateBuffet.put(PARAM_STATUS, 1)
    updateBuffet.put(PARAM_TYPE_DESC, buffetItem.buffetBasicData.desc)
    updateBuffet.put(PARAM_TYPE, buffetItem.buffetBasicData.type)
    updateBuffet.put(PARAM_BUFFET_CUTOFFTIME, buffetItem.buffetBasicData.buffetCutOffTime)
    updateBuffet.put(PARAM_BUFFET_CUTOFFTIME_FLAG, true)

    val buffetItemsObject = JSONObject()
    val array = JSONArray()
    buffetItem.itemsList.forEach {
        array.put(it)
    }
    buffetItemsObject.put(PARAM_ITEMS_LST, array)
    buffetItemsObject.put(PARAM_BUFFET_ID, buffetId)
    updateBuffet.put(PARAM_BUFFET_ITEMS, buffetItemsObject)


    val buffetUpdateRequest = object : JsonObjectRequest(Request.Method.POST,
            UPDATE_BUFFET, null, Response.Listener<JSONObject> {
        val listType = object : TypeToken<StandardResponse>() {}.type
        val response = Gson().fromJson<StandardResponse>(it.toString(), listType)
        responseCallBack.onSuccess(response.shortDescription)
    }, ErrorListener {
        val networkResponse = it.networkResponse
        if (networkResponse?.data != null) {
            val jsonError = String(networkResponse.data)
            val listType = object : TypeToken<StandardResponse>() {}.type
            val response = Gson().fromJson<StandardResponse>(jsonError, listType)
            responseCallBack.onError(response.shortDescription)
        } else {
            responseCallBack.onError("Something Went wrong try again")
        }
    }) {
        override fun getBodyContentType(): String {
            return "application/json"
        }

        @Throws(AuthFailureError::class)
        override fun getBody(): ByteArray {
            return updateBuffet.toString().toByteArray()
        }
    }
    requestQueue?.add(buffetUpdateRequest)
}


fun getMealsList(restaurantId: String, responseCallBack: ResponseCallback) {
    val requestUrl = String.format(GET_MEALS_LIST, restaurantId)
    val requestQueue = MealNBuffetApplication.instance?.getVolleyRequestObject()
    val arrayRequest = JsonArrayRequest(Request.Method.GET,
            requestUrl, null, Response.Listener<JSONArray> {
        val listType = object : TypeToken<List<MealItem>>() {}.type
        val buffetItemsList = Gson().fromJson<List<MealItem>>(it.toString(), listType)
        responseCallBack.onSuccess(buffetItemsList)
    }, ErrorListener {
        responseCallBack.onError()
    })
    arrayRequest.setShouldCache(false)
    requestQueue?.add(arrayRequest)
}


fun createMealService(mealItem: CreateMealItem, responseCallBack: ResponseCallback) {
    val requestQueue = MealNBuffetApplication.instance?.getVolleyRequestObject()

    val addItemObject = JSONObject()
    addItemObject.put(PARAM_RESTAURANT_ID, mealItem.mealBasicData.restaurantId)
    addItemObject.put(PARAM_ACTIVE_FLAG, true)
    addItemObject.put(PARAM_MEAL_NAME, mealItem.mealBasicData.mealName)
    addItemObject.put(PARAM_STATUS, 1)
    addItemObject.put(PARAM_TYPE_DESC, mealItem.mealBasicData.mealDesc)
    addItemObject.put(PARAM_MAX_ITEMS_QTY, mealItem.mealBasicData.itemQty)
    addItemObject.put(PARAM_COMP_MSG, mealItem.mealBasicData.meaCompMsg)

    val mealItemsObject = JSONObject()
    val array = JSONArray()
    mealItem.itemsList.forEach {
        array.put(it)
    }
    mealItemsObject.put(PARAM_ITEMS_LST, array)
    addItemObject.put(PARAM_MEAL_ITEMS, mealItemsObject)

    val stringRequest = object : JsonObjectRequest(Request.Method.POST,
            CREATE_MEAL, null, Response.Listener<JSONObject> {
        val listType = object : TypeToken<StandardResponse>() {}.type
        val response = Gson().fromJson<StandardResponse>(it.toString(), listType)
        responseCallBack.onSuccess(response.shortDescription)
    }, ErrorListener {
        responseCallBack.onError(it)
    }) {
        override fun getBodyContentType(): String {
            return "application/json"
        }

        @Throws(AuthFailureError::class)
        override fun getBody(): ByteArray {
            return addItemObject.toString().toByteArray()
        }
    }
    requestQueue?.add(stringRequest)
}


fun updateSelectedMeal(mealItem: CreateMealItem, mealId: String, responseCallBack: ResponseCallback) {
    val requestQueue = MealNBuffetApplication.instance?.getVolleyRequestObject()

    val addItemObject = JSONObject()
    addItemObject.put(PARAM_RESTAURANT_ID, mealItem.mealBasicData.restaurantId)
    addItemObject.put(PARAM_ACTIVE_FLAG, true)
    addItemObject.put(PARAM_MEAL_ID, mealId)
    addItemObject.put(PARAM_MEAL_NAME, mealItem.mealBasicData.mealName)
    addItemObject.put(PARAM_STATUS, 1)
    addItemObject.put(PARAM_TYPE_DESC, mealItem.mealBasicData.mealDesc)
    addItemObject.put(PARAM_MAX_ITEMS_QTY, mealItem.mealBasicData.itemQty)
    addItemObject.put(PARAM_COMP_MSG, mealItem.mealBasicData.meaCompMsg)

    val mealItemsObject = JSONObject()
    val array = JSONArray()
    mealItem.itemsList.forEach {
        array.put(it)
    }
    mealItemsObject.put(PARAM_ITEMS_LST, array)
    addItemObject.put(PARAM_MEAL_ITEMS, mealItemsObject)

    val stringRequest = object : JsonObjectRequest(Request.Method.POST,
            UPDATE_MEAL, null, Response.Listener<JSONObject> {
        val listType = object : TypeToken<StandardResponse>() {}.type
        val response = Gson().fromJson<StandardResponse>(it.toString(), listType)
        responseCallBack.onSuccess(response.shortDescription)
    }, ErrorListener {
        responseCallBack.onError(it)
    }) {
        override fun getBodyContentType(): String {
            return "application/json"
        }

        @Throws(AuthFailureError::class)
        override fun getBody(): ByteArray {
            return addItemObject.toString().toByteArray()
        }
    }
    requestQueue?.add(stringRequest)
}



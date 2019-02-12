package admin.mealbuffet.com.mealnbuffetadmin.network

import admin.mealbuffet.com.mealnbuffetadmin.MealNBuffetApplication
import admin.mealbuffet.com.mealnbuffetadmin.model.BuffetOrderRawData
import admin.mealbuffet.com.mealnbuffetadmin.model.MealOrderRawData
import admin.mealbuffet.com.mealnbuffetadmin.model.RestaurantDetails
import admin.mealbuffet.com.mealnbuffetadmin.model.StandardResponse
import admin.mealbuffet.com.mealnbuffetadmin.model.UpdateRestaurantDetails
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.RESTAURANT_GET_DETAILS
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.RESTAURANT_UPDATE_DETAILS
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.error.AuthFailureError
import com.android.volley.request.JsonObjectRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONObject

fun updateBuffetOrderStatus(buffetId: String, status: Int, responseCallBack: ResponseCallback) {
    val requestUrl = String.format(MealAdminUrls.UPDATE_BUFFET_ORDER_STATUS, buffetId, status)
    val requestQueue = MealNBuffetApplication.instance?.getVolleyRequestObject()
    val jsonObjectRequest = JsonObjectRequest(Request.Method.POST,
            requestUrl, null, Response.Listener<JSONObject> {
        val dataType = object : TypeToken<StandardResponse>() {}.type
        val response = Gson().fromJson<StandardResponse>(it.toString(), dataType)
        responseCallBack.onSuccess(response)
    }, Response.ErrorListener {
        responseCallBack.onError()
    })
    requestQueue?.add(jsonObjectRequest)
}

fun getBuffetOrdersHistory(restaurantId: String, responseCallBack: ResponseCallback) {
    val requestUrl = String.format(MealAdminUrls.BUFFET_ORDERS_HISTORY, restaurantId)
    val requestQueue = MealNBuffetApplication.instance?.getVolleyRequestObject()
    val jsonObjectRequest = JsonObjectRequest(Request.Method.GET,
            requestUrl, null, Response.Listener<JSONObject> {
        val dataType = object : TypeToken<BuffetOrderRawData>() {}.type
        val buffetOrderRawData = Gson().fromJson<BuffetOrderRawData>(it.toString(), dataType)
        responseCallBack.onSuccess(buffetOrderRawData)
    }, Response.ErrorListener {
        responseCallBack.onError()
    })
    jsonObjectRequest.setShouldCache(false)
    requestQueue?.add(jsonObjectRequest)
}


fun getMealOrdersHistory(restaurantId: String, responseCallBack: ResponseCallback) {
    val requestUrl = String.format(MealAdminUrls.MEAL_ORDERS_HISTORY, restaurantId)
    val requestQueue = MealNBuffetApplication.instance?.getVolleyRequestObject()
    val jsonObjectRequest = JsonObjectRequest(Request.Method.GET,
            requestUrl, null, Response.Listener<JSONObject> {
        val dataType = object : TypeToken<MealOrderRawData>() {}.type
        val mealOrderRawData = Gson().fromJson<MealOrderRawData>(it.toString(), dataType)
        responseCallBack.onSuccess(mealOrderRawData)
    }, Response.ErrorListener {
        responseCallBack.onError()
    })
    jsonObjectRequest.setShouldCache(false)
    requestQueue?.add(jsonObjectRequest)
}


fun updateMealOrderStatus(mealOrderId: String, status: Int, responseCallBack: ResponseCallback) {
    val requestUrl = String.format(MealAdminUrls.UPDATE_MEALORDER_STATUS, mealOrderId, status)
    val requestQueue = MealNBuffetApplication.instance?.getVolleyRequestObject()
    val jsonObjectRequest = JsonObjectRequest(Request.Method.POST,
            requestUrl, null, Response.Listener<JSONObject> {
        val dataType = object : TypeToken<StandardResponse>() {}.type
        val response = Gson().fromJson<StandardResponse>(it.toString(), dataType)
        responseCallBack.onSuccess(response)
    }, Response.ErrorListener {
        responseCallBack.onError()
    })
    requestQueue?.add(jsonObjectRequest)
}

fun getRestaurantDetails(restaurantId: String, responseCallBack: ResponseCallback) {
    val requestQueue = MealNBuffetApplication.instance?.getVolleyRequestObject()
    val requestUrl = String.format(RESTAURANT_GET_DETAILS, restaurantId)
    val objectRequest = JsonObjectRequest(Request.Method.GET,
            requestUrl, null, Response.Listener<JSONObject> {
        val listType = object : TypeToken<RestaurantDetails>() {}.type
        val restaurantsDetails = Gson().fromJson<RestaurantDetails>(it.toString(), listType)
        responseCallBack.onSuccess(restaurantsDetails)
    }, Response.ErrorListener {
        responseCallBack.onError(it)
    })
    objectRequest.setShouldCache(false)
    requestQueue?.add(objectRequest)
}

fun updateRestaurantInformation(resDetails: UpdateRestaurantDetails, responseCallBack: ResponseCallback) {
    val requestQueue = MealNBuffetApplication.instance?.getVolleyRequestObject()

    val restaurantDetailsObject = JSONObject()
    restaurantDetailsObject.put(MealAdminUrls.PARAM_ID, resDetails._id)
    restaurantDetailsObject.put(MealAdminUrls.PARAM_RESTAURANT_ID, resDetails.restaurantId)
    restaurantDetailsObject.put(MealAdminUrls.PARAM_CITY, resDetails.city)
    restaurantDetailsObject.put(MealAdminUrls.PARAM_IS_BUFFET_AVAILABLE, resDetails.isBuffetAvailable)
    restaurantDetailsObject.put(MealAdminUrls.PARAM_IS_MEAL_AVAILABLE, resDetails.mealAvailable)
    restaurantDetailsObject.put(MealAdminUrls.PARAM_MEAL_AVAILABLE, resDetails.mealAvailable)
    restaurantDetailsObject.put(MealAdminUrls.PARAM_RESTAURANT_NAME, resDetails.restaurantName)
    restaurantDetailsObject.put(MealAdminUrls.PARAM_STREET, resDetails.street)
    restaurantDetailsObject.put(MealAdminUrls.PARAM_STATE, resDetails.state)
    restaurantDetailsObject.put(MealAdminUrls.PARAM_ZIP_CODE, resDetails.zipCode)
    restaurantDetailsObject.put(MealAdminUrls.PARAM_TAX_ONE, resDetails.tax1)
    restaurantDetailsObject.put(MealAdminUrls.PARAM_TAX_TWO, resDetails.tax2)
    restaurantDetailsObject.put(MealAdminUrls.PARAM_TAX_TWO, resDetails.tax2)
    restaurantDetailsObject.put(MealAdminUrls.PARAM_ICON, resDetails.icon)
    restaurantDetailsObject.put(MealAdminUrls.PARAM_PHONENUMBER, resDetails.phoneNumber)
    restaurantDetailsObject.put(MealAdminUrls.PARAM_TIMEZONE, resDetails.timeZone)

    val foodTypes = JSONArray()
    resDetails.foodType.forEach {
        foodTypes.put(it)
    }
    restaurantDetailsObject.put(MealAdminUrls.PARAM_TYPE, foodTypes)


    val objectRequest = object : JsonObjectRequest(Request.Method.POST,
            RESTAURANT_UPDATE_DETAILS, null, Response.Listener<JSONObject> {
        val listType = object : TypeToken<StandardResponse>() {}.type
        val response = Gson().fromJson<StandardResponse>(it.toString(), listType)
        responseCallBack.onSuccess(response.shortDescription)
    }, Response.ErrorListener { error ->
        val networkResponse = error.networkResponse
        if (networkResponse?.data != null) {
            val jsonError = String(networkResponse.data)
            val listType = object : TypeToken<StandardResponse>() {}.type
            val response = Gson().fromJson<StandardResponse>(jsonError, listType)
            if(response != null) {
                responseCallBack.onError(response.shortDescription)
            } else {
                responseCallBack.onError("Something went wrong. Check Network Settings and try again")
            }
        }
    }) {
        override fun getBodyContentType(): String {
            return "application/json"
        }

        @Throws(AuthFailureError::class)
        override fun getBody(): ByteArray {
            return restaurantDetailsObject.toString().toByteArray()
        }
    }
    requestQueue?.add(objectRequest)

}
package admin.mealbuffet.com.mealnbuffetadmin.network

import admin.mealbuffet.com.mealnbuffetadmin.MealNBuffetApplication
import admin.mealbuffet.com.mealnbuffetadmin.model.BuffetOrderRawData
import admin.mealbuffet.com.mealnbuffetadmin.model.MealOrderRawData
import admin.mealbuffet.com.mealnbuffetadmin.model.RestaurantDetails
import admin.mealbuffet.com.mealnbuffetadmin.model.StandardResponse
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.RESTAURANT_GET_DETAILS
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.request.JsonObjectRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
    requestQueue?.add(objectRequest)
}
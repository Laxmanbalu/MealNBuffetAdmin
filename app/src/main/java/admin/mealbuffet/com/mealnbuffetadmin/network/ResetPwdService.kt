package admin.mealbuffet.com.mealnbuffetadmin.network

import admin.mealbuffet.com.mealnbuffetadmin.MealNBuffetApplication
import admin.mealbuffet.com.mealnbuffetadmin.model.StandardResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.request.JsonObjectRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

fun sendOtpToMailId(mailId: String, responseCallBack: ResponseCallback) {
    val requestQueue = MealNBuffetApplication.instance?.getVolleyRequestObject()
    val requestUrl = String.format(MealAdminUrls.SEND_OTP_MAIL, mailId)
    val objectRequest = JsonObjectRequest(Request.Method.POST,
            requestUrl, null, Response.Listener<JSONObject> {
        val listType = object : TypeToken<StandardResponse>() {}.type
        val response = Gson().fromJson<StandardResponse>(it.toString(), listType)
        responseCallBack.onSuccess(response.shortDescription)
    }, Response.ErrorListener {
        val networkResponse = it.networkResponse
        if (networkResponse?.data != null) {
            val jsonError = String(networkResponse.data)
            val listType = object : TypeToken<StandardResponse>() {}.type
            val response = Gson().fromJson<StandardResponse>(jsonError.toString(), listType)
            responseCallBack.onError(response.shortDescription)
        } else {
            responseCallBack.onError("Something Went Wrong Try again ..")
        }
    })
    requestQueue?.add(objectRequest)
}


fun validateOtpRequest(mailId: String, otp: String, responseCallBack: ResponseCallback) {
    val requestQueue = MealNBuffetApplication.instance?.getVolleyRequestObject()
    val requestUrl = String.format(MealAdminUrls.VALIDATE_OTP, mailId, otp)
    val objectRequest = JsonObjectRequest(Request.Method.GET,
            requestUrl, null, Response.Listener<JSONObject> {
        responseCallBack.onSuccess()
    }, Response.ErrorListener {
        val networkResponse = it.networkResponse
        if (networkResponse?.data != null) {
            val jsonError = String(networkResponse.data)
            val listType = object : TypeToken<StandardResponse>() {}.type
            val response = Gson().fromJson<StandardResponse>(jsonError.toString(), listType)
            responseCallBack.onError(response.shortDescription)
        } else {
            responseCallBack.onError("Something Went Wrong Try again ..")
        }
    })
    requestQueue?.add(objectRequest)
}
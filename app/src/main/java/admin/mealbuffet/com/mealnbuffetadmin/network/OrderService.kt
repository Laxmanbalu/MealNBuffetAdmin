package admin.mealbuffet.com.mealnbuffetadmin.network

import admin.mealbuffet.com.mealnbuffetadmin.MealNBuffetApplication
import admin.mealbuffet.com.mealnbuffetadmin.model.StandardResponse
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
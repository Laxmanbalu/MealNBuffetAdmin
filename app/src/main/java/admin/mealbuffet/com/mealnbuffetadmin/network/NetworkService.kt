package admin.mealbuffet.com.mealnbuffetadmin.network

import admin.mealbuffet.com.mealnbuffetadmin.MealNBuffetApplication
import admin.mealbuffet.com.mealnbuffetadmin.model.AddItem
import admin.mealbuffet.com.mealnbuffetadmin.model.Category
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.ADD_ITEM
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.GET_CATEGORIES
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.PARAM_ADD_ITEM_CATEGORY_ID
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.PARAM_ADD_ITEM_DESC
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.PARAM_ADD_ITEM_FILE
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.PARAM_ADD_ITEM_NAME
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.PARAM_ADD_ITEM_PRICE
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.PARAM_ADD_ITEM_RESTAURANT_ID
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.PARAM_ADD_ITEM_STATUS
import admin.mealbuffet.com.mealnbuffetadmin.network.MealAdminUrls.Companion.PARAM_ADD_ITEM_TYPE
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.request.JsonArrayRequest
import com.android.volley.request.SimpleMultiPartRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONObject


fun addItemToServer(addItem: AddItem, restaurantId: String, responseCallBack: ResponseCallback) {
    val requestQueue = MealNBuffetApplication.instance?.getVolleyRequestObject()

    val addItemObject = JSONObject()
    addItemObject.put(PARAM_ADD_ITEM_RESTAURANT_ID, restaurantId)
    addItemObject.put(PARAM_ADD_ITEM_NAME, addItem.itemName)
    addItemObject.put(PARAM_ADD_ITEM_DESC, addItem.desc)
    addItemObject.put(PARAM_ADD_ITEM_PRICE, addItem.price.toString())
    addItemObject.put(PARAM_ADD_ITEM_TYPE, addItem.foodType)
    addItemObject.put(PARAM_ADD_ITEM_STATUS, "Active")
    addItemObject.put(PARAM_ADD_ITEM_CATEGORY_ID, "1811170316321132")

    val smr = object : SimpleMultiPartRequest(Request.Method.POST, ADD_ITEM,
            Response.Listener<String> { response ->
                responseCallBack.onSuccess()
            }, Response.ErrorListener {
        responseCallBack.onSuccess()
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

fun getCategoriesList(responseCallBack: ResponseCallback) {
    val requestQueue = MealNBuffetApplication.instance?.getVolleyRequestObject()
    val arrayRequest = JsonArrayRequest(Request.Method.GET,
            GET_CATEGORIES, null, Response.Listener<JSONArray> {
        val listType = object : TypeToken<List<Category>>() {}.type
        val categoriesLst = Gson().fromJson<List<Category>>(it.toString(), listType)
        responseCallBack.onSuccess(categoriesLst)
    }, Response.ErrorListener {
        responseCallBack.onError()
    })
    requestQueue?.add(arrayRequest)
}
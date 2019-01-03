/*
* Lowe's Companies Inc., Android Application
* Copyright (C)  Lowe's Companies Inc.
*
*  The Lowe's Application is the private property of
*  Lowe's Companies Inc. Any distribution of this software
*   is unlawful and prohibited.
*/
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
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.request.JsonArrayRequest
import com.android.volley.request.SimpleMultiPartRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by Laxman Dodda on 15/12/18.
 *
 * Copyright (C) 2018 Lowe's Companies Inc.
 *
 *  This application is the private property of Lowe's Companies Inc.
 *  Any distribution of this software is unlawful and prohibited.
 */

fun addItemToServer(addItem: AddItem, responseCallBack: ResponseCallback) {
    val requestQueue = MealNBuffetApplication.instance?.getVolleyRequestObject()
    val requestUrl = ADD_ITEM

    val addItemObject = JSONObject()
    try {
        addItemObject.put(PARAM_ADD_ITEM_RESTAURANT_ID, "R002")
        addItemObject.put(PARAM_ADD_ITEM_NAME, addItem.itemName)
        addItemObject.put(PARAM_ADD_ITEM_DESC, addItem.desc)
        addItemObject.put(PARAM_ADD_ITEM_PRICE, addItem.price.toString())
        addItemObject.put(PARAM_ADD_ITEM_TYPE, addItem.foodType)
        addItemObject.put(PARAM_ADD_ITEM_STATUS, "Active")
        addItemObject.put(PARAM_ADD_ITEM_CATEGORY_ID, "1811170316321132")
    } catch (e: org.json.JSONException) {
    }

    val smr = object : SimpleMultiPartRequest(Request.Method.POST, requestUrl,
            Response.Listener<String> { response ->
                Log.d("Response", response)
            }, Response.ErrorListener {
        Log.d("TEST123", "Error" + it.message + it.networkResponse.data.toString() + it.networkResponse.statusCode)
    }) {
        override fun getParams(): MutableMap<String, String> {
            val params = HashMap<String, String>()
            params["Accept"] = "application/json";
            headers["Content-Type"] = "application/json; charset=utf-8";
            return params
        }
    }
    smr.addFile(PARAM_ADD_ITEM_FILE, addItem.imagePath)
    smr.addMultipartParam("item", "application/json", addItemObject.toString())
    smr.isFixedStreamingMode = true;
    requestQueue?.add(smr)
}

fun getCategoriesList(responseCallBack: ResponseCallback) {
    var requestUrl = GET_CATEGORIES
    val requestQueue = MealNBuffetApplication.instance?.getVolleyRequestObject()
    val arrayRequest = JsonArrayRequest(Request.Method.GET,
            requestUrl, null, Response.Listener<JSONArray> {
        val listType = object : TypeToken<List<Category>>() {}.type
        val categoriesLst = Gson().fromJson<List<Category>>(it.toString(), listType)
        responseCallBack.onSuccess(categoriesLst)
    }, Response.ErrorListener {
        responseCallBack.onError()
    })
    requestQueue?.add(arrayRequest)
}
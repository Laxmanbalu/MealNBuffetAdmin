package admin.mealbuffet.com.mealnbuffetadmin.util

import admin.mealbuffet.com.mealnbuffetadmin.util.Constants.EMPTY_STRING
import android.content.Context

object PreferencesHelper {
    private val KEY_RESTAURANT_ID = "restaurantId"
    private val KEY_RESTAURANT_DISPLAYNAME = "resDisplayName"
    private val PREF_FILE_NAME = "com.mealNBuffet.admin.preferences"

    fun storeRestaurantDetails(context: Context, restaurantId: String) {
        val sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(KEY_RESTAURANT_ID, restaurantId)
        editor.apply()
    }

    fun getRestaurantId(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_RESTAURANT_ID, EMPTY_STRING)
    }


    fun storeRestaurantDisplayName(context: Context, displayName: String) {
        val sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(KEY_RESTAURANT_DISPLAYNAME, displayName)
        editor.apply()
    }

    fun getRestaurantDisplayName(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_RESTAURANT_DISPLAYNAME, EMPTY_STRING)
    }
}
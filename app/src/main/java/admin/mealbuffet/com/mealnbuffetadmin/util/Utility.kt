package admin.mealbuffet.com.mealnbuffetadmin.util

import admin.mealbuffet.com.mealnbuffetadmin.util.Constants.EMPTY_STRING
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager


fun hideKeyboard(activity: Activity) {
    val view = activity.findViewById<View>(android.R.id.content)
    if (view != null) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

//    ORDERED  -0 - client
//    ACCEPTED -1 - admin
//    COMPLETED-4 - admin
//    REJECTED-5  - Admin
//    CANCELED-6  - Client
fun getBuffetOrderStatus(status: Int): String = when (status) {
    BuffetOrderStatus.ACCEPTED.status -> "ACCEPTED"
    BuffetOrderStatus.COMPLETED.status -> "COMPLETED"
    BuffetOrderStatus.REJECTED.status -> "REJECTED"
    else -> EMPTY_STRING
}





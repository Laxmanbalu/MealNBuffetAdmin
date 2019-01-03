package admin.mealbuffet.com.mealnbuffetadmin

import admin.mealbuffet.com.mealnbuffetadmin.util.Constants.EMPTY_STRING
import android.app.Application
import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class MealNBuffetApplication : Application() {

    init {
        instance = this
    }

    private var userId: String? = EMPTY_STRING
    private var requestQueue: RequestQueue? = null

    override fun onCreate() {
        super.onCreate()
        val context: Context = MealNBuffetApplication.applicationContext()
        requestQueue = Volley.newRequestQueue(context)
    }

    fun setLoginUserId(argUserId: String) {
        this.userId = argUserId
    }

    fun getUserId(): String? {
        return userId
    }

    fun getVolleyRequestObject(): RequestQueue? {
        return requestQueue
    }

    companion object {
        @get:Synchronized var instance: MealNBuffetApplication? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }
}

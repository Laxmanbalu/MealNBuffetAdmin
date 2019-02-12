package admin.mealbuffet.com.mealnbuffetadmin

import android.app.Application
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class MealNBuffetApplication : Application() {

    private var requestQueue: RequestQueue? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
        requestQueue = Volley.newRequestQueue(applicationContext)
    }

    fun getVolleyRequestObject(): RequestQueue? {
        return requestQueue
    }

    companion object {
        @get:Synchronized
        var instance: MealNBuffetApplication? = null
            private set
    }
}

package admin.mealbuffet.com.mealnbuffetadmin.base

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.home.HomeActivity
import admin.mealbuffet.com.mealnbuffetadmin.util.PreferencesHelper
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Window
import android.view.WindowManager
import kotlinx.android.synthetic.main.splash_screen.*


class SplashScreenActivity : AppCompatActivity() {

    private val mHandler = Handler()

    private val showNextActivity = Runnable {
        var intent: Intent? = null
        val restaurantId = PreferencesHelper.getRestaurantId(this)
        /*if (restaurantId.isEmpty()) {
            intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
        } else {
            intent = Intent(this@SplashScreenActivity, HomeActivity::class.java)

        }*/
        intent = Intent(this@SplashScreenActivity, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.splash_screen)
        updateTitleView()
    }

    private fun updateTitleView() {
        val wordSpan = SpannableString(getString(R.string.app_name))
        wordSpan.setSpan(ForegroundColorSpan(getColor(R.color.orange_app)), 0, getString(R.string.title_meal).length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        tv_splash.text = wordSpan
    }

    override fun onResume() {
        super.onResume()
        mHandler.postDelayed(showNextActivity, 30)
    }

    override fun onStop() {
        super.onStop()
        mHandler.removeCallbacks(showNextActivity)
    }
}
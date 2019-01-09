package admin.mealbuffet.com.mealnbuffetadmin.auth

import admin.mealbuffet.com.mealnbuffetadmin.home.HomeActivity
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import com.mealbuffet.controller.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class LoginActivity : BaseActivity() {

    private val loginFragment by lazy { LoginFragment() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showLoginFragment()
        supportActionBar?.hide()
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    private fun showLoginFragment() {
        showFragment(loginFragment, false)
    }

    override fun onPerformAction(action: String, data: Any?): Boolean {
        when (action) {
            LoginFragment.MOVE_HOME_ACTIVITY -> {
                moveToHomeActivity()
                true
            }
        }
        return false
    }

    private fun moveToHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }
}
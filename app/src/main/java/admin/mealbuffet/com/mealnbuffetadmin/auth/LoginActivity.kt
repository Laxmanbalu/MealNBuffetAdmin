package admin.mealbuffet.com.mealnbuffetadmin.auth

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.auth.LoginFragment.Companion.OTP_VIEW
import admin.mealbuffet.com.mealnbuffetadmin.home.HomeActivity
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.view.MenuItem
import com.mealbuffet.controller.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class LoginActivity : BaseActivity() {

    private val loginFragment by lazy { LoginFragment() }
    private val otpFragment by lazy { OtpFragment() }
    private val resetPasswordFragment by lazy { ResetPasswordFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showLoginFragment()
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (otpFragment.isVisible && item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return false
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
            OTP_VIEW -> {
                showOtpFragment(data as String)
            }
            OtpFragment.RESET_PASSWORD -> {

            }
        }
        return false
    }

    override fun onBackPressed() {
        when {
            otpFragment.isVisible -> showLoginFragment()
            resetPasswordFragment.isVisible -> showLoginFragment()
            else -> super.onBackPressed()
        }
    }

    private fun showOtpFragment(emailId: String) {
        title = getString(R.string.title_otpscreen)
        setHomeIcon(R.drawable.ic_arrow_back_white)
        otpFragment.userEmailId = emailId
        showFragment(otpFragment, false)
    }

    private fun showResetFragment() {
        title = getString(R.string.title_otpscreen)
        setHomeIcon(R.drawable.ic_arrow_back_white)
        showFragment(resetPasswordFragment, false)
    }

    private fun moveToHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }
}
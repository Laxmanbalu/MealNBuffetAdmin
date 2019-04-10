package admin.mealbuffet.com.mealnbuffetadmin.auth

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.custom.InfoDialog
import admin.mealbuffet.com.mealnbuffetadmin.model.User
import admin.mealbuffet.com.mealnbuffetadmin.network.ResponseCallback
import admin.mealbuffet.com.mealnbuffetadmin.network.authenticateUser
import admin.mealbuffet.com.mealnbuffetadmin.network.getUserDetails
import admin.mealbuffet.com.mealnbuffetadmin.network.sendOtpToMailId
import admin.mealbuffet.com.mealnbuffetadmin.util.Constants.EMPTY_STRING
import admin.mealbuffet.com.mealnbuffetadmin.util.PreferencesHelper
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.CompoundButton
import com.mealbuffet.controller.BaseFragment
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseFragment() {

    protected val textwatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            takeCareOfBtnLogin()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    private fun takeCareOfBtnLogin() {
        val userId = et_username.text.toString()
        val password = et_password.text.toString()
        if (userId.isEmpty() || password.isEmpty() || !check_box.isChecked) {
            btn_login.setBackgroundResource(R.drawable.common_background_grey)
            btn_login.isEnabled = false
        } else {
            btn_login.setBackgroundResource(R.drawable.button_round)
            btn_login.isEnabled = true
        }
    }

    override fun layoutResource(): Int = R.layout.fragment_login

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? LoginActivity)?.supportActionBar?.hide()

        btn_login.setOnClickListener {
            proceedLogin()
        }
        et_username.addTextChangedListener(textwatcher)
        et_password.addTextChangedListener(textwatcher)
        check_box.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
            takeCareOfBtnLogin()
        }

        tv_terms.setOnClickListener {
            launchWebBrowser("https://www.google.com")
        }

        tv_policy.setOnClickListener {
            launchWebBrowser("https://www.google.com")
        }

        reset_pwd.setOnClickListener {
            resetPassword()
        }
    }

    private fun resetPassword() {
        PreferencesHelper.storeRestaurantDetails(requireContext(), EMPTY_STRING)
        val userId = et_username.text.toString()
        if (userId.isEmpty()) {
            val dialog = InfoDialog.newInstance(getString(R.string.provide_userid))
            dialog.show(activity?.supportFragmentManager, getString(R.string.provide_userid))
            return
        }
        getUserInformation(userId, true)
    }

    private fun launchWebBrowser(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    private fun proceedLogin() {
        showProgress()
        val user = User(userId = et_username.text.toString(), password = et_password.text.toString(), role = ROLE_ADMIN)
        authenticateUser(user, object : ResponseCallback {
            override fun onSuccess(data: Any?) {
                getUserInformation(user.userId)
            }

            override fun onError(data: Any?) {
                hideProgress()
                showNetworkError()
            }
        })
    }

    private fun getUserInformation(userId: String, isForReset: Boolean = false) {
        getUserDetails(userId, object : ResponseCallback {
            override fun onSuccess(data: Any?) {
                hideProgress()
                val userInfo = data as User
                if (isForReset) {
                    (activity as? LoginActivity)?.userId = userId
                    userInfo.emailId?.let { RequestForOtp(it) }
                } else {
                    userInfo.restaurantId?.let { PreferencesHelper.storeRestaurantDetails(requireContext(), it) }
                    PreferencesHelper.storeRestaurantDisplayName(requireContext(), userInfo.firstName
                            ?: EMPTY_STRING)
                    actionListener?.onAction(MOVE_HOME_ACTIVITY)
                }
            }

            override fun onError(data: Any?) {
                hideProgress()
                showCustomError(getString(R.string.auth_get_user_error))
            }
        })
    }

    private fun RequestForOtp(emailId: String) {
        showProgress()
        sendOtpToMailId(emailId, object : ResponseCallback {
            override fun onSuccess(data: Any?) {
                hideProgress()
                actionListener?.onAction(OTP_VIEW, emailId)
            }

            override fun onError(data: Any?) {
                hideProgress()
                showCustomError(data as String)
            }

        })
    }

    companion object {
        const val ROLE_ADMIN = "RA"
        const val MOVE_HOME_ACTIVITY = "moveHomeActivity"
        const val OTP_VIEW = "OTPView"
    }

}
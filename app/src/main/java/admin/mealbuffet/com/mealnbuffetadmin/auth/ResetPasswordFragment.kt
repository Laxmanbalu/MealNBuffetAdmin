package admin.mealbuffet.com.mealnbuffetadmin.auth

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.custom.InfoDialog
import admin.mealbuffet.com.mealnbuffetadmin.model.User
import admin.mealbuffet.com.mealnbuffetadmin.network.ResponseCallback
import admin.mealbuffet.com.mealnbuffetadmin.network.getUserDetails
import admin.mealbuffet.com.mealnbuffetadmin.network.requestResetPassword
import admin.mealbuffet.com.mealnbuffetadmin.util.Constants.EMPTY_STRING
import android.os.Bundle
import android.view.View
import com.mealbuffet.controller.BaseFragment
import kotlinx.android.synthetic.main.fragment_password_reset.*

class ResetPasswordFragment : BaseFragment() {
    override fun layoutResource(): Int = R.layout.fragment_password_reset
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? LoginActivity)?.supportActionBar?.show()

        btn_reset_confirm.setOnClickListener {
            if (isValidPasswords()) {
                getUserInformation()
            } else {
                showPasswordErrorDialog()
            }
        }
    }

    private fun showPasswordErrorDialog() {
        val dialog = InfoDialog.newInstance(getString(R.string.passwords_notmatching))
        dialog.show(activity?.supportFragmentManager, getString(R.string.passwords_notmatching))
    }

    private fun isValidPasswords(): Boolean {
        return et_confirm_pwd.text.toString().equals(et_new_pwd.text.toString())
    }

    private fun getUserInformation() {
        val userId = (activity as? LoginActivity)?.userId ?: EMPTY_STRING
        getUserDetails(userId, object : ResponseCallback {
            override fun onSuccess(data: Any?) {
                hideProgress()
                val userInfo = data as User
                resetPasswordService(userInfo)
            }

            override fun onError(data: Any?) {
                hideProgress()
                showCustomError(getString(R.string.auth_get_user_error))
            }
        })
    }

    private fun resetPasswordService(userInfo: User) {
        showProgress()
        requestResetPassword(userInfo, et_confirm_pwd.text.toString(), object : ResponseCallback {
            override fun onSuccess(data: Any?) {
                hideProgress()
                showCustomError("Successfully Done Reset...Login Again..")
                actionListener?.onAction(SHOW_LOGIN)
            }

            override fun onError(data: Any?) {
                showNetworkError()
                hideProgress()
            }
        })
    }

    companion object {
        const val SHOW_LOGIN = "ShowLogin"
    }
}
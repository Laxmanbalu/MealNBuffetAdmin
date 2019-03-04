package admin.mealbuffet.com.mealnbuffetadmin.auth

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.model.User
import admin.mealbuffet.com.mealnbuffetadmin.network.ResponseCallback
import admin.mealbuffet.com.mealnbuffetadmin.network.authenticateUser
import admin.mealbuffet.com.mealnbuffetadmin.network.getUserDetails
import admin.mealbuffet.com.mealnbuffetadmin.util.Constants.EMPTY_STRING
import admin.mealbuffet.com.mealnbuffetadmin.util.PreferencesHelper
import android.os.Bundle
import android.view.View
import com.mealbuffet.controller.BaseFragment
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseFragment() {
    override fun layoutResource(): Int = R.layout.fragment_login

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_login.setOnClickListener {
            proceedLogin()
        }
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

    private fun getUserInformation(userId: String) {
        getUserDetails(userId, object : ResponseCallback {
            override fun onSuccess(data: Any?) {
                val userInfo = data as User
                userInfo.restaurantId?.let { PreferencesHelper.storeRestaurantDetails(requireContext(), it) }
                PreferencesHelper.storeRestaurantDisplayName(requireContext(), userInfo.firstName
                        ?: EMPTY_STRING)
                actionListener?.onAction(MOVE_HOME_ACTIVITY)
                hideProgress()
            }

            override fun onError(data: Any?) {
                hideProgress()
                showCustomError(getString(R.string.auth_get_user_error))
            }
        })
    }

    companion object {
        const val ROLE_ADMIN = "RA"
        const val MOVE_HOME_ACTIVITY = "moveHomeActivity"
    }

}
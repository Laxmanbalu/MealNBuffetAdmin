package admin.mealbuffet.com.mealnbuffetadmin.auth

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.network.ResponseCallback
import admin.mealbuffet.com.mealnbuffetadmin.network.validateOtpRequest
import admin.mealbuffet.com.mealnbuffetadmin.util.Constants.EMPTY_STRING
import android.os.Bundle
import android.view.View
import com.mealbuffet.controller.BaseFragment
import kotlinx.android.synthetic.main.fragment_otp.*

class OtpFragment : BaseFragment() {
    var userEmailId: String = EMPTY_STRING

    override fun layoutResource(): Int = R.layout.fragment_otp

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? LoginActivity)?.supportActionBar?.show()

        otp_continue.setOnClickListener {
            validateOtp()
        }
    }

    private fun validateOtp() {
        showProgress()
        validateOtpRequest(userEmailId, otp_view.text.toString(), object : ResponseCallback {
            override fun onSuccess(data: Any?) {
                hideProgress()
                actionListener?.onAction(RESET_PASSWORD)
            }

            override fun onError(data: Any?) {
                hideProgress()
                showCustomError(data as String)
            }
        })
    }

    companion object {
        const val RESET_PASSWORD = "ResetPassword"
    }
}
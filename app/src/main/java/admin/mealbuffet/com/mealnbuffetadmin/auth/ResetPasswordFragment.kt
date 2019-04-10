package admin.mealbuffet.com.mealnbuffetadmin.auth

import admin.mealbuffet.com.mealnbuffetadmin.R
import android.os.Bundle
import android.view.View
import com.mealbuffet.controller.BaseFragment

class ResetPasswordFragment : BaseFragment() {
    override fun layoutResource(): Int = R.layout.fragment_password_reset
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? LoginActivity)?.supportActionBar?.show()

    }
}
package admin.mealbuffet.com.mealnbuffetadmin.report

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.network.ResponseCallback
import admin.mealbuffet.com.mealnbuffetadmin.network.sendReport
import admin.mealbuffet.com.mealnbuffetadmin.util.PreferencesHelper
import android.os.Bundle
import android.view.View
import com.mealbuffet.controller.BaseFragment
import kotlinx.android.synthetic.main.fragment_report.*

class ReportFragment : BaseFragment() {
    override fun layoutResource(): Int = R.layout.fragment_report
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_submit.setOnClickListener {
            report_status_view.visibility = View.GONE
            val day = report_date.dayOfMonth
            val month = report_date.month + 1
            val year = report_date.year
            val selectedDate = "$day-$month-$year"
            submitReport(selectedDate)
        }
    }

    private fun submitReport(selectedDate: String) {
        val restaurantId = PreferencesHelper.getRestaurantId(requireContext())
        showProgress()
        sendReport(selectedDate, restaurantId, object : ResponseCallback {
            override fun onSuccess(data: Any?) {
                val reportSendDescription = data as String
                report_status_view.visibility = View.VISIBLE
                status_msg.text = reportSendDescription
                hideProgress()
            }

            override fun onError(data: Any?) {
                showCustomError(data as String)
                hideProgress()
            }
        })
    }
}
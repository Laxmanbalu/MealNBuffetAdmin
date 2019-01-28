package admin.mealbuffet.com.mealnbuffetadmin.custom

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.util.getBuffetOrderStatusValu
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat.getColor
import android.support.v7.app.AlertDialog
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import kotlinx.android.synthetic.main.order_status_change_dialog.view.*

class OrderStatusChangeDialog : DialogFragment() {


    private var dialogClickListener: DialogClickListener? = null
    fun setDialogActionListener(dialogClickListener: DialogClickListener) {
        this.dialogClickListener = dialogClickListener
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Set dialog title, message, and buttons texts
        val view = View.inflate(this!!.activity, R.layout.order_status_change_dialog, null)
        view.buffet_orderid.text = getSpannableTitle(arguments?.getString(ORDER_ID))
        view.buffet_order_status.text = getSpannableStatus(arguments?.getString(CURR_STATUS))

        // Build the dialog

        return AlertDialog.Builder(context!!)
                .setView(view).setPositiveButton("Proceed") { dialog, which ->
                    val orderStatus: String = view.buffet_order_status_spinner.selectedItem.toString()
                    if (orderStatus.equals(getString(R.string.buffet_order_default))) {
                        dialog.dismiss()
                        return@setPositiveButton
                    }
                    dialogClickListener?.onPositiveBanClick(getBuffetOrderStatusValu(activity?.applicationContext!!, orderStatus))
                }.setNegativeButton("Cancel") { dialog, _ ->
                    //                    dialogClickListener?.onNegativeBtnClick()
                    dialog.dismiss()
                }.create().apply {
                    setCanceledOnTouchOutside(true)
                }
    }

    private fun getSpannableTitle(title: String?): SpannableString {
        val wordSpan = SpannableString(title)
        wordSpan.setSpan(ForegroundColorSpan(getColor(this!!.context!!, R.color.black)), 0, getString(R.string.order_id).length-2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return wordSpan
    }

    private fun getSpannableStatus(status: String?): SpannableString {
        val wordSpan = SpannableString(status)
        wordSpan.setSpan(ForegroundColorSpan(getColor(this!!.context!!, R.color.black)), 0, getString(R.string.present_status).length-2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return wordSpan
    }

    companion object {
        private val TAG = OrderStatusChangeDialog::class.java.simpleName
        private const val ORDER_ID = "order_id"
        private const val CURR_STATUS = "current_status"


        fun newInstance(orderId: String, orderStatus: String): OrderStatusChangeDialog {
            val args = Bundle()
            args.putString(CURR_STATUS, orderStatus)
            args.putString(ORDER_ID, orderId)
            val fragment = OrderStatusChangeDialog()
            fragment.arguments = args

            return fragment
        }
    }
}

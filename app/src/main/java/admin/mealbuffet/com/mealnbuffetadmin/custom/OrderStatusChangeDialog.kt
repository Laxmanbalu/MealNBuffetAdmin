package admin.mealbuffet.com.mealnbuffetadmin.custom

import admin.mealbuffet.com.mealnbuffetadmin.R
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.View
import kotlinx.android.synthetic.main.order_status_change_dialog.view.*

class OrderStatusChangeDialog : DialogFragment() {

    private var listener: DialogInterface.OnClickListener? = null

    private var dialogClickListener: DialogClickListener? = null

    fun setDialogActionListener(dialogClickListener: DialogClickListener) {
        this.dialogClickListener = dialogClickListener
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Set dialog title, message, and buttons texts
        val view = View.inflate(activity, R.layout.order_status_change_dialog, null)
        view.buffet_orderid.text = arguments?.getString(ORDER_ID)
        view.buffet_order_status.text = arguments?.getString(CURR_STATUS)

        // Build the dialog

        return AlertDialog.Builder(context!!)
                .setView(view).setPositiveButton("Proceed") { dialog, which ->
                    listener?.onClick(dialog, which)
                    dialogClickListener?.onPositiveBanClick()
                }.setNegativeButton("Cancel") { dialog, which ->
                    dialogClickListener?.onNegativeBtnClick()
                }.create().apply {
                    setCanceledOnTouchOutside(true)
                }
    }

    override fun onCancel(dialog: DialogInterface) {
        listener?.onClick(dialog, Dialog.BUTTON_NEUTRAL)
        super.onCancel(dialog)
    }

    companion object {
        private val TAG = OrderStatusChangeDialog::class.java.simpleName
        private const val ORDER_ID = "order_id"
        private const val CURR_STATUS = "current_status"


        fun newInstance(orderId: String, orderStatus: String): OrderStatusChangeDialog {
            val args = Bundle()
            args.putString(CURR_STATUS, orderStatus)
            args.putString(ORDER_ID, orderId )
            val fragment = OrderStatusChangeDialog()
            fragment.arguments = args

            return fragment
        }
    }
}

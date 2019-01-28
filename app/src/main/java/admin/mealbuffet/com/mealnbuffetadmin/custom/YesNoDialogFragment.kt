package admin.mealbuffet.com.mealnbuffetadmin.custom

import admin.mealbuffet.com.mealnbuffetadmin.R
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.View
import kotlinx.android.synthetic.main.yes_no_dialog.view.*

class YesNoDialogFragment : DialogFragment() {

    private var listener: DialogInterface.OnClickListener? = null

    private var yesorNoDialogClickListener: DialogClickListener? = null

    fun setDialogActionListener(dialogClickListener: DialogClickListener) {
        this.yesorNoDialogClickListener = dialogClickListener
    }
    
    fun setButtonLabels(yesLabel: String, noLabel: String) {
        arguments?.putString(YES_LBL, yesLabel)
        arguments?.putString(NO_LBL, noLabel)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Set dialog title, message, and buttons texts
        val view = View.inflate(activity, R.layout.yes_no_dialog, null)
        view.title_tv.text = arguments?.getString(TITLE)
        view.message_tv.text = arguments?.getString(BODY)

        // Build the dialog
        var dialog = AlertDialog.Builder(context!!)
                .setView(view).setPositiveButton(arguments?.getString(YES_LBL)) { dialog, which ->
                    listener?.onClick(dialog, which)
                    yesorNoDialogClickListener?.onPositiveBanClick()
                }.setNegativeButton(arguments?.getString(NO_LBL)) { dialog, which ->
                    yesorNoDialogClickListener?.onNegativeBtnClick()
                }.create().apply {
                    setCanceledOnTouchOutside(true)
                }

        return dialog
    }

    override fun onCancel(dialog: DialogInterface) {
        listener?.onClick(dialog, Dialog.BUTTON_NEUTRAL)
        super.onCancel(dialog)
    }

    companion object {
        private val TAG = YesNoDialogFragment::class.java.simpleName
        private const val BODY = "body"
        private const val TITLE = "title"
        private const val YES_LBL = "yesLbl"
        private const val NO_LBL = "noLbl"

        fun newInstance(title: String, msg: String): YesNoDialogFragment {
            val args = Bundle()
            args.putString(TITLE, title)
            args.putString(BODY, msg)
            args.putString(YES_LBL, "Yes")
            args.putString(NO_LBL, "No")

            val fragment = YesNoDialogFragment()
            fragment.arguments = args

            return fragment
        }
    }
}

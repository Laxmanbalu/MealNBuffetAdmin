package admin.mealbuffet.com.mealnbuffetadmin.custom

import admin.mealbuffet.com.mealnbuffetadmin.R
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment

class InfoDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog.Builder(activity)
                .setTitle(getString(R.string.app_name))
                .setMessage(arguments?.getString(BODY))
                .setPositiveButton(R.string.ok, null).create()

        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    companion object {
        private const val BODY = "body"
        fun newInstance(msg: String): InfoDialog {
            val args = Bundle()
            args.putString(BODY, msg)

            val fragment = InfoDialog()
            fragment.arguments = args

            return fragment
        }
    }
}

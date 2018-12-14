
package com.mealbuffet.custom
import android.os.Bundle
import android.support.v4.app.DialogFragment

class ProgressDialogFragment() : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?) = ProgressDialog(requireActivity())
}
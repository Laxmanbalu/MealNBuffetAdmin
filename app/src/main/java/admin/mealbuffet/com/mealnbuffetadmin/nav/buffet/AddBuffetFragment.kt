package admin.mealbuffet.com.mealnbuffetadmin.nav.buffet

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.custom.InfoDialog
import admin.mealbuffet.com.mealnbuffetadmin.model.BuffetBasicData
import admin.mealbuffet.com.mealnbuffetadmin.util.PreferencesHelper
import admin.mealbuffet.com.mealnbuffetadmin.util.hideKeyboard
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import com.mealbuffet.controller.BaseFragment
import kotlinx.android.synthetic.main.fragment_addbuffet.*


class AddBuffetFragment : BaseFragment() {
    override fun layoutResource(): Int = R.layout.fragment_addbuffet

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        et_buffetstartTime.inputType = InputType.TYPE_NULL
        et_buffetstartTime.setOnClickListener {
            showStartTimePickerDialog()
        }

        et_buffetendTime.inputType = InputType.TYPE_NULL
        et_buffetendTime.setOnClickListener {
            hideKeyboard(requireActivity())
            showEndTimePickerDialog()
        }
        buffet_movenext.setOnClickListener {
            val data = makeBuffetBasicObject()
            if (data != null) {
                wrapActionListener().onAction(BUFFET_MOVE_NEXT, data)
            }
        }
    }

    private fun makeBuffetBasicObject(): BuffetBasicData? {
        if (showDataError(et_buffetname) || showDataError((et_buffetdisplayname)) || showDataError(et_buffetdesc) || showDataError((et_buffetstartTime)) ||
                showDataError((et_buffetstartTime)) || showDataError((et_buffetendTime)) || showDataError((et_buffetadult_price)) || showDataError((et_buffetkids_price))) {
            return null
        }

        if (spin_buffettype.selectedItem.toString() == getString(R.string.default_selection_msg)) {
            showFoodSelectionError("Select Buffet Type")
            return null
        }
        val restaurantId = PreferencesHelper.getRestaurantId(requireContext())
        return BuffetBasicData(buffetName = et_buffetname.text.toString(), desc = et_buffetdesc.text.toString(), type = spin_buffettype.selectedItem.toString(),
                startTime = et_buffetstartTime.text.toString(), endTime = et_buffetendTime.text.toString(), adultPrice = et_buffetadult_price.text.toString().toDouble(),
                kidsPrice = et_buffetkids_price.text.toString().toDouble(), displayName = et_buffetdisplayname.text.toString(), restaurantId = restaurantId)
    }

    private fun showFoodSelectionError(message: String) {
        val dialog = InfoDialog.newInstance(message)
        dialog.show(activity?.supportFragmentManager, message)
    }

    private fun showDataError(etData: EditText): Boolean {
        return if (etData.text.isEmpty()) {
            etData.error = getString(R.string.empty_field)
            true
        } else {
            etData.error = null
            false
        }
    }

    private fun showStartTimePickerDialog() {
        val timePickerDialog = TimePickerDialog(requireContext(), android.R.style.Theme_Holo_Light_Dialog,
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute -> et_buffetstartTime.setText(String.format("%02d:%02d", hourOfDay, minute)) }, 0, 0, false)
        timePickerDialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        timePickerDialog.show()
    }

    private fun showEndTimePickerDialog() {
        val timePickerDialog = TimePickerDialog(requireContext(), android.R.style.Theme_Holo_Light_Dialog,
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute -> et_buffetendTime.setText(String.format("%02d:%02d", hourOfDay, minute)) }, 0, 0, false)
        timePickerDialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        timePickerDialog.show()
    }

    companion object {
        const val BUFFET_MOVE_NEXT: String = "buffetMoveNext"
    }
}
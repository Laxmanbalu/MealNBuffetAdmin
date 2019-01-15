package admin.mealbuffet.com.mealnbuffetadmin.nav.buffet

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.model.BuffetBasicData
import admin.mealbuffet.com.mealnbuffetadmin.util.PreferencesHelper
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputType
import android.view.View
import com.mealbuffet.controller.BaseFragment
import kotlinx.android.synthetic.main.fragment_addbuffet.*


class AddBuffetFragment : BaseFragment() {
    override fun layoutResource(): Int = R.layout.fragment_addbuffet

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        et_buffetstartTime.inputType = InputType.TYPE_NULL
        et_buffetstartTime.setOnClickListener { showStartTimePickerDialog() }

        et_buffetendTime.inputType = InputType.TYPE_NULL
        et_buffetendTime.setOnClickListener { showEndTimePickerDialog() }
        buffet_movenext.setOnClickListener {

            wrapActionListener().onAction(BUFFET_MOVE_NEXT, makeBuffetBasicObject())
        }
    }

    private fun makeBuffetBasicObject(): BuffetBasicData {
        val restaurantId = PreferencesHelper.getRestaurantId(requireContext())
        var buffetBasic = BuffetBasicData(buffetName = et_buffetname.text.toString(), desc = et_buffetdesc.text.toString(), type = spin_buffettype.selectedItem.toString(),
                startTime = et_buffetstartTime.text.toString(), endTime = et_buffetendTime.text.toString(), adultPrice = et_buffetadult_price.text.toString().toDouble(),
                kidsPrice = et_buffetkids_price.text.toString().toDouble(), displayName = et_buffetdisplayname.text.toString(), restaurantId = restaurantId)
        return buffetBasic
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
        const val BUFFET_MOVE_NEXT = "buffetMoveNext"
    }
}
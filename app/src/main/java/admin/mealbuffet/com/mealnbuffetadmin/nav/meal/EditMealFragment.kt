package admin.mealbuffet.com.mealnbuffetadmin.nav.meal

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.model.EditMealData
import admin.mealbuffet.com.mealnbuffetadmin.model.MealBasicData
import admin.mealbuffet.com.mealnbuffetadmin.model.MealItem
import admin.mealbuffet.com.mealnbuffetadmin.util.PreferencesHelper
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.mealbuffet.controller.BaseFragment
import kotlinx.android.synthetic.main.fragment_add_meal.*

class EditMealFragment : BaseFragment() {

    private lateinit var selectedMealItem: MealItem
    override fun layoutResource(): Int = R.layout.fragment_add_meal

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillMealDataToView()
        meal_movenext.setOnClickListener {
            val mealObject = createMealObject()
            if (mealObject != null) {
                wrapActionListener().onAction(MEAL_EDIT_MOVE_NEXT, mealObject)
            }
        }
    }

    private fun fillMealDataToView() {
        et_mealname.setText(selectedMealItem.mealName)
        et_mealqty.setText(selectedMealItem.itemsQty.toString())
        et_meal_comp_msg.setText(selectedMealItem.complimentory)
    }

    private fun createMealObject(): EditMealData? {
        if (showDataError(et_mealname) || showDataError((et_mealqty)) /*|| showDataError(et_mealdesc)*/ || showDataError(et_meal_comp_msg)) {
            return null
        }

        val restaurantId = PreferencesHelper.getRestaurantId(requireContext())
        val mealBasicData = MealBasicData(et_mealname.text.toString(), itemQty = et_mealqty.text.toString().toInt(),
                meaCompMsg = et_meal_comp_msg.text.toString(), mealDesc = et_mealdesc.text.toString(), restaurantId = restaurantId)

        return EditMealData(mealBasicData, selectedMealItem)
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

    fun setSelectedMealItem(argMealItem: MealItem) {
        this.selectedMealItem = argMealItem
    }

    companion object {
        const val MEAL_EDIT_MOVE_NEXT: String = "mealEditMoveNext"
    }
}
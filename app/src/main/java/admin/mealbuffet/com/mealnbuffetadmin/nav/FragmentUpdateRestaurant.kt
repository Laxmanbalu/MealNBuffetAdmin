package admin.mealbuffet.com.mealnbuffetadmin.nav

import admin.mealbuffet.com.mealnbuffetadmin.model.RestaurantDetails
import admin.mealbuffet.com.mealnbuffetadmin.model.UpdateRestaurantDetails
import admin.mealbuffet.com.mealnbuffetadmin.network.ResponseCallback
import admin.mealbuffet.com.mealnbuffetadmin.network.getRestaurantDetails
import admin.mealbuffet.com.mealnbuffetadmin.network.updateRestaurantInformation
import admin.mealbuffet.com.mealnbuffetadmin.util.PreferencesHelper
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.mealbuffet.controller.BaseFragment
import kotlinx.android.synthetic.main.fragment_updatedetails.*


class FragmentUpdateRestaurant : BaseFragment() {
    override fun layoutResource(): Int = admin.mealbuffet.com.mealnbuffetadmin.R.layout.fragment_updatedetails

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchCurrentRestaurantDetails()
        btn_update_details.setOnClickListener {
            updateRestaurantDetails()
        }
    }

    private fun updateRestaurantDetails() {
        showProgress()
        val restaurantId = PreferencesHelper.getRestaurantId(requireContext())
        val foodTypes = edit_food_type.text.toString().split(",")
        val updateRestaurantDetails = UpdateRestaurantDetails(isBuffetAvailable = buffet_switchButton.isChecked, mealAvailable = meal_switchButton.isChecked,
                tax1 = edit_tax_one.text.toString(), tax2 = edit_tax_two.text.toString(),
                street = edit_street.text.toString(), city = edit_city.text.toString(), zipCode = edit_zipcode.text.toString().toInt(), state = edit_state.text.toString(),
                phoneNumber = edit_phonenumber.text.toString(), foodType = foodTypes, restaurantName = edit_res_name.text.toString(),
                restaurantId = restaurantId)


        updateRestaurantInformation(updateRestaurantDetails, object : ResponseCallback {
            override fun onSuccess(data: Any?) {
                showCustomError("Updated Successfully")
                hideProgress()
            }

            override fun onError(data: Any?) {
                hideProgress()
                showCustomError(data as String)
            }

        })
    }

    private fun fetchCurrentRestaurantDetails() {
        showProgress()
        val resId = PreferencesHelper.getRestaurantId(requireContext())
        getRestaurantDetails(resId, object : ResponseCallback {
            override fun onSuccess(data: Any?) {
                hideProgress()
                initViewDetails(data as RestaurantDetails)
            }

            override fun onError(data: Any?) {
                hideProgress()
                showNetworkError()
            }
        })
    }

    private fun initViewDetails(restaurantDetails: RestaurantDetails) {
        buffet_switchButton.isChecked = restaurantDetails.isBuffetAvailable
        meal_switchButton.isChecked = restaurantDetails.mealAvailable
        edit_tax_one.setText(restaurantDetails.tax1.toString())
        edit_tax_two.setText(restaurantDetails.tax1.toString())
        edit_street.setText(restaurantDetails.street)
        edit_city.setText(restaurantDetails.city)
        edit_state.setText(restaurantDetails.state)
        edit_zipcode.setText(restaurantDetails.zipCode.toString())
        edit_res_name.setText(restaurantDetails.restaurantName)
        edit_phonenumber.setText(restaurantDetails.phoneNumber)
        val foodType = android.text.TextUtils.join(",", restaurantDetails.type)
        edit_food_type.setText(foodType)
        Glide.with(requireContext()).load(restaurantDetails.icon).into(res_icon)
    }
}
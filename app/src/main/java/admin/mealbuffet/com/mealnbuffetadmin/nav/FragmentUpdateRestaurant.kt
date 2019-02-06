package admin.mealbuffet.com.mealnbuffetadmin.nav

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.model.RestaurantDetails
import admin.mealbuffet.com.mealnbuffetadmin.network.ResponseCallback
import admin.mealbuffet.com.mealnbuffetadmin.network.getRestaurantDetails
import admin.mealbuffet.com.mealnbuffetadmin.util.PreferencesHelper
import android.os.Bundle
import android.view.View
import com.mealbuffet.controller.BaseFragment
import kotlinx.android.synthetic.main.fragment_updatedetails.*

class FragmentUpdateRestaurant : BaseFragment() {
    override fun layoutResource(): Int = R.layout.fragment_updatedetails

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchCurrentRestaurantDetails()
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

    }
}
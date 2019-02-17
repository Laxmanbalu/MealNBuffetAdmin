package admin.mealbuffet.com.mealnbuffetadmin.nav

import admin.mealbuffet.com.mealnbuffetadmin.model.RestaurantDetails
import admin.mealbuffet.com.mealnbuffetadmin.model.UpdateRestaurantDetails
import admin.mealbuffet.com.mealnbuffetadmin.network.ResponseCallback
import admin.mealbuffet.com.mealnbuffetadmin.network.getRestaurantDetails
import admin.mealbuffet.com.mealnbuffetadmin.network.updateRestaurantInformation
import admin.mealbuffet.com.mealnbuffetadmin.util.Constants
import admin.mealbuffet.com.mealnbuffetadmin.util.PreferencesHelper
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.mealbuffet.controller.BaseFragment
import kotlinx.android.synthetic.main.fragment_updatedetails.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*


class FragmentUpdateRestaurant : BaseFragment() {
    private var filePath: String = Constants.EMPTY_STRING

    private lateinit var currentRestaurnt: RestaurantDetails

    override fun layoutResource(): Int = admin.mealbuffet.com.mealnbuffetadmin.R.layout.fragment_updatedetails

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchCurrentRestaurantDetails()
        btn_update_details.setOnClickListener {
            updateRestaurantDetails()
        }
    }

    private fun updateRestaurantDetails() {
        if (!::currentRestaurnt.isInitialized) {
            showNetworkError()
            return
        }
        showProgress()
        val restaurantId = PreferencesHelper.getRestaurantId(requireContext())
        val foodTypes = edit_food_type.text.toString().split(",")
        val updateRestaurantDetails = UpdateRestaurantDetails(isBuffetAvailable = buffet_switchButton.isChecked, mealAvailable = meal_switchButton.isChecked,
                tax1 = edit_tax_one.text.toString().toDouble(), tax2 = edit_tax_two.text.toString().toDouble(),
                street = edit_street.text.toString(), city = edit_city.text.toString(), zipCode = edit_zipcode.text.toString().toInt(), state = edit_state.text.toString(),
                phoneNumber = edit_phonenumber.text.toString(), foodType = foodTypes, restaurantName = edit_res_name.text.toString(),
                restaurantId = restaurantId, _id = currentRestaurnt.id, icon = filePath, timeZone = currentRestaurnt.timeZone)


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
                currentRestaurnt = data as RestaurantDetails
                initViewDetails(currentRestaurnt)
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
        edit_tax_two.setText(restaurantDetails.tax2.toString())
        edit_street.setText(restaurantDetails.street)
        edit_city.setText(restaurantDetails.city)
        edit_state.setText(restaurantDetails.state)
        edit_zipcode.setText(restaurantDetails.zipCode.toString())
        edit_res_name.setText(restaurantDetails.restaurantName)
        edit_phonenumber.setText(restaurantDetails.phoneNumber)
        val foodType = android.text.TextUtils.join(",", restaurantDetails.type)
        edit_food_type.setText(foodType)
        Glide.with(requireContext()).load(restaurantDetails.icon).into(res_icon)

        Glide.with(requireContext())
                .load(restaurantDetails.icon)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        showNetworkError()
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        res_icon.setImageDrawable(resource)
                        filePath = saveImage(resource) ?: Constants.EMPTY_STRING
                        return true
                    }
                })
                .into(res_icon)
    }


    private fun saveImage(drawable: Drawable?): String? {
        val bitmap = (drawable as BitmapDrawable).bitmap
        val wrapper = ContextWrapper(requireContext().applicationContext)
        var file = wrapper.getDir("images", Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")
        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            showNetworkError()
            e.printStackTrace()
        }
        // Return the saved image uri
        return file.absolutePath
    }

}
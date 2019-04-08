package admin.mealbuffet.com.mealnbuffetadmin.nav

import admin.mealbuffet.com.mealnbuffetadmin.custom.ExifUtil.rotateBitmap
import admin.mealbuffet.com.mealnbuffetadmin.model.RestaurantDetails
import admin.mealbuffet.com.mealnbuffetadmin.model.UpdateRestaurantDetails
import admin.mealbuffet.com.mealnbuffetadmin.network.ResponseCallback
import admin.mealbuffet.com.mealnbuffetadmin.network.getRestaurantDetails
import admin.mealbuffet.com.mealnbuffetadmin.network.updateRestaurantInformation
import admin.mealbuffet.com.mealnbuffetadmin.util.Constants
import admin.mealbuffet.com.mealnbuffetadmin.util.PreferencesHelper
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.mealbuffet.controller.BaseFragment
import kotlinx.android.synthetic.main.fragment_updatedetails.*
import java.io.*
import java.util.*


class FragmentUpdateRestaurant : BaseFragment() {

    private val REQUEST_LOADIMAGE_PERMISSION = 1001
    private val REQUEST_TAKEPICTURE_PERMISSION = 1002
    private val RESULT_LOAD_IMAGE = 1
    private val CAMERA_REQUEST = 2
    protected var uploadBitmapImage: Bitmap? = null
    private var filePath: String = Constants.EMPTY_STRING

    private lateinit var currentRestaurnt: RestaurantDetails

    override fun layoutResource(): Int = admin.mealbuffet.com.mealnbuffetadmin.R.layout.fragment_updatedetails

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchCurrentRestaurantDetails()
        btn_update_details.setOnClickListener {
            updateRestaurantDetails()
        }

        take_picture.setOnClickListener {
            takePicture()
        }
        update_item_image.setOnClickListener {
            loadImageFromGallery()
        }
    }

    private fun takePicture() {
        if (ActivityCompat.checkSelfPermission(activity!!,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_TAKEPICTURE_PERMISSION)

        } else {
            launchCameraApp()
        }
    }

    private fun launchCameraApp() {
        val photoCaptureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(photoCaptureIntent, CAMERA_REQUEST)
    }

    private fun loadImageFromGallery() {
        if (ActivityCompat.checkSelfPermission(activity!!,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_LOADIMAGE_PERMISSION)

        } else {
            browsePickFromGallery()
        }
    }

    private fun browsePickFromGallery() {
        val cameraIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(cameraIntent, RESULT_LOAD_IMAGE)
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_LOADIMAGE_PERMISSION || requestCode == REQUEST_TAKEPICTURE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                if (requestCode == REQUEST_TAKEPICTURE_PERMISSION) {
                    launchCameraApp()
                } else {
                    browsePickFromGallery()
                }
            } else {
                showCustomError("To access Gallery App, Need Accept Permissions")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val itemImageView = res_icon
        //super method removed
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RESULT_LOAD_IMAGE) {
                val returnUri = data!!.data
                try {
                    uploadBitmapImage = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, returnUri)
                    itemImageView.setImageBitmap(uploadBitmapImage)
                    itemImageView.visibility = View.VISIBLE
                    val selectedImage = data.data
                    filePath = getPath(selectedImage) ?: Constants.EMPTY_STRING
                } catch (e: Exception) {
                    showCustomError("Loading Image Failed.. try from camera Folder")
                    itemImageView.visibility = View.GONE
                }
            } else if (requestCode == CAMERA_REQUEST) {
                val photo = data?.extras?.get("data") as Bitmap
                val tempUri = getImageUri(requireContext(), photo)
                filePath = getPath(tempUri) ?: Constants.EMPTY_STRING
                val bitmap = rotateBitmap(requireContext(), tempUri, photo)
                itemImageView.setImageBitmap(bitmap)
            }
        } else {
            showCustomError("Loading Image Failed...")
        }
    }


    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    private fun getPath(uri: Uri): String? {
        val projection = arrayOf(MediaStore.MediaColumns.DATA)
        val cursor = context?.contentResolver?.query(uri, projection, null, null, null)
        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        cursor?.moveToFirst()
        val data = cursor?.getString(columnIndex ?: 0)
        cursor?.close()
        return data
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
        val streetDetails = restaurantDetails.street?.replace(Regex(","), "\n")
        edit_street.setText(streetDetails)
        edit_city.setText(restaurantDetails.city)
        edit_state.setText(restaurantDetails.state)
        edit_zipcode.setText(restaurantDetails.zipCode.toString())
        edit_res_name.setText(restaurantDetails.restaurantName)
        edit_phonenumber.setText(restaurantDetails.phoneNumber)
        edit_email.setText(restaurantDetails.email)
        tv_comission_value.text = restaurantDetails.commission.toString() + "%"
        val foodType = android.text.TextUtils.join(",", restaurantDetails.type)
        edit_food_type.setText(foodType)
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
package admin.mealbuffet.com.mealnbuffetadmin.nav.orderdashboard

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.custom.ExifUtil
import admin.mealbuffet.com.mealnbuffetadmin.model.Category
import admin.mealbuffet.com.mealnbuffetadmin.util.Constants
import admin.mealbuffet.com.mealnbuffetadmin.viewmodel.CategoryViewModel
import android.Manifest
import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.text.TextUtils
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import com.mealbuffet.controller.BaseFragment
import kotlinx.android.synthetic.main.fragment_additem.*
import java.io.ByteArrayOutputStream
import java.io.IOException

abstract class ItemBaseFragment : BaseFragment() {
    private val REQUEST_LOADIMAGE_PERMISSION = 1001
    private val REQUEST_TAKEPICTURE_PERMISSION = 1002
    private val RESULT_LOAD_IMAGE = 1
    private val CAMERA_REQUEST = 2
    protected var uploadBitmapImage: Bitmap? = null
    protected var filePath: String = Constants.EMPTY_STRING
    protected lateinit var categoryLst: ArrayList<Category>
    private lateinit var categoryViewModel: CategoryViewModel

    override fun layoutResource(): Int = R.layout.fragment_additem
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCategoryViewModel()
        add_item_image.setOnClickListener { loadImageFromGallery() }
        item_take_picture.setOnClickListener {
            takePicture()
        }
        btn_add_item.setOnClickListener {
            sendFoodItemDataToService()
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

    abstract fun sendFoodItemDataToService()

    private fun loadImageFromGallery() {
        if (ActivityCompat.checkSelfPermission(activity!!,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_LOADIMAGE_PERMISSION)

        } else {
            browsePickFromGallery()
        }
    }

    private fun initCategoryViewModel() {
        categoryViewModel = ViewModelProviders.of(requireActivity()).get(CategoryViewModel::class.java)
        categoryViewModel.liveData.observe(this, Observer { it ->
            if (it == null) {
                showNetworkError()
            } else {
                categoryLst = it
                addSpinnerClickListener()
            }
        })
        categoryViewModel.getCategoriesListData()
    }


    private fun browsePickFromGallery() {
        val cameraIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(cameraIntent, RESULT_LOAD_IMAGE)
    }

    private fun addSpinnerClickListener() {
        additem_category_spinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categoryLst)
        setDefaultSpinnerItem()
    }

    abstract fun setDefaultSpinnerItem()

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

    protected fun isValidEntry(editText: EditText, errorId: Int): Boolean {
        val strData = editText.text.toString().trim()
        if (TextUtils.isEmpty(strData)) {
            editText.error = getString(errorId)
            editText.requestFocus()
            return true
        } else {
            editText.error = null
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val itemImageView = item_image
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
                } catch (e: IOException) {
                    showCustomError("Loading Image Failed.. try from camera Folder")
                    itemImageView.visibility = View.GONE
                }
            } else if (requestCode == CAMERA_REQUEST) {
                val photo = data?.extras?.get("data") as Bitmap
                val tempUri = getImageUri(requireContext(), photo)
                filePath = getPath(tempUri) ?: Constants.EMPTY_STRING
                val bitmap = ExifUtil.rotateBitmap(requireContext(), tempUri, photo)
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

}
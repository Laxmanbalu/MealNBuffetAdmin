package admin.mealbuffet.com.mealnbuffetadmin.nav

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.model.AddItem
import admin.mealbuffet.com.mealnbuffetadmin.model.Category
import admin.mealbuffet.com.mealnbuffetadmin.network.ResponseCallback
import admin.mealbuffet.com.mealnbuffetadmin.network.getCategoriesList
import admin.mealbuffet.com.mealnbuffetadmin.util.Constants.EMPTY_STRING
import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.MediaColumns
import android.support.v4.app.ActivityCompat
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.EditText
import com.mealbuffet.controller.BaseFragment
import kotlinx.android.synthetic.main.fragment_additem.*
import java.io.IOException


class AddItemFragment : BaseFragment() {
    private val REQUEST_STORAGE_PERMISSION = 1001
    private val RESULT_LOAD_IMAGE = 1
    private var uploadBitmapImage: Bitmap? = null
    private var filePath: String = EMPTY_STRING
    private lateinit var categoryLst: ArrayList<Category>

    override fun layoutResource(): Int = R.layout.fragment_additem

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_add_item.setOnClickListener {
            sendAddItemDataToService()
        }
        add_item_image.setOnClickListener { loadImageFromGallery() }
    }

    override fun onResume() {
        super.onResume()
        if (!::categoryLst.isInitialized) {
            fetchCategories()
        }
    }

    private fun fetchCategories() {
        showProgress()
        getCategoriesList(object : ResponseCallback {
            override fun onSuccess(data: Any?) {
                categoryLst = data as ArrayList<Category>
                hideProgress()
                addSpinnerClickListener()
            }

            override fun onError(data: Any?) {
                hideProgress()
                showNetworkError()
            }
        })
    }

    private fun sendAddItemDataToService() {
        if (isValidEntry(et_additem_name, R.string.add_item_name_warning)) return
        if (isValidEntry(et_additem_price, R.string.add_item_price_warning)) return
        if (isValidEntry(et_additem_desc, R.string.add_item_desc_warning)) return

        val itemName = et_additem_name.text.toString()
        val price = et_additem_price.text.toString().toFloat()
        val desc = et_additem_desc.text.toString()
        val categoryId = categoryLst.get(additem_category_spinner.selectedItemId.toInt()).id ?: EMPTY_STRING
        val foodType = additem_foodtype_spinner.selectedItem.toString()
        val addItem = AddItem(itemName, price, foodType, desc, categoryId, filePath)

        /* addItemToServer(addItem, object : ResponseCallback {
             override fun onError(data: Any?) {
                 showNetworkError()
             }

             override fun onSuccess(data: Any?) {
             }
         })*/
    }

    private fun isValidEntry(editText: EditText, errorId: Int): Boolean {
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

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startGallery()
            } else {
                showCustomError("To access Gallery App Permissions are must...")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val cakeImageView = item_image
        //super method removed
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_LOAD_IMAGE) {
                val returnUri = data!!.data
                try {
                    uploadBitmapImage = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, returnUri)
                    cakeImageView.setImageBitmap(uploadBitmapImage)
                    cakeImageView.visibility = View.VISIBLE
                    val selectedImage = data.data

                    filePath = getPath(selectedImage) ?: EMPTY_STRING
                } catch (e: IOException) {
                    showCustomError("Loading Image Failed.. try from camera Folder")
                    cakeImageView.visibility = View.GONE
                }
            }
        } else {
            showCustomError("Loading Image Failed.. try from camera Folder")
        }
    }

    private fun getPath(uri: Uri): String? {
        val projection = arrayOf(MediaColumns.DATA)
        val cursor = context?.contentResolver?.query(uri, projection, null, null, null)
        val columnIndex = cursor?.getColumnIndexOrThrow(MediaColumns.DATA)
        cursor?.moveToFirst()
        val data = cursor?.getString(columnIndex ?: 0)
        cursor?.close()
        return data
    }

    private fun loadImageFromGallery() {
        if (ActivityCompat.checkSelfPermission(activity!!,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_STORAGE_PERMISSION)
        } else {
            startGallery()
        }
    }

    private fun startGallery() {
        val cameraIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(cameraIntent, RESULT_LOAD_IMAGE)
    }

    private fun addSpinnerClickListener() {
        additem_category_spinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categoryLst)
        additem_category_spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
//                Log.d("TEST123", "Maincourse" + categoryLst[position])
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }
        }
    }
}
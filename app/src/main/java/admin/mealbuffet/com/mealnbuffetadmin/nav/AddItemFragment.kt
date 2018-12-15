package admin.mealbuffet.com.mealnbuffetadmin.nav

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.model.addItem
import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.text.TextUtils
import android.util.Base64
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.EditText
import com.mealbuffet.controller.BaseFragment
import kotlinx.android.synthetic.main.fragment_additem.*
import java.io.ByteArrayOutputStream
import java.io.IOException


open class AddItemFragment : BaseFragment() {
    private val REQUEST_STORAGE_PERMISSION = 1001
    private val RESULT_LOAD_IMAGE = 1
    private var uploadBitmapImage: Bitmap? = null

    override fun layoutResource(): Int = R.layout.fragment_additem

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addSpinnerClickListener()
        add_item.setOnClickListener {
            sendAddItemDataToService()
        }
        add_item_image.setOnClickListener { loadImageFromGallery() }
    }

    private fun sendAddItemDataToService() {
        if (isValidEntry(et_additem_name, R.string.add_item_name_warning)) return
        if (isValidEntry(et_additem_price, R.string.add_item_price_warning)) return
        if (isValidEntry(et_additem_desc, R.string.add_item_desc_warning)) return


        val itemName = et_additem_name.text.toString()
        val price = et_additem_price.text.toString().toFloat()
        val desc = et_additem_desc.text.toString()
        var category = additem_category_spinner.selectedItem.toString()
        if (category == getString(R.string.custom)) {
            category = et_additem_custom_category.text.toString()
        }
        val baos = ByteArrayOutputStream()
        uploadBitmapImage?.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val imageBytes = baos.toByteArray()
        val itemImage = Base64.encodeToString(imageBytes, Base64.DEFAULT)

        val addItem = addItem(itemName, price, desc, category, itemImage)
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
        val cakeImageView = add_cakeImg
        //super method removed
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_LOAD_IMAGE) {
                val returnUri = data!!.data
                try {
                    uploadBitmapImage = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, returnUri)
                    cakeImageView.setImageBitmap(uploadBitmapImage)
                    cakeImageView.visibility = View.VISIBLE

                } catch (e: IOException) {
                    showCustomError("Loading Image Failed.. try from camera Folder")
                    cakeImageView.visibility = View.GONE
                }
            }
        } else {
            showCustomError("Loading Image Failed.. try from camera Folder")
        }
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
        additem_category_spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                var category = additem_category_spinner.selectedItem.toString()
                if (category == getString(R.string.custom)) {
                    tv_category.visibility = View.VISIBLE
                } else {
                    tv_category.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }
        }
    }
}
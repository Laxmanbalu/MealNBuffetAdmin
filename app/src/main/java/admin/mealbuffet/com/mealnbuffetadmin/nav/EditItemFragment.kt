package admin.mealbuffet.com.mealnbuffetadmin.nav

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.model.FoodItem
import admin.mealbuffet.com.mealnbuffetadmin.model.updateFoodItem
import admin.mealbuffet.com.mealnbuffetadmin.nav.orderdashboard.ItemBaseFragment
import admin.mealbuffet.com.mealnbuffetadmin.network.ResponseCallback
import admin.mealbuffet.com.mealnbuffetadmin.network.updateItem
import admin.mealbuffet.com.mealnbuffetadmin.util.Constants
import admin.mealbuffet.com.mealnbuffetadmin.util.Constants.EMPTY_STRING
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
import kotlinx.android.synthetic.main.fragment_additem.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*


class EditItemFragment : ItemBaseFragment() {

    private lateinit var selectedFoodItem: FoodItem

    override fun layoutResource(): Int = R.layout.fragment_additem

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }


    override fun setDefaultSpinnerItem() {
        categoryLst.forEachIndexed { index, category ->
            if (category.categoryName == selectedFoodItem.categoryId) {
                additem_category_spinner.setSelection(index)
            }
        }
    }

    override fun sendFoodItemDataToService() {
        if (isValidEntry(et_additem_name, R.string.add_item_name_warning)) return
        if (isValidEntry(et_additem_price, R.string.add_item_price_warning)) return
        if (isValidEntry(et_additem_desc, R.string.add_item_desc_warning)) return
        val itemName = et_additem_name.text.toString()
        val price = et_additem_price.text.toString().toFloat()
        val desc = et_additem_desc.text.toString()
        val categoryId = categoryLst[additem_category_spinner.selectedItemId.toInt()].id
                ?: Constants.EMPTY_STRING
        val foodType = additem_foodtype_spinner.selectedItem.toString()
        val updateFoodItem = updateFoodItem(itemName, price, foodType, desc, categoryId, filePath, selectedFoodItem.itemCode!!)

        val restaurantId = PreferencesHelper.getRestaurantId(requireContext())
        updateItem(updateFoodItem, restaurantId, object : ResponseCallback {
            override fun onError(data: Any?) {
                showNetworkError()
            }

            override fun onSuccess(data: Any?) {
                showCustomError(R.string.item_added_successfully)
                val myFile = File(filePath)
                myFile.delete()
                wrapActionListener().onAction(AddItemFragment.ADDED_ITEM_SUCCESSFULLY)
            }
        })
    }

    private fun initView() {
        et_additem_name.setText(selectedFoodItem.item)
        et_additem_price.setText(selectedFoodItem.price.toString())
        et_additem_desc.setText(selectedFoodItem.desc)
        btn_add_item.text = getString(R.string.updatedetails)

        Glide.with(requireContext())
                .load(selectedFoodItem.image)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        showNetworkError()
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        item_image.setImageDrawable(resource)
                        filePath = saveImage(resource) ?: EMPTY_STRING
                        return true
                    }
                })
                .into(item_image)
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

    fun setSelectedFoodItem(argFoodItem: FoodItem) {
        selectedFoodItem = argFoodItem
    }
}
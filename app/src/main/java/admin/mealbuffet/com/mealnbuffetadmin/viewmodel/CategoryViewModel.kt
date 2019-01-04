package admin.mealbuffet.com.mealnbuffetadmin.viewmodel

import admin.mealbuffet.com.mealnbuffetadmin.model.Category
import admin.mealbuffet.com.mealnbuffetadmin.network.ResponseCallback
import admin.mealbuffet.com.mealnbuffetadmin.network.getCategoriesList
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class CategoryViewModel : ViewModel() {
    var liveData: MutableLiveData<ArrayList<Category>> = MutableLiveData()

    fun getCategoriesListData() {
        if (categoryList != null) {
            liveData.value = categoryList
        } else {
            getCategoriesList(object : ResponseCallback {
                override fun onSuccess(data: Any?) {
                    categoryList = data as ArrayList<Category>
                    liveData.value = data
                }

                override fun onError(data: Any?) {
                    liveData.value = null
                }
            })
        }
    }

    companion object {
        var categoryList: ArrayList<Category>? = null
    }
}
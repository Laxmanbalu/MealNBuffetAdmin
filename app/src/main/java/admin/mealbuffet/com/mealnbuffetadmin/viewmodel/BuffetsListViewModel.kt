package admin.mealbuffet.com.mealnbuffetadmin.viewmodel

import admin.mealbuffet.com.mealnbuffetadmin.model.BuffetItem
import admin.mealbuffet.com.mealnbuffetadmin.network.ResponseCallback
import admin.mealbuffet.com.mealnbuffetadmin.network.getBuffetsList
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders

class BuffetsListViewModel : ViewModel() {
    var liveData: MutableLiveData<ArrayList<BuffetItem>> = MutableLiveData()

    fun fetchBuffetsList(restaurantId: String) {
        getBuffetsList(restaurantId, object : ResponseCallback {
            override fun onSuccess(data: Any?) {
                liveData.value = data as ArrayList<BuffetItem>
            }

            override fun onError(data: Any?) {
                liveData.value = null
            }
        })
    }



}
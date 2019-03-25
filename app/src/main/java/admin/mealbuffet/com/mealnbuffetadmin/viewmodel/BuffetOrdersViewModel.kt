package admin.mealbuffet.com.mealnbuffetadmin.viewmodel

import admin.mealbuffet.com.mealnbuffetadmin.model.BuffetOrderRawData
import admin.mealbuffet.com.mealnbuffetadmin.network.ResponseCallback
import admin.mealbuffet.com.mealnbuffetadmin.network.getBuffetOrdersHistory
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class BuffetOrdersViewModel : ViewModel() {
    var liveData: MutableLiveData<BuffetOrderRawData> = MutableLiveData()

    fun getBuffetOrdersList(restaurantId: String) {
        getBuffetOrdersHistory(restaurantId, object : ResponseCallback {
            override fun onSuccess(data: Any?) {
                liveData.value = data as BuffetOrderRawData
            }

            override fun onError(data: Any?) {
                liveData.value = null
            }
        })
    }
}


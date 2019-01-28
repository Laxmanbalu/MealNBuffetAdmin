package admin.mealbuffet.com.mealnbuffetadmin.viewmodel

import admin.mealbuffet.com.mealnbuffetadmin.model.MealOrderRawData
import admin.mealbuffet.com.mealnbuffetadmin.network.ResponseCallback
import admin.mealbuffet.com.mealnbuffetadmin.network.getMealOrdersHistory
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class MealOrdersViewModel : ViewModel() {
    var liveData: MutableLiveData<MealOrderRawData> = MutableLiveData()

    fun getMealOrdersList(restaurantId: String) {
        getMealOrdersHistory(restaurantId, object : ResponseCallback {
            override fun onSuccess(data: Any?) {
                liveData.value = data as MealOrderRawData
            }

            override fun onError(data: Any?) {
                liveData.value = null
            }
        })
    }
}
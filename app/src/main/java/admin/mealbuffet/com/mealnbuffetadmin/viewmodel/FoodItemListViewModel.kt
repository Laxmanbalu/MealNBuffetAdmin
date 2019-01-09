package admin.mealbuffet.com.mealnbuffetadmin.viewmodel

import admin.mealbuffet.com.mealnbuffetadmin.model.FoodItem
import admin.mealbuffet.com.mealnbuffetadmin.network.ResponseCallback
import admin.mealbuffet.com.mealnbuffetadmin.network.getFoodItemsList
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class FoodItemListViewModel : ViewModel() {
    var liveData: MutableLiveData<ArrayList<FoodItem>> = MutableLiveData()

    fun getFoodItemsData(restaurantId: String) {
        getFoodItemsList(restaurantId, object : ResponseCallback {
            override fun onSuccess(data: Any?) {
                liveData.value = data as ArrayList<FoodItem>
            }

            override fun onError(data: Any?) {
                liveData.value = null
            }
        })

    }
}
package admin.mealbuffet.com.mealnbuffetadmin.model

import admin.mealbuffet.com.mealnbuffetadmin.util.Constants.EMPTY_STRING


data class AddItem(var itemName: String, val price: Float, val foodType: String, val desc: String,
                   val category: String, val imagePath: String)

data class updateFoodItem(var itemName: String, val price: Float, val foodType: String, val desc: String,
                   val category: String, val imagePath: String, val itemCode : String)

data class User(var id: String? = EMPTY_STRING,
                var firstName: String? = EMPTY_STRING,
                var lastName: String? = EMPTY_STRING,
                var mobileNumber: String? = EMPTY_STRING,
                var emailId: String? = EMPTY_STRING,
                val userId: String,
                val password: String,
                val role: String? = EMPTY_STRING,
                val restaurantId: String? = EMPTY_STRING)


data class CreateBuffetItem(var status: Int, var typeDes: String, var itemsList: ArrayList<String>, var buffetBasicData: BuffetBasicData)

data class BuffetBasicData(var adultPrice: Double, var buffetName: String, var displayName: String, var endTime: String,
                           var kidsPrice: Double, var restaurantId: String, var startTime: String, var desc: String,
                           var type: String, var buffetCutOffTime : String)

data class EditBuffetData(val buffetBasicData: BuffetBasicData, val buffetItem: BuffetItem)

data class MealBasicData(var mealName: String, var itemQty: Int, var mealDesc: String, var meaCompMsg: String, var restaurantId: String)

data class CreateMealItem(var status: Int, var typeDes: String, var itemsList: ArrayList<String>, var mealBasicData: MealBasicData)

data class EditMealData(val mealBasicData: MealBasicData, val mealItem: MealItem)


data class BuffetOrder(val date: String, val orderId: String, val mobileNumber: String, val emailId: String,
                       val restaurantId: String, val userId: String, val billedAmount: Float, val numberOfKids: Int,
                       val buffetName: String, val restaurantName: String, val numerOfAudults: Int, val buffetId: String,
                       val id: String, val status: Int)

data class BuffetOrderRawData(val responseStatus: StandardResponse, val buffetOrderList: List<BuffetOrder>)

data class MealList(
        val date: String? = null,
        val image: String? = null,
        val item: String? = null,
        val price: Float? = null,
        val itemCode: String? = null,
        val qty: Int? = null,
        val id: String? = null,
        val restaurantId: String? = null,
        val type: String? = null,
        val categoryId: String? = null,
        val desc: String? = null,
        val status: String? = null
)

data class MealOrders(
        val date: String? = null,
        val billedAmount: Float? = null,
        val mealList: List<List<MealList?>?>? = null,
        val quantity: Int? = null,
        val restaurantName: String? = null,
        val mobileNumber: Any? = null,
        val mealOrderId: String? = null,
        val emailId: String? = null,
        val id: String? = null,
        val restaurantId: String? = null,
        val userId: String? = null,
        val status: Int? = null
)

data class MealOrderRawData(
        val mealOrders: List<MealOrders?>? = null,
        val responseStatus: StandardResponse? = null
)
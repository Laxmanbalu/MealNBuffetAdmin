package admin.mealbuffet.com.mealnbuffetadmin.model

import admin.mealbuffet.com.mealnbuffetadmin.util.Constants.EMPTY_STRING


data class AddItem(var itemName: String, val price: Float, val foodType: String, val desc: String,
                   val category: String, val imagePath: String)

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
                           var type: String)

data class EditBuffetData(val buffetBasicData: BuffetBasicData, val buffetItem: BuffetItem)

data class MealBasicData(var mealName: String, var itemQty: Int, var mealDesc: String, var meaCompMsg: String, var restaurantId: String)

data class CreateMealItem(var status: Int, var typeDes: String, var itemsList: ArrayList<String>, var mealBasicData: MealBasicData)

data class EditMealData(val mealBasicData: MealBasicData, val mealItem: MealItem)


data class BuffetOrder(val date: String, val orderId: String, val mobileNumber: String, val emailId: String,
                       val restaurantId: String, val userId: String, val billedAmount: Float, val numberOfKids: Int,
                       val buffetName: String, val restaurantName: String, val numerOfAudults: Int, val buffetId: String,
                       val id: String, val status: Int)

data class BuffetOrderRawData(val responseStatus: StandardResponse, val buffetOrderList: List<BuffetOrder>)

data class MealIOrderItem(var restaurantId: String, var categoryId: String, var itemCode: String, var item: String,
                          var image: String, var type: String, var price: Float, var status: String, var desc : String,
                          var date: String, var qty: Int, var id: String)

data class MealOrders(var userId: String, var mealOrderId: String, var restaurantId: String, var date: String, var mobileNumber: String,
                      var emailId: String, var status: Int, var restaurantName: String, var quantity: Int, var billedAmount: Float, var mealList: List<List<MealIOrderItem>>)

data class MealOrderRawData(val responseStatus: StandardResponse, val mealOrders: List<MealOrders>)
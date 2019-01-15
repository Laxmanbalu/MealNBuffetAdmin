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
package admin.mealbuffet.com.mealnbuffetadmin.model

import admin.mealbuffet.com.mealnbuffetadmin.util.Constants.EMPTY_STRING
import com.google.gson.annotations.SerializedName

data class BuffetItem(var restaurantId: String = EMPTY_STRING,
                      var buffetName: String = EMPTY_STRING,
                      var buffetId: String = EMPTY_STRING,
                      var activeFlag: Boolean = false,
                      var type: String = EMPTY_STRING,
                      var displayName: String = EMPTY_STRING,
                      var typeDesc: String = EMPTY_STRING,
                      var startTime: String = EMPTY_STRING,
                      var endTime: String = EMPTY_STRING,
                      var kidsPrice: Double = 0.0,
                      var adultPrice: Double = 0.0,
                      var status: String = EMPTY_STRING,
                      var buffetItems: String = EMPTY_STRING,
                      var id: String = EMPTY_STRING,
                      @SerializedName("orderCutOffTime")
                      var orderCutOffTime : String = EMPTY_STRING,
                      @SerializedName("items")
                      var items: HashMap<String, List<BMFoodItem>>? = null

)

data class BMFoodItem(var restaurantId: String = EMPTY_STRING,
                      var categoryId: String = EMPTY_STRING,
                      var itemCode: String = EMPTY_STRING,
                      var item: String = EMPTY_STRING,
                      var desc: String = EMPTY_STRING,
                      var image: String = EMPTY_STRING,
                      var type: String = EMPTY_STRING,
                      var price: Double = 0.0,
                      var status: String = EMPTY_STRING,
                      var date: String = EMPTY_STRING,
                      var id: String = EMPTY_STRING,
                      var qty: Int = 0)

data class MealItem(var mealId: String = EMPTY_STRING,
                    var mealName: String = EMPTY_STRING,
                    var restaurantId: String = EMPTY_STRING,
                    var complimentory: String = EMPTY_STRING,
                    var mealItems: String = EMPTY_STRING,
                    var id: String = EMPTY_STRING,
                    var activeFlag: Boolean = false,
                    var itemsQty: Int = 0,
                    @SerializedName("itemsMap")
                    var items: HashMap<String, List<BMFoodItem>>? = null)
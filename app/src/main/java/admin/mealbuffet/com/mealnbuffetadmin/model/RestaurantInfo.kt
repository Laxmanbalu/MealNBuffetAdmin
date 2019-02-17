package admin.mealbuffet.com.mealnbuffetadmin.model

import admin.mealbuffet.com.mealnbuffetadmin.util.Constants.EMPTY_STRING
import com.google.gson.annotations.SerializedName

data class RestaurantDetails(
        val zipCode: Int? = null,
        val distance: Int? = null,
        val city: String? = null,
        val icon: String = EMPTY_STRING,
        val rating: Double? = null,
        val timeZone: String = EMPTY_STRING,
        val discount: Int? = null,
        val restaurantId: String? = null,
        val accountNumber: String? = null,
        val type: List<String?>? = null,
        val complementary: Any? = null,
        val isBuffetAvailable: Boolean = false,
        val tax1: Double? = null,
        val buffetMap: Any? = null,
        val tax2: Double? = null,
        val mealAvailable: Boolean = false,
        val phoneNumber: String? = null,
        val discountApplicable: Boolean? = null,
        val restaurantName: String? = null,
        val street: String? = null,
        val minPrice: Int? = null,
        @SerializedName("_id")
        val id: String = EMPTY_STRING,
        val state: String? = null)

data class UpdateRestaurantDetails(val isBuffetAvailable: Boolean = false, val mealAvailable: Boolean = false,
                                   val tax1: Double, val tax2: Double, val street: String, val city: String, val state: String,
                                   val zipCode: Int, val restaurantName: String, val phoneNumber: String, val foodType: List<String>,
                                   val restaurantId: String, val _id: String, val icon: String, val timeZone: String)
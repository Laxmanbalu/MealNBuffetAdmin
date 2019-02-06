package admin.mealbuffet.com.mealnbuffetadmin.model

data class RestaurantDetails(
        val zipCode: Int? = null,
        val distance: Int? = null,
        val city: String? = null,
        val icon: String? = null,
        val rating: Double? = null,
        val timeZone: String? = null,
        val discount: Int? = null,
        val restaurantId: String? = null,
        val accountNumber: String? = null,
        val type: List<String?>? = null,
        val complementary: Any? = null,
        val isBuffetAvailable: Boolean = false,
        val tax1: Int? = null,
        val buffetMap: Any? = null,
        val tax2: Int? = null,
        val mealAvailable: Boolean = false,
        val phoneNumber: String? = null,
        val discountApplicable: Boolean? = null,
        val restaurantName: String? = null,
        val street: String? = null,
        val minPrice: Int? = null,
        val id: String? = null,
        val state: String? = null)

data class UpdateRestaurantDetails(val isBuffetAvailable: Boolean = false, val mealAvailable: Boolean = false,
                                   val tax1: String, val tax2: String, val street: String, val city: String, val state : String,
                                   val zipCode: Int, val restaurantName: String, val phoneNumber: String, val foodType: List<String>,
                                   val restaurantId: String)
package admin.mealbuffet.com.mealnbuffetadmin.model

data class FoodItem(
        val restaurantId: String? = null,
        val categoryId: String? = null,
        val itemCode: String? = null,
        val item: String? = null,
        val desc: String? = null,
        val image: String? = null,
        val type: String? = null,
        val price: Float? = null,
        val status: String? = null,
        val date: String? = null,
        val qty: Int? = null,
        val id: String? = null
)
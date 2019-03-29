package admin.mealbuffet.com.mealnbuffetadmin.model

data class Report(
	val date: String? = null,
	val agreedCommision: Int? = null,
	val restaurantName: String? = null,
	val emailId: String? = null,
	val reportDataList: List<Any?>? = null
)

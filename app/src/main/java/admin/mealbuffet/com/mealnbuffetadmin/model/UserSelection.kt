package admin.mealbuffet.com.mealnbuffetadmin.model


data class AddItem(var itemName: String, val price: Float, val foodType : String, val desc: String,
                   val category: String, val imagePath: String)
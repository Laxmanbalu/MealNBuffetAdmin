package admin.mealbuffet.com.mealnbuffetadmin.util

object Constants {
    const val EMPTY_STRING = ""
    const val SPACING_STRING = " "
}

enum class BuffetOrderStatus(var status: Int) {

    //    ORDERED  -0 - client
//    ACCEPTED -1 - admin
//    COMPLETED-4 - admin
//    REJECTED-5  - Admin
//    CANCELED-6  - Client
    ACCEPTED(1),
    COMPLETED(4),
    REJECTED(5)
}
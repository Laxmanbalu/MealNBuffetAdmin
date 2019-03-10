package admin.mealbuffet.com.mealnbuffetadmin.util

object Constants {
    const val EMPTY_STRING = ""
    const val NEW_LINE = "\n"
    const val SPACING_STRING = " "
}

enum class BuffetOrderStatus(var status: Int) {

    //    ORDERED  -0 - client
//    ORDERED -1 - admin
//    COMPLETED-4 - admin
//    REJECTED-5  - Admin
//    CANCELED-6  - Client
//    ORDERED(0),
    ORDERED(0),
    COMPLETED(4),
    REJECTED(5)
}

enum class MealOrderStatus(var status: Int) {
    //    ORDERED -0 - client
//    INPROGRESS-2 - admin
//    READY TO PICKUP-3 - admin
//    COMPLETED-4 - admin
//    REJECTED-5  -Admin
//    CANCELED-6  -Client
    IN_PROGRESS(2),
    READY_TO_PICKUP(3),
    COMPLETED(4),
    REJECTED(5)
}
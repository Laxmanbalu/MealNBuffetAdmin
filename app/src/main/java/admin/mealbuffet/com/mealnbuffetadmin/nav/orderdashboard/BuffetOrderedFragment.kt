package admin.mealbuffet.com.mealnbuffetadmin.nav.orderdashboard

import admin.mealbuffet.com.mealnbuffetadmin.util.BuffetOrderStatus

class BuffetOrderedFragment : BuffetOrderBaseFragment() {
    override fun getBuffetOrdersHistory() {
        val ordersList = getBuffetOrdersList()
        val mealOrdersPendingHistory = ordersList?.filter {
            it.status == BuffetOrderStatus.ACCEPTED.status
        }
        updateViews(mealOrdersPendingHistory)
    }

}

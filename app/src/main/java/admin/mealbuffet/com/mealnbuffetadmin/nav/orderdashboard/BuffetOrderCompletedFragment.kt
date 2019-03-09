package admin.mealbuffet.com.mealnbuffetadmin.nav.orderdashboard

import admin.mealbuffet.com.mealnbuffetadmin.util.BuffetOrderStatus

class BuffetOrderCompletedFragment : BuffetOrderBaseFragment() {
    override fun getBuffetOrdersHistory() {
        val ordersList = getBuffetOrdersList()
        val mealOrdersPendingHistory = ordersList?.filter {
            it.status == BuffetOrderStatus.COMPLETED.status
        }
        updateViews(mealOrdersPendingHistory)
    }
}

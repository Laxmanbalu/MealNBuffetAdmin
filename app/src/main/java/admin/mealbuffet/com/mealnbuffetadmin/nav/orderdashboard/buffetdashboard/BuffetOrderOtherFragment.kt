package admin.mealbuffet.com.mealnbuffetadmin.nav.orderdashboard.buffetdashboard

import admin.mealbuffet.com.mealnbuffetadmin.util.BuffetOrderStatus

class BuffetOrderOtherFragment : BuffetOrderBaseFragment() {
    override fun getBuffetOrdersHistory() {
        val ordersList = getBuffetOrdersList()
        val mealOrdersPendingHistory = ordersList?.filter {
            it.status != BuffetOrderStatus.COMPLETED.status && it.status != BuffetOrderStatus.ORDERED.status
        }
        updateViews(mealOrdersPendingHistory)
    }
}

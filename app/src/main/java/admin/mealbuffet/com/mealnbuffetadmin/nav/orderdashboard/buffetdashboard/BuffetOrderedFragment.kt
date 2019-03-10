package admin.mealbuffet.com.mealnbuffetadmin.nav.orderdashboard.buffetdashboard

import admin.mealbuffet.com.mealnbuffetadmin.util.BuffetOrderStatus

class BuffetOrderedFragment : BuffetOrderBaseFragment() {

    override fun getBuffetOrdersHistory() {
        val ordersList = getBuffetOrdersList()
        val mealOrdersPendingHistory = ordersList?.filter {
            it.status == BuffetOrderStatus.ORDERED.status
        }
        updateViews(mealOrdersPendingHistory)
    }
}

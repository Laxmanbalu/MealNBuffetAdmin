package admin.mealbuffet.com.mealnbuffetadmin.nav.orderdashboard.mealdashboard

import admin.mealbuffet.com.mealnbuffetadmin.util.MealOrderStatus

class MealOrderedFragment : MealOrderBaseFragment() {
    override fun getMealOrdersHistory() {
        val ordersList = getMealOrdersList()
        val mealOrdersPendingHistory = ordersList?.filter {
            it.status == MealOrderStatus.ORDERED.status ||
                    it.status == MealOrderStatus.IN_PROGRESS.status ||
                    it.status == MealOrderStatus.READY_TO_PICKUP.status
        }
        updateViews(mealOrdersPendingHistory)
    }
}

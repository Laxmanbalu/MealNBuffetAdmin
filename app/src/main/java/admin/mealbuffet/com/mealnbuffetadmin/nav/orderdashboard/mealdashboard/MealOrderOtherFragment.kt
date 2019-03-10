package admin.mealbuffet.com.mealnbuffetadmin.nav.orderdashboard.mealdashboard

import admin.mealbuffet.com.mealnbuffetadmin.util.MealOrderStatus

class MealOrderOtherFragment : MealOrderBaseFragment() {
    override fun getMealOrdersHistory() {
        val ordersList = getMealOrdersList()
        val mealOrdersPendingHistory = ordersList?.filter {
            it.status != MealOrderStatus.IN_PROGRESS.status &&
                    it.status != MealOrderStatus.READY_TO_PICKUP.status &&
                    it.status != MealOrderStatus.COMPLETED.status
        }
        updateViews(mealOrdersPendingHistory)
    }

}

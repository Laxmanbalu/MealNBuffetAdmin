package admin.mealbuffet.com.mealnbuffetadmin.nav.orderdashboard.mealdashboard

import admin.mealbuffet.com.mealnbuffetadmin.util.MealOrderStatus

class MealOrderCompletedFragment : MealOrderBaseFragment() {
    override fun getMealOrdersHistory() {
        val ordersList = getMealOrdersList()
        val mealOrdersPendingHistory = ordersList?.filter {
            it.status == MealOrderStatus.COMPLETED.status
        }
        updateViews(mealOrdersPendingHistory)
    }

}

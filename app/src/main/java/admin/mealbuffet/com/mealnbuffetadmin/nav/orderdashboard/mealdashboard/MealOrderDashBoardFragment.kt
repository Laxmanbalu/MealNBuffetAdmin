package admin.mealbuffet.com.mealnbuffetadmin.nav.orderdashboard.mealdashboard

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.model.MealOrders
import admin.mealbuffet.com.mealnbuffetadmin.util.MealOrderStatus
import admin.mealbuffet.com.mealnbuffetadmin.util.PreferencesHelper
import admin.mealbuffet.com.mealnbuffetadmin.viewmodel.MealOrdersViewModel
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter
import android.view.View
import com.mealbuffet.controller.BaseFragment
import kotlinx.android.synthetic.main.fragment_mealdashboard.*
import java.util.*

class MealOrderDashBoardFragment : BaseFragment() {
    private lateinit var mealOrdersViewModel: MealOrdersViewModel
    private lateinit var mealOrders: List<MealOrders>

    private val mealOrderedFragment by lazy { MealOrderedFragment() }
    private val mealCompletedFragment by lazy { MealOrderCompletedFragment() }
    private val mealOtherFragment by lazy { MealOrderOtherFragment() }
    private val pageAdapter by lazy { ViewPagerAdapter(childFragmentManager) }

    override fun layoutResource(): Int = R.layout.fragment_mealdashboard

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        initMealOrdersViewModel()
    }

    private fun initMealOrdersViewModel() {
        mealOrdersViewModel = ViewModelProviders.of(requireActivity()).get(MealOrdersViewModel::class.java)
        mealOrdersViewModel.liveData.observe(this, Observer {
            if (it == null) {
                showNetworkError()
            } else {
                mealOrders = it.mealOrders as List<MealOrders>
                updateTabTitles(mealOrders)
                when (meal_orders_viewpager.currentItem) {
                    0 -> mealOrderedFragment.refreshView()
                    1 -> mealCompletedFragment.refreshView()
                    2 -> mealOtherFragment.refreshView()
                }
            }
        })
        fetchMealOrdersList()
    }

    private fun updateTabTitles(mealOrdersHistory: List<MealOrders>) {

        //ToUpdate Ordered tab title
        val mealOrderedOrdersHistory = mealOrdersHistory.filter {
            it.status == MealOrderStatus.IN_PROGRESS.status || it.status == MealOrderStatus.READY_TO_PICKUP.status || it.status == MealOrderStatus.ORDERED.status
        }

        val acceptedTitle = String.format(getString(R.string.ordered), mealOrderedOrdersHistory.size)
        val acceptedTab = meal_orders_tab.getTabAt(0)
        acceptedTab!!.text = acceptedTitle


        //Method to update Completed Tab title
        val mealCompletedOrdersHistory = mealOrdersHistory.filter {
            it.status == MealOrderStatus.COMPLETED.status
        }
        val completedTitle = String.format(getString(R.string.completed), mealCompletedOrdersHistory.size)
        val completedTab = meal_orders_tab.getTabAt(1)
        completedTab!!.text = completedTitle


        //ToUpdate Other tab title
        val mealRejectedOrdersHistory = mealOrdersHistory.filter {
            it.status != MealOrderStatus.IN_PROGRESS.status &&
                    it.status != MealOrderStatus.COMPLETED.status &&
                    it.status != MealOrderStatus.READY_TO_PICKUP.status &&
                    it.status != MealOrderStatus.ORDERED.status
        }

        val rejectedTitle = String.format(getString(R.string.other), mealRejectedOrdersHistory.size)
        val rejectedTab = meal_orders_tab.getTabAt(2)
        rejectedTab!!.text = rejectedTitle

    }

    private fun fetchMealOrdersList() {
        val restaurantId = PreferencesHelper.getRestaurantId(requireContext())
        mealOrdersViewModel.getMealOrdersList(restaurantId)
    }

    private fun setupViewPager() {
        val strMealOrderedTitle = String.format(getString(R.string.ordered), 0)
        val strMealCompletedTitle = String.format(getString(R.string.completed), 0)
        val strMealOtherTitle = String.format(getString(R.string.other), 0)

        pageAdapter.addFragment(mealOrderedFragment, strMealOrderedTitle)
        pageAdapter.addFragment(mealCompletedFragment, strMealCompletedTitle)
        pageAdapter.addFragment(mealOtherFragment, strMealOtherTitle)

        meal_orders_viewpager.adapter = pageAdapter
        meal_orders_viewpager.offscreenPageLimit = 3
        meal_orders_tab.setupWithViewPager(meal_orders_viewpager)
        meal_orders_viewpager.currentItem = 0
    }


    internal inner class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getItemPosition(item: Any): Int {
            return PagerAdapter.POSITION_NONE
        }


        override fun getCount(): Int {
            return 3 //MAx Tabs 3
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitleList[position]
        }
    }
}
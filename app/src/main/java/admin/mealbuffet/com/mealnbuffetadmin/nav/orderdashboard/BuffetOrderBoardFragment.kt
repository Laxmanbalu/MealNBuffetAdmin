package admin.mealbuffet.com.mealnbuffetadmin.nav.orderdashboard

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.model.BuffetOrder
import admin.mealbuffet.com.mealnbuffetadmin.util.BuffetOrderStatus
import admin.mealbuffet.com.mealnbuffetadmin.util.PreferencesHelper
import admin.mealbuffet.com.mealnbuffetadmin.viewmodel.BuffetOrdersViewModel
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter
import android.view.View
import com.mealbuffet.controller.BaseFragment
import kotlinx.android.synthetic.main.fragment_buffetdashboard.*
import java.util.*

class BuffetOrderBoardFragment : BaseFragment() {
    private var buffetOrdersList = ArrayList<BuffetOrder>()
    private val buffetOrderedFragment by lazy { BuffetOrderedFragment() }
    private val buffetCompletedFragment by lazy { BuffetOrderCompletedFragment() }
    private val buffetOtherFragment by lazy { BuffetOrderOtherFragment() }
    private val pageAdapter by lazy { ViewPagerAdapter(childFragmentManager) }
    private lateinit var buffetOrdersViewModel: BuffetOrdersViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        initBuffetOrdersViewModel()
    }

    override fun layoutResource(): Int = R.layout.fragment_buffetdashboard

    private fun setupViewPager() {

        val strBuffetOrderedTitle = String.format(getString(R.string.ordered), 0)
        val strBuffetCompletedTitle = String.format(getString(R.string.completed), 0)
        val strBuffetOtherTitle = String.format(getString(R.string.other), 0)

        pageAdapter.addFragment(buffetOrderedFragment, strBuffetOrderedTitle)
        pageAdapter.addFragment(buffetCompletedFragment, strBuffetCompletedTitle)
        pageAdapter.addFragment(buffetOtherFragment, strBuffetOtherTitle)

        buffet_orders_viewpager.adapter = pageAdapter
        buffet_orders_viewpager.offscreenPageLimit = 3
        buffet_orders_tab.setupWithViewPager(buffet_orders_viewpager)
        buffet_orders_viewpager.currentItem = 0
    }

    private fun initBuffetOrdersViewModel() {
        buffetOrdersViewModel = ViewModelProviders.of(requireActivity()).get(BuffetOrdersViewModel::class.java)
        buffetOrdersViewModel.liveData.observe(this, Observer {
            if (it == null) {
                showNetworkError()
            } else {
                buffetOrdersList = it.buffetOrderList as ArrayList<BuffetOrder>
                updateTabTitles(buffetOrdersList)
                when (buffet_orders_viewpager.currentItem) {
                    0 -> buffetOrderedFragment.refreshView()
                    1 -> buffetCompletedFragment.refreshView()
                    2 -> buffetOtherFragment.refreshView()
                }
            }
        })
        fetchBuffetOrdersList()
    }


    private fun updateTabTitles(mealOrdersHistory: ArrayList<BuffetOrder>) {

        //ToUpdate Ordered tab title
        val mealOrderedOrdersHistory = mealOrdersHistory?.filter {
            it.status == BuffetOrderStatus.ORDERED.status
        }

        val acceptedTitle = String.format(getString(R.string.ordered), mealOrderedOrdersHistory?.size)
        val acceptedTab = buffet_orders_tab.getTabAt(0)
        acceptedTab!!.text = acceptedTitle


        //Method to update Completed Tab title
        val mealCompletedOrdersHistory = mealOrdersHistory?.filter {
            it.status == BuffetOrderStatus.COMPLETED.status
        }
        val completedTitle = String.format(getString(R.string.completed), mealCompletedOrdersHistory?.size)
        val completedTab = buffet_orders_tab.getTabAt(1)
        completedTab!!.text = completedTitle


        //ToUpdate Other tab title
        val mealRejectedOrdersHistory = mealOrdersHistory?.filter {
            it.status != BuffetOrderStatus.ORDERED.status && it.status != BuffetOrderStatus.COMPLETED.status
        }

        val rejectedTitle = String.format(getString(R.string.other), mealRejectedOrdersHistory?.size)
        val rejectedTab = buffet_orders_tab.getTabAt(2)
        rejectedTab!!.text = rejectedTitle

    }

    private fun fetchBuffetOrdersList() {
        val restaurantId = PreferencesHelper.getRestaurantId(requireContext())
        buffetOrdersViewModel.getBuffetOrdersList(restaurantId)
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
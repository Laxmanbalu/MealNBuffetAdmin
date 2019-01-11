package admin.mealbuffet.com.mealnbuffetadmin.nav.buffet

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.model.BuffetItem
import admin.mealbuffet.com.mealnbuffetadmin.nav.InternalActionListener
import admin.mealbuffet.com.mealnbuffetadmin.util.PreferencesHelper
import admin.mealbuffet.com.mealnbuffetadmin.viewmodel.BuffetsListViewModel
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.mealbuffet.controller.BaseFragment
import kotlinx.android.synthetic.main.fragment_buffetlist.*

class BuffetListFragment : BaseFragment(), InternalActionListener {
    override fun onAction(action: String, data: Any?) {
    }

    override fun layoutResource(): Int = R.layout.fragment_buffetlist
    private lateinit var buffetItemsAdapter: BuffetItemsAdapter
    private lateinit var buffetsListViewModel: BuffetsListViewModel
    private var buffetItemsList = ArrayList<BuffetItem>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBuffetsListViewModel()
    }

    private fun initBuffetsListViewModel() {
        buffetsListViewModel = ViewModelProviders.of(this).get(BuffetsListViewModel::class.java)
        buffetsListViewModel.liveData.observe(this, Observer {
            if (it == null) {
                showNetworkError()
            } else {
                buffetItemsList = it
                renderFoodItemsView()
            }
        })
        getBuffetsListData()
    }

    private fun getBuffetsListData() {
        val restaurantId = PreferencesHelper.getRestaurantId(requireContext())
        buffetsListViewModel.fetchBuffetsList(restaurantId)
    }

    private fun renderFoodItemsView() {
        buffetItemsAdapter = BuffetItemsAdapter(requireContext(), this as InternalActionListener)
        buffetItems_recyclerView.apply {
            adapter = buffetItemsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        buffetItems_recyclerView.itemAnimator = DefaultItemAnimator()
        buffetItemsAdapter.setData(buffetItemsList)
        buffetItemsAdapter.notifyDataSetChanged()
    }
}
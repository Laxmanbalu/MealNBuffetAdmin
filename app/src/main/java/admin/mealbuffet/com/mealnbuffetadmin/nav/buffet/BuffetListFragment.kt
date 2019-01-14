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
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.mealbuffet.controller.BaseFragment
import kotlinx.android.synthetic.main.fragment_buffetlist.*

class BuffetListFragment : BaseFragment(), InternalActionListener {
    override fun onAction(action: String, data: Any?) {
        when (action) {
            DELETED_BUFFET_SUCCESSFULLY -> {
                showCustomError(getString(R.string.deleted_item_successfully))
                getBuffetsListData()
            }
            DELETED_BUFFET_FAILED -> showNetworkError()
            PUBLISHED_BUFFET_SUCCESSFULLY -> {
                showCustomError(getString(R.string.publish_item_successfully))
                getBuffetsListData()
            }
            PUBLISHED_BUFFET_FAILED -> showNetworkError()
        }
    }

    override fun layoutResource(): Int = R.layout.fragment_buffetlist
    private lateinit var buffetItemsAdapter: BuffetItemsAdapter
    private lateinit var buffetsListViewModel: BuffetsListViewModel
    private var buffetItemsList = ArrayList<BuffetItem>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBuffetsListViewModel()
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_add_buffet, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_list_add_buffet -> {
                wrapActionListener().onAction(ADD_BUFFET)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initBuffetsListViewModel() {
        buffetsListViewModel = ViewModelProviders.of(this).get(BuffetsListViewModel::class.java)
        buffetsListViewModel.liveData.observe(this, Observer {
            if (it == null) {
                showNetworkError()
            } else {
                buffetItemsList.clear()
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

    companion object {
        const val DELETED_BUFFET_SUCCESSFULLY = "DeletedBuffetItemSuccessfully"
        const val DELETED_BUFFET_FAILED = "DeletedBuffetItemFailed"
        const val PUBLISHED_BUFFET_SUCCESSFULLY = "PublishedBuffetItemSuccessfully"
        const val PUBLISHED_BUFFET_FAILED = "PublishedBuffetItemFailed"
        const val ADD_BUFFET: String = "addBuffet"
    }
}
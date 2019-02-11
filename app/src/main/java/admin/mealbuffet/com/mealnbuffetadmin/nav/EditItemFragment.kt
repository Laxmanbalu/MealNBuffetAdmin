package admin.mealbuffet.com.mealnbuffetadmin.nav

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.model.Category
import admin.mealbuffet.com.mealnbuffetadmin.model.FoodItem
import admin.mealbuffet.com.mealnbuffetadmin.viewmodel.CategoryViewModel
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.mealbuffet.controller.BaseFragment
import kotlinx.android.synthetic.main.fragment_additem.*


class EditItemFragment : BaseFragment() {
    private lateinit var categoryLst: ArrayList<Category>
    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var selectedFoodItem: FoodItem

    override fun layoutResource(): Int = R.layout.fragment_additem

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCategoryViewModel()
        initView()
    }

    private fun initCategoryViewModel() {
        categoryViewModel = ViewModelProviders.of(requireActivity()).get(CategoryViewModel::class.java)
        categoryViewModel.liveData.observe(this, Observer { it ->
            if (it == null) {
                showNetworkError()
            } else {
                categoryLst = it
                addSpinnerClickListener()
            }
        })
        categoryViewModel.getCategoriesListData()
    }

    private fun addSpinnerClickListener() {
        additem_category_spinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categoryLst)
        categoryLst.forEachIndexed { index, category ->
            if (category.categoryName == selectedFoodItem.categoryId) {
                additem_category_spinner.setSelection(index)
            }
        }
    }

    private fun initView() {
        et_additem_name.setText(selectedFoodItem.item)
        et_additem_price.setText(selectedFoodItem.price.toString())
        et_additem_desc.setText(selectedFoodItem.desc)
        Glide.with(requireContext()).load(selectedFoodItem.image).into(item_image)
    }

    fun setSelectedFoodItem(argFoodItem: FoodItem) {
        selectedFoodItem = argFoodItem
    }
}
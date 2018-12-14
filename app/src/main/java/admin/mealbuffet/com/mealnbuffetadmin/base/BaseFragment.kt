package com.mealbuffet.controller

import admin.mealbuffet.com.mealnbuffetadmin.R
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class BaseFragment : Fragment() {

    private var needToUpdateViews = false

    abstract fun layoutResource(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(layoutResource(), container, false)

    override fun onResume() {
        super.onResume()
        viewUpdateOnResume()
    }

    fun showProgress() = (activity as BaseActivity).showProgress()
    fun showNetworkError() = (activity as BaseActivity).showNetworkError()
    fun showCustomError(resourceId: Int = R.string.error_network) = (activity as BaseActivity).showCustomError(resourceId)
    fun showCustomError(errorMSg: String = getString(R.string.error_network)) = (activity as BaseActivity).showCustomError(errorMSg)

    fun hideProgress() {
        (activity as? BaseActivity)?.let {
            it.notifyPendingChange(Runnable {
                it.hideProgress()
            })
        }
    }

    private fun viewUpdateOnResume() {
        if (needToUpdateViews) {
            updateViews()
            needToUpdateViews = false
        }
    }

    fun notifyModelChanged() {
        if (isResumed) {
            activity?.runOnUiThread(updateViewsRunnable)
        } else {
            needToUpdateViews = true
        }
    }

    private val updateViewsRunnable = Runnable {
        updateViews()
    }

    open fun updateViews() {
    }

    var actionListener : ActionListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context is ActionListener){
            actionListener = context
        } else {
            throw Exception("Activity is expected to implement ActionListener")
        }
    }

    override fun onDetach() {
        actionListener = null
        super.onDetach()
    }

    /**
     * Wrap the action listener when passing through to an adapter. Never hold a reference
     * to the actual action listener
     */
    fun wrapActionListener() = object : ActionListener {
        override fun onAction(action: String, data: Any?) {
            actionListener?.onAction(action, data)
        }
    }
}
package com.mealbuffet.controller

import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.auth.LoginActivity
import admin.mealbuffet.com.mealnbuffetadmin.custom.DialogClickListener
import admin.mealbuffet.com.mealnbuffetadmin.custom.YesNoDialogFragment
import admin.mealbuffet.com.mealnbuffetadmin.util.Constants.EMPTY_STRING
import admin.mealbuffet.com.mealnbuffetadmin.util.PreferencesHelper
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.MenuItem
import com.mealbuffet.custom.ProgressDialogFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.left_menu_view.*
import kotlinx.android.synthetic.main.nav_drawer_footer.*
import kotlinx.android.synthetic.main.nav_drawer_header.view.*
import kotlinx.android.synthetic.main.toolbar.*

abstract class BaseActivity : AppCompatActivity(), ActionListener, NavigationView.OnNavigationItemSelectedListener {
    private lateinit var progressDialog: ProgressDialogFragment
    private val loadingDialogTag = "ProgressDialog"
    private val runOnResume = ArrayList<Runnable>()
    protected var resumedState = false
    open var menuItemId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        leftNavMenu.setNavigationItemSelectedListener(this)
        initNavigationDrawer()
    }


    private fun initProgressDialog() {
        if (!::progressDialog.isInitialized) {
            progressDialog = ProgressDialogFragment()
        }
    }

    /**
     * Handles menu option selection
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                drawer.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun showProgress() {
        initProgressDialog()
        if (!progressDialog.isAdded) {
            progressDialog.show(supportFragmentManager, loadingDialogTag)
        }
    }

    fun getSpannableTitle(): SpannableString {
        val wordSpan = SpannableString(getString(R.string.app_name))
        wordSpan.setSpan(ForegroundColorSpan(getColor(R.color.black)), 0, getString(R.string.title_meal).length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return wordSpan
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            menuItemId -> {
                //If it's current screen -> close
                drawer.closeDrawers()
                return true
            }
            else -> {
                drawer.closeDrawers()
                handleNavigationItemSelected(item)
            }
        }
        drawer.closeDrawers()
        return true
    }

    open fun handleNavigationItemSelected(item: MenuItem) {}

    private fun updateMenuSelection() {
        if (menuItemId > 0) {
            leftNavMenu.setCheckedItem(menuItemId)
        }
    }

    override fun onResume() {
        super.onResume()
        resumedState = true
        viewUpdateOnResume()
        updateMenuSelection()
    }

    // initializing the navigation drawer views
    private fun initNavigationDrawer() {
        val headerView = leftNavMenu.getHeaderView(0)
        headerView.nav_header_username.text = PreferencesHelper.getRestaurantId(this)

        leftNavSignOut.setOnClickListener {
            showSignOutDialog()
        }
    }

    private fun showSignOutDialog() {
        val dialog = YesNoDialogFragment.newInstance(
                resources.getString(R.string.sign_out_dialog_title),
                resources.getString(R.string.sign_out_dialog_msg))

        dialog.setDialogActionListener(object : DialogClickListener {
            override fun onPositiveBanClick(addToBackStack: Any?) {
                dialog.dismiss()
                PreferencesHelper.storeRestaurantDetails(applicationContext, EMPTY_STRING)
                //Move to login activity
                var intent = Intent(applicationContext, LoginActivity::class.java)
                intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }

            override fun onNegativeBtnClick() {
                dialog.dismiss()
            }

        })
        dialog.setButtonLabels(resources.getString(R.string.sign_out_button), resources.getString(R.string.cancel))
        dialog.show(supportFragmentManager, EMPTY_STRING)
    }


    private fun viewUpdateOnResume() {
        if (!runOnResume.isEmpty()) {
            runOnResume.forEach {
                it.run()
            }
            runOnResume.clear()
        }
    }

    fun hideProgress() {
        notifyPendingChange(Runnable {
            if (::progressDialog.isInitialized) {
                progressDialog.dismiss()
            }
        })
    }

    fun notifyPendingChange(runnable: Runnable) {
        if (resumedState) {
            runOnUiThread(runnable)
        } else {
            runOnResume.add(runnable)
        }
    }

    fun showNetworkError(resourceId: Int = R.string.error_network) {
        Snackbar.make(coordinatorLayout, resourceId, Snackbar.LENGTH_LONG).show()
    }

    fun showCustomError(errorMsg: String = getString(R.string.error_network)) {
        Snackbar.make(coordinatorLayout, errorMsg, Snackbar.LENGTH_SHORT).show()
    }

    fun showCustomError(resourceId: Int = R.string.error_network) {
        Snackbar.make(coordinatorLayout, resourceId, Snackbar.LENGTH_SHORT).show()
    }

    fun setHomeIcon(res: Int) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(res)
    }

    open fun showFragment(fragment: Fragment, addToBackStack: Boolean = false) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(fragment.javaClass.simpleName)
        }
        notifyPendingChange(Runnable {
            fragmentTransaction.commit()
        })
    }

    override fun onAction(action: String, data: Any?) {
        when (action) {
            ACTION_SHOW_NETWORK_ERROR -> showNetworkError()
            else -> if (!onPerformAction(action, data)) Log.d("Error", "Unexpected action: $action")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        runOnResume.clear()
    }

    open fun onPerformAction(action: String, data: Any?) = false

    companion object {
        val ACTION_SHOW_NETWORK_ERROR = BaseActivity::class.java.simpleName + ".showNetworkError"
    }
}
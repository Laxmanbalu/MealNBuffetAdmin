package com.mealbuffet.custom

import admin.mealbuffet.com.mealnbuffetadmin.R
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.ViewGroup
import android.view.Window


class ProgressDialog(context: Context) : AlertDialog(context) {

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.apply {
            attributes.apply {
                width = ViewGroup.LayoutParams.MATCH_PARENT
                height = ViewGroup.LayoutParams.MATCH_PARENT
                horizontalMargin = 0f
                verticalMargin = 0f
            }
            setBackgroundDrawable(null)
        }
        setCanceledOnTouchOutside(false)
        setCancelable(false)
        setContentView(R.layout.dialog_loading_light)
    }
}
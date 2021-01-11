package com.example.expenseappmvvm.screens.baseFragmentDialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.View
import androidx.fragment.app.DialogFragment

abstract class BaseFragmentDialog(private val dialogLayout: Int) : DialogFragment() {

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val contentView = View.inflate(context, dialogLayout, null)
        dialog.setContentView(contentView)

        getContentView(contentView)
    }

    abstract fun getContentView(contentView: View)
}
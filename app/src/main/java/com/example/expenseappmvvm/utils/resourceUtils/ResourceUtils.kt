package com.example.expenseappmvvm.utils.resourceUtils

import android.content.Context

class ResourceUtils(private val context: Context) {
    fun getStringResource(stringId: Int): String{
        return context.getString(stringId)
    }

    fun getContext(): Context{
        return context
    }
}
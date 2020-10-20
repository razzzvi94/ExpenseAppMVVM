package com.example.expenseappmvvm.utils.resourceUtils

import android.content.Context

class ResourceUtils(private val context: Context) {
    fun getStringResource(stringId: Int): String{
        return context.getString(stringId)
    }

    fun getStringResourceAppend(stringId: Int, date: String): String{
        return context.getString(stringId, date)
    }

    fun getContext(): Context{
        return context
    }
}
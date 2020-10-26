package com.example.expenseappmvvm.utils.resourceUtils

import android.content.Context
import android.net.ConnectivityManager

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

    /**
     * Checks if the device is connected to a network
     *
     * @return true or false depending on the device connectivity
     */
    fun isNetworkConnected(): Boolean {
        val connManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connManager.activeNetworkInfo ?: return false
        return networkInfo.isConnected
    }
}
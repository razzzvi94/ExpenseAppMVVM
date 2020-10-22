package com.example.expenseappmvvm.utils

import android.content.Context
import android.net.ConnectivityManager
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun Disposable.disposeBy(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}

/**
 * Checks if the device is connected to a network
 *
 * @return true or false depending on the device connectivity
 */
fun isNetworkConnected(context: Context): Boolean {
    val connManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connManager.activeNetworkInfo ?: return false
    return networkInfo.isConnected
}
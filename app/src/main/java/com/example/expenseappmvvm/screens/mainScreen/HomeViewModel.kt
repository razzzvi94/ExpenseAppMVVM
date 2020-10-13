package com.example.expenseappmvvm.screens.mainScreen

import androidx.lifecycle.ViewModel
import com.example.expenseappmvvm.utils.Preferences
import com.example.expenseappmvvm.utils.SingleLiveEvent

class HomeViewModel(private val sharedPref: Preferences): ViewModel() {

    var goToAddActionScreen = SingleLiveEvent<Any>()
    var goToCurrencyConverterScreen = SingleLiveEvent<Any>()
    var goToLogout = SingleLiveEvent<Any>()

    fun onLogoutClick(){
        sharedPref.clear()
        goToLogout.call()
    }

    fun onAddActionClick(){
        goToAddActionScreen.call()
    }

    fun onCurrencyConverterClick(){
        goToCurrencyConverterScreen.call()
    }
}
package com.example.expenseappmvvm.screens.mainScreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.expenseappmvvm.data.database.repositories.UserRepository
import com.example.expenseappmvvm.data.rx.AppRxSchedulers
import com.example.expenseappmvvm.utils.Constants
import com.example.expenseappmvvm.utils.Preferences
import com.example.expenseappmvvm.utils.SingleLiveEvent
import com.example.expenseappmvvm.utils.disposeBy
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class HomeViewModel(
    private val sharedPref: Preferences,
    private val userRepository: UserRepository,
    private val rxSchedulers: AppRxSchedulers,
    private val compositeDisposable: CompositeDisposable
) : ViewModel() {

    var goToAddActionScreen = SingleLiveEvent<Any>()
    var goToCurrencyConverterScreen = SingleLiveEvent<Any>()
    var goToLogout = SingleLiveEvent<Any>()
    var goToBudgetFragment = SingleLiveEvent<Any>()
    var goToExpenseFragment = SingleLiveEvent<Any>()
    var isExpenseTabSelected = MutableLiveData<Boolean>().apply { value = false }

    val userName = MutableLiveData<String>().apply { value = Constants.EMPTY_STRING }

    fun onLogoutClick() {
        sharedPref.clear()
        goToLogout.call()
    }

    fun onAddActionClick() {
        goToAddActionScreen.call()
    }

    fun onCurrencyConverterClick() {
        goToCurrencyConverterScreen.call()
    }

    fun budgetTabOnClick() {
        isExpenseTabSelected.value = false
        goToBudgetFragment.call()
    }

    fun expenseTabOnClick() {
        isExpenseTabSelected.value = true
        goToExpenseFragment.call()
    }

    fun getUserName() {
        var userId: Long = 0
        if (sharedPref.hasKey(Constants.USER_ID)) {
            userId = sharedPref.read(Constants.USER_ID, userId)
        }

        return userRepository.getUserName(userId)
            .subscribeOn(rxSchedulers.background())
            .observeOn(rxSchedulers.androidUI())
            .subscribe({
                userName.value = it
            }, {
                Timber.e(it.localizedMessage)
            }).disposeBy(compositeDisposable)
    }
}
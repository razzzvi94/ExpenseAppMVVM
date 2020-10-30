package com.example.expenseappmvvm.screens.splashScreen

import androidx.lifecycle.ViewModel
import com.example.expenseappmvvm.data.rx.AppRxSchedulers
import com.example.expenseappmvvm.screens.loginScreen.LoginActivity
import com.example.expenseappmvvm.screens.mainScreen.HomeActivity
import com.example.expenseappmvvm.utils.Constants
import com.example.expenseappmvvm.utils.Preferences
import com.example.expenseappmvvm.utils.disposeBy
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class SplashViewModel(
    private val rxSchedulers: AppRxSchedulers,
    private val sharedPref: Preferences,
    private val compositeDisposable: CompositeDisposable
) : ViewModel() {

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun verifyUserLoggedIn(activity: SplashActivity) {
        Observable.timer(SPLASH_SCREEN_DURATION, TimeUnit.SECONDS)
            .observeOn(rxSchedulers.androidUI())
            .subscribe {
                if (sharedPref.hasKey(Constants.USER_ID)) {
                    HomeActivity.startHome(activity)
                } else {
                    LoginActivity.startLogin(activity)
                }
            }.disposeBy(compositeDisposable)
    }

    companion object {
        const val SPLASH_SCREEN_DURATION = 3L
    }
}

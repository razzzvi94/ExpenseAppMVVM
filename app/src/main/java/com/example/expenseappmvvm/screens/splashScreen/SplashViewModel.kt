package com.example.expenseappmvvm.screens.splashScreen

import android.os.Handler
import androidx.lifecycle.ViewModel
import com.example.expenseappmvvm.screens.loginScreen.LoginActivity

class SplashViewModel() : ViewModel() {
    fun goToLogin(activity: SplashActivity) {
        Handler().postDelayed({
            LoginActivity.startLogin(activity)
        }, 3000)
    }
}

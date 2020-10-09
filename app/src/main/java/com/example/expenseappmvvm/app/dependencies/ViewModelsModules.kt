package com.example.expenseappmvvm.app.dependencies

import com.example.expenseappmvvm.screens.loginScreen.LoginViewModel
import com.example.expenseappmvvm.screens.mainScreen.HomeViewModel
import com.example.expenseappmvvm.screens.splashScreen.SplashViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val viewModelsModule: Module = module {
    viewModel { SplashViewModel() }
    viewModel { HomeViewModel() }
    viewModel { LoginViewModel(get()) }
}
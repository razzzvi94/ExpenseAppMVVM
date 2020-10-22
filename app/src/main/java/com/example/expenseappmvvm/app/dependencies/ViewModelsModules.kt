package com.example.expenseappmvvm.app.dependencies

import com.example.expenseappmvvm.screens.addActionScreen.AddActionViewModel
import com.example.expenseappmvvm.screens.currencyConverterScreen.CurrencyConverterViewModel
import com.example.expenseappmvvm.screens.loginScreen.LoginViewModel
import com.example.expenseappmvvm.screens.mainScreen.HomeViewModel
import com.example.expenseappmvvm.screens.mainScreen.fragments.budget.BudgetFragmentViewModel
import com.example.expenseappmvvm.screens.splashScreen.SplashViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val viewModelsModule: Module = module {
    viewModel { SplashViewModel(get(), get(), get()) }
    viewModel { HomeViewModel(get(), get(), get(), get(), get(), get(), get()) }
    viewModel { LoginViewModel(get(), get(), get(), get(), get()) }
    viewModel { BudgetFragmentViewModel(get(), get(), get(), get()) }
    viewModel { AddActionViewModel(get(), get(), get(), get(), get()) }
    viewModel { CurrencyConverterViewModel(get(), get(), get(), get()) }
}
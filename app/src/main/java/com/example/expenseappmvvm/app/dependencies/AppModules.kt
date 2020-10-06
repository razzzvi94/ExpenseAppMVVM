package com.example.expenseappmvvm.app.dependencies

import io.reactivex.disposables.CompositeDisposable
import com.example.expenseappmvvm.data.rx.AppRxSchedulers
import org.koin.core.module.Module
import org.koin.dsl.module

val rxModules: Module = module {
    single { AppRxSchedulers() }
    factory { CompositeDisposable() }
}
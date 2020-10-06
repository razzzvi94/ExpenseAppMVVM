package com.example.expenseappmvvm.app

import android.app.Application
import com.example.expenseappmvvm.BuildConfig
import com.example.expenseappmvvm.data.model.prefModule
import com.facebook.stetho.Stetho
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import timber.log.Timber

class ExpenseApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@ExpenseApp)
            modules(listOf(prefModule))
        }
        Stetho.initializeWithDefaults(this)
        initTimber()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
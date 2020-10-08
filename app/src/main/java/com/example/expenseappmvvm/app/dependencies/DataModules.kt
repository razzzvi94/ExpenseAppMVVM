package com.example.expenseappmvvm.app.dependencies

import com.example.expenseappmvvm.data.database.RoomDB
import com.example.expenseappmvvm.data.database.repositories.UserRepository
import com.example.expenseappmvvm.utils.Preferences
import com.example.expenseappmvvm.utils.resourceUtils.ResourceUtils
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

val databaseModule: Module = module {
    single { RoomDB(androidContext()).appDatabase }
}

val preferencesModule: Module = module {
    single { Preferences(androidContext()) }
    single { ResourceUtils(androidContext()) }
}

val dataModules: Module = module {
    single { UserRepository(get()) }
}
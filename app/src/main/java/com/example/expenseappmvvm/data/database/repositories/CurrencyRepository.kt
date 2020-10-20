package com.example.expenseappmvvm.data.database.repositories

import com.example.expenseappmvvm.data.database.AppDatabase
import com.example.expenseappmvvm.data.database.entities.Currency
import io.reactivex.Single

class CurrencyRepository(private val db: AppDatabase) {
    fun insertCurrency(currency: Currency): Single<Long> {
        return db.currencyDao().insertCurrency(currency)
    }
}
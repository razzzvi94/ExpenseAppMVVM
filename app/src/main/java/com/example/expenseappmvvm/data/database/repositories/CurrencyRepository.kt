package com.example.expenseappmvvm.data.database.repositories

import com.example.expenseappmvvm.data.database.AppDatabase
import com.example.expenseappmvvm.data.database.entities.Currency
import io.reactivex.Maybe
import io.reactivex.Single

class CurrencyRepository(private val db: AppDatabase) {
    fun insertCurrency(currency: Currency): Single<Long> {
        return db.currencyDao().insertCurrency(currency)
    }

    fun getCurrencyDate(): Maybe<Long> {
        return db.currencyDao().getCurrencyDate()
    }

    fun getCurrency(currencyBase: String): Single<Currency> {
        return db.currencyDao().getCurrencyDB(currencyBase)
    }
}
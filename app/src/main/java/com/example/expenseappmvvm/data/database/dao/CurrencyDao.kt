package com.example.expenseappmvvm.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.expenseappmvvm.data.database.entities.Currency
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface CurrencyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrency(currency: Currency): Single<Long>

    @Query("SELECT currencyDate FROM currencies WHERE currencyId = 0")
    fun getCurrencyDate(): Maybe<Long>

    @Query("SELECT * FROM currencies WHERE currencyId = 1")
    fun getCurrencyDB(): Single<Currency>

    @Query("SELECT * FROM currencies WHERE currencyBase = :currencyBase")
    fun getMultiplier(currencyBase: String): Single<Currency>
}
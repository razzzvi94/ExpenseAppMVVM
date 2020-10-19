package com.example.expenseappmvvm.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.expenseappmvvm.data.database.entities.Currency
import io.reactivex.Single

@Dao
interface CurrencyDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertCurrency(currency: Currency): Single<Long>
}
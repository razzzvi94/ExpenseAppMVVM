package com.example.expenseappmvvm.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.expenseappmvvm.data.database.entities.Transaction
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateTransaction(transaction: Transaction): Single<Long>

    @Query("SELECT SUM(transactionAmount) FROM transactions WHERE NOT transactionCategory = 'Income' AND transactionDate BETWEEN :startDate AND :endDate AND userId=:userId GROUP BY :userId")
    fun getUserExpenseByDate(startDate: Long, endDate: Long, userId: Long): Maybe<Double>

    @Query("SELECT SUM(CASE WHEN transactionCategory = 'Income' then transactionAmount else -transactionAmount END) AS transactionAmount FROM transactions WHERE userId = :userId")
    fun getUserBalance(userId: Long): Maybe<Double>

    @Query("UPDATE transactions SET transactionAmount = transactionAmount * :multiplier WHERE userId = :userId")
    fun updateUserAmountPreferredCurrency(multiplier: Double, userId: Long): Single<Int>
}
package com.example.expenseappmvvm.data.database.repositories

import com.example.expenseappmvvm.data.database.AppDatabase
import com.example.expenseappmvvm.data.database.entities.Currency
import com.example.expenseappmvvm.data.database.entities.Transaction
import io.reactivex.Maybe
import io.reactivex.Single

class TransactionRepository(private val db: AppDatabase) {
    fun insertTransaction(transaction: Transaction): Single<Long> {
        return db.transactionDao().insertOrUpdateTransaction(transaction)
    }

    fun getExpenseByDate(startDate: Long, endDate: Long, userId: Long): Maybe<Double> {
        return db.transactionDao().getUserExpenseByDate(startDate, endDate, userId)
    }

    fun getUserBalance(userId: Long): Maybe<Double>{
        return db.transactionDao().getUserBalance(userId)
    }

    fun updateUserAmountCurrency(multiplier: Double, userId: Long): Single<Int>{
        return db.transactionDao().updateUserAmountPreferredCurrency(multiplier, userId)
    }

    fun getMultiplier(baseCurrency: String): Single<Currency>{
        return db.currencyDao().getMultiplier(baseCurrency)
    }
}
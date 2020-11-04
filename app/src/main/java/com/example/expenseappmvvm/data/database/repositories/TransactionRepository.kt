package com.example.expenseappmvvm.data.database.repositories

import com.example.expenseappmvvm.data.database.AppDatabase
import com.example.expenseappmvvm.data.database.entities.Currency
import com.example.expenseappmvvm.data.database.entities.Transaction
import io.reactivex.Observable
import io.reactivex.Single

class TransactionRepository(private val db: AppDatabase) {
    fun insertTransaction(transaction: Transaction): Single<Long> {
        return db.transactionDao().insertOrUpdateTransaction(transaction)
    }

    fun getExpenseByDate(startDate: Long, endDate: Long, userId: Long): Observable<Double> {
        return db.transactionDao().getUserExpenseByDate(startDate, endDate, userId)
    }

    fun getUserBalance(userId: Long): Observable<Double>{
        return db.transactionDao().getUserBalance(userId)
    }

    fun updateUserAmountCurrency(multiplier: Double, userId: Long): Single<Int>{
        return db.transactionDao().updateUserAmountPreferredCurrency(multiplier, userId)
    }

    fun getMultiplier(baseCurrency: String): Single<Currency>{
        return db.currencyDao().getMultiplier(baseCurrency)
    }

    fun getUserTransactionsDescending(userId: Long): Observable<MutableList<Transaction>>{
        return db.transactionDao().getUserTransactionsDescending(userId)
    }

    fun deleteTransactionById(incomeId: Long){
        return db.transactionDao().deleteTransactionById(incomeId)
    }

    fun editTransaction(id: Long, new_income: Double): Single<Int>{
        return db.transactionDao().editTransaction(id, new_income)
    }
}
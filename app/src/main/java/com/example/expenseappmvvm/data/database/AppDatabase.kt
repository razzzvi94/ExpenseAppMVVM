package com.example.expenseappmvvm.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.expenseappmvvm.data.database.dao.CurrencyDao
import com.example.expenseappmvvm.data.database.dao.TransactionDao
import com.example.expenseappmvvm.data.database.dao.UserDao
import com.example.expenseappmvvm.data.database.entities.Currency
import com.example.expenseappmvvm.data.database.entities.Transaction
import com.example.expenseappmvvm.data.database.entities.User

@Database(entities = [User::class, Transaction::class, Currency::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun transactionDao(): TransactionDao
    abstract fun currencyDao(): CurrencyDao
}
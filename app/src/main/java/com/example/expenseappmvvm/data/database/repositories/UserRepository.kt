package com.example.expenseappmvvm.data.database.repositories

import com.example.expenseappmvvm.data.database.AppDatabase
import com.example.expenseappmvvm.data.database.entities.User
import io.reactivex.Single

class UserRepository(private val db: AppDatabase) {
    fun registerUser(user: User):Single<Long>{
        return db.userDao().registerUser(user)
    }

    fun loginUser(userEmail: String, userPassword: String): Single<User>{
        return db.userDao().loginUser(userEmail, userPassword)
    }

    fun getUser(userId: Long): Single<User> {
        return db.userDao().getUser(userId)
    }

    fun preferredCurrency(currency: String, userId: Long): Single<Int>{
        return db.userDao().updateUserPreferredCurrency(currency, userId)
    }
}
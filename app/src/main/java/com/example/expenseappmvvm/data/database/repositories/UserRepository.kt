package com.example.expenseappmvvm.data.database.repositories

import com.example.expenseappmvvm.data.database.AppDatabase
import com.example.expenseappmvvm.data.database.entities.User
import io.reactivex.Single

class UserRepository(private val db: AppDatabase) {
    fun savePrimaryUser(user: User): Single<Unit> {
        return Single.just(db.userDao().saveUser(user))
    }

    fun getUserId(email: String): Single<Long> {
        return db.userDao().getUserIdByEmail(email)
    }
}
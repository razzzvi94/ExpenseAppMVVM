package com.example.expenseappmvvm.data.database.repositories

import com.example.expenseappmvvm.data.database.AppDatabase
import com.example.expenseappmvvm.data.database.entities.User
import io.reactivex.Flowable
import io.reactivex.Single

class UserRepository(private val db: AppDatabase) {
    fun savePrimaryUser(user: User): Flowable<Unit> {
        return Flowable.just(db.userDao().saveUser(user))
    }

    fun getUserId(email: String): Single<String> {
        return db.userDao().getUserIdByEmail(email)
    }
}
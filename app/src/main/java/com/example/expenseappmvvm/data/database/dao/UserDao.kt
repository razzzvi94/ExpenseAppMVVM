package com.example.expenseappmvvm.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.expenseappmvvm.data.database.entities.User
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE uid = :id ")
    fun getUserById(id: Long): Flowable<User>

    @Query("SELECT uid FROM users WHERE userEmail = :email")
    fun getUserIdByEmail(email: String): Single<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveUser(user: User)
}
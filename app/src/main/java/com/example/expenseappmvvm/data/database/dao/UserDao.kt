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
    @Query("SELECT * FROM users WHERE userName = :id ")
    fun getUserById(id: Long): Flowable<User>

    @Query("SELECT userId FROM users WHERE userEmail = :email")
    fun getUserIdByEmail(email: String): Single<Long>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun saveUser(user: User): Single<Long>
}
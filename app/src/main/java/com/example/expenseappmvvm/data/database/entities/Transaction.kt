package com.example.expenseappmvvm.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "Transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) var transactionId: Long = 0,
    @ForeignKey(entity = User::class, parentColumns = ["userId"], childColumns = ["userId"]) var userId: Long,
    @ColumnInfo(name = "transactionDate") var transactionDate: Long,
    @ColumnInfo(name = "transactionAmount") var transactionAmount: Double,
    @ColumnInfo(name = "transactionCategory") var transactionCategory: String,
    @ColumnInfo(name = "transactionDetails") var transactionDetails: String,
    @ColumnInfo(name = "transactionImage") var transactionImage: ByteArray
)
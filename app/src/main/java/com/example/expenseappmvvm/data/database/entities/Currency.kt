package com.example.expenseappmvvm.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Currencies")
data class Currency(
    @PrimaryKey(autoGenerate = false) var currencyId: Long = 0,
    @ColumnInfo(name = "currencyDate") var currencyDate: Long,
    @ColumnInfo(name = "currencyBase") var currencyBase: String,
    @ColumnInfo(name = "EUR") var EUR: Double,
    @ColumnInfo(name = "USD") var USD: Double,
    @ColumnInfo(name = "GBP") var GBP: Double,
    @ColumnInfo(name = "CHF") var CHF: Double,
    @ColumnInfo(name = "AUD") var AUD: Double,
)
package com.example.expenseappmvvm.screens.mainScreen.fragments.expense.adapter.models

data class RecyclerTransaction(
    val transactionId: Long,
    val date: Long,
    val amount: Double,
    val category: String,
    val name: String,
    var balance: Double,
    val details: String,
    var imageDetails: ByteArray
)
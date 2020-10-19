package com.example.expenseappmvvm.network

import com.example.expenseappmvvm.data.rest.CurrencyResponse
import io.reactivex.Observable
import retrofit2.http.GET

interface RestServiceInterface {
    @get:GET("latest?base=RON")
    val allCurrency: Observable<CurrencyResponse>
}
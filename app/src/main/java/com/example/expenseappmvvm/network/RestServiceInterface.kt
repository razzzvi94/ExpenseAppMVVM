package com.example.expenseappmvvm.network

import com.example.expenseappmvvm.data.rest.CurrencyResponse
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RestServiceInterface {
    @GET("latest")
    fun allCurrency(@Query("base") base: String): Observable<CurrencyResponse>
}
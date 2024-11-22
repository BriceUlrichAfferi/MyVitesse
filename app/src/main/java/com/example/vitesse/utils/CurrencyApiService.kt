package com.example.vitesse.utils

import retrofit2.Call
import retrofit2.http.GET

interface CurrencyApiService {
    @GET("https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/currencies/eur.json")
    fun getCurrencyRates(): Call<CurrencyResponse>
}

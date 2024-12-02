package com.example.vitesse.data.service.apiConversion

import retrofit2.http.GET

interface CurrencyApiService {
    @GET("currencies/eur.json")
    suspend fun getCurrencyRates(): CurrencyResponse
}


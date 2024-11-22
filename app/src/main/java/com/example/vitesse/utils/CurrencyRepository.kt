package com.example.vitesse.utils

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CurrencyRepository(private val currencyApiService: CurrencyApiService) {

    fun getCurrencyRates(): Call<CurrencyResponse> {
        return currencyApiService.getCurrencyRates()
    }
}



package com.example.vitesse.data.service.apiConversion

class CurrencyRepository(private val currencyApiService: CurrencyApiService) {

    suspend fun getCurrencyRates(): CurrencyResponse {
        return currencyApiService.getCurrencyRates()
    }
}




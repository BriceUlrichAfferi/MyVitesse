package com.example.vitesse.data.service.apiConversion

data class CurrencyResponse(
    val date: String,
    val eur: CurrencyRates
)

data class CurrencyRates(
    val gbp: Double
)


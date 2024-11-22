package com.example.vitesse.utils

data class CurrencyResponse(
    val date: String,
    val eur: CurrencyRates
)

data class CurrencyRates(
    val gbp: Double // Only GBP and EUR are needed, but you can add others if needed
)


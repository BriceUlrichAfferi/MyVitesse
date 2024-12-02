package com.example.vitesse.data.service.apiConversion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CurrencyViewModelFactory(private val repository: CurrencyRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CurrencyViewModel::class.java)) {
            CurrencyViewModel(repository) as T  // Pass repository to the ViewModel
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

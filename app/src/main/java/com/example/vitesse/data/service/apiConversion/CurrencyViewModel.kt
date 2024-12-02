package com.example.vitesse.data.service.apiConversion

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class CurrencyViewModel(private val repository: CurrencyRepository) : ViewModel() {

    private val _currencyRates = MutableLiveData<Double>()
    val currencyRates: LiveData<Double> get() = _currencyRates

    private val _convertedSalary = MutableLiveData<String>()
    val convertedSalary: LiveData<String> get() = _convertedSalary

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error
    fun fetchCurrencyRates() {
        viewModelScope.launch {
            try {
                val response = repository.getCurrencyRates()
                val gbpRate = response.eur.gbp
                if (gbpRate > 0) {
                    _currencyRates.value = gbpRate
                } else {
                    _error.value = "Currency rate data is invalid"
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
    @SuppressLint("DefaultLocale")
    fun convertSalaryToPounds(euros: Double) {
        _currencyRates.value?.let { gbpRate ->
            val pounds = euros * gbpRate
            _convertedSalary.value = "Soit £ ${String.format("%.2f", pounds)}"
        } ?: run {
            _error.value = "Conversion de devises en cours"
        }
    }
}


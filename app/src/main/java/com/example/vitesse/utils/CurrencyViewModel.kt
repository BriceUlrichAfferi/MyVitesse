package com.example.vitesse.utils

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CurrencyViewModel(private val repository: CurrencyRepository) : ViewModel() {

    private val _currencyRates = MutableLiveData<Double>()
    val currencyRates: LiveData<Double> get() = _currencyRates

    private val _convertedSalary = MutableLiveData<String>()
    val convertedSalary: LiveData<String> get() = _convertedSalary

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchCurrencyRates() {
        repository.getCurrencyRates().enqueue(object : Callback<CurrencyResponse> {
            override fun onResponse(call: Call<CurrencyResponse>, response: Response<CurrencyResponse>) {
                if (response.isSuccessful) {
                    val gbpRate = response.body()?.eur?.gbp ?: 0.0
                    if (gbpRate > 0) {
                        _currencyRates.value = gbpRate
                        Log.d("CurrencyViewModel", "Fetched GBP rate: $gbpRate")
                    } else {
                        _error.value = "Currency rate data is invalid"
                        Log.d("CurrencyViewModel", "Invalid GBP rate fetched")
                    }
                } else {
                    _error.value = "Failed to fetch data"
                    Log.d("CurrencyViewModel", "API request failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<CurrencyResponse>, t: Throwable) {
                _error.value = t.message
                Log.d("CurrencyViewModel", "API call failed: ${t.message}")
            }
        })
    }

    fun convertSalaryToPounds(euros: Double) {
        _currencyRates.value?.let { gbpRate ->
            val pounds = euros * gbpRate
            _convertedSalary.value = "Soit £ ${String.format("%.2f", pounds)}"
            Log.d("CurrencyViewModel", "Converted salary: £${String.format("%.2f", pounds)}")
        } ?: run {
            _error.value = "Conversion de devises en cours"
            Log.d("CurrencyViewModel", "Currency rate not available")
        }
    }
}

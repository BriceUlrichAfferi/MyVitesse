package com.example.vitesse

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.vitesse.data.service.CandidatApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/*class CandidatViewModel @Inject constructor(
    private val candidatApi: CandidatApi
) {
    // Fetch candidates from the API
    suspend fun fetchCandidats(): List<Candidat> {
        return candidatApi.getCandidat()
    }
}*/
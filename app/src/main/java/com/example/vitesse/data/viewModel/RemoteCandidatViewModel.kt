package com.example.vitesse.data.viewModel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vitesse.data.repository.RemoteCandidatRepository
import com.example.vitesse.model.Candidate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RemoteCandidatViewModel @Inject constructor(
    private val remoteCandidatRepository: RemoteCandidatRepository
) : ViewModel() {

    private val _remoteCandidats = MutableLiveData<List<Candidate>>()
    val remoteCandidats: LiveData<List<Candidate>> get() = _remoteCandidats

    fun fetchCandidats() {
        viewModelScope.launch {
            try {
                val candidats = remoteCandidatRepository.fetchCandidats()  // Fetch data from the API
                Log.d("RemoteCandidatViewModel", "Fetched from Fake API: $candidats")
                _remoteCandidats.value = candidats  // Update LiveData with fetched candidates
            } catch (e: Exception) {
                Log.e("RemoteCandidatViewModel", "Error fetching candidats: ${e.message}")
            }
        }
    }


    // Function to add a candidat to the API
    // Function to add a candidat to the API through the repository
    fun addCandidatToApi(candidate: Candidate) {
        viewModelScope.launch {
            try {
                remoteCandidatRepository.addCandidat(candidate)// Call the repository method
                Log.d("RemoteCandidatViewModel", "Candidat added to API: $candidate")
            } catch (e: Exception) {
                Log.e("RemoteCandidatViewModel", "Error adding candidat to API: ${e.message}")
            }
        }
    }
}
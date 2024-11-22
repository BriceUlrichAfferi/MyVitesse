package com.example.vitesse

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vitesse.data.repository.LocalCandidatRepository
import com.example.vitesse.data.repository.RemoteCandidatRepository
import com.example.vitesse.model.Candidate
import com.example.vitesse.usecase.AddCandidateUseCase
import com.example.vitesse.usecase.DeleteCandidateUseCase
import com.example.vitesse.usecase.GetAllCandidatesUseCase
import com.example.vitesse.usecase.UpdateCandidateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val addCandidateUseCase: AddCandidateUseCase,
    private val deleteCandidateUseCase: DeleteCandidateUseCase,
    private val getAllCandidatesUseCase: GetAllCandidatesUseCase,
    private val updateCandidateUseCase: UpdateCandidateUseCase,
    private val localCandidatRepository: LocalCandidatRepository

) : ViewModel() {

    private val _favorites = MutableStateFlow<List<Candidate>>(emptyList())
    val favorites: StateFlow<List<Candidate>> get() = _favorites

    private val _selectedCandidat = MutableStateFlow<Candidate?>(null)
    val selectedCandidat: StateFlow<Candidate?> get() = _selectedCandidat

    // Initialize favorites by loading from local database
    // Initialize favorites by loading from local database
    init {
        // Collect flow to update _favorites
        viewModelScope.launch {
            getAllCandidatesUseCase.execute().collect { candidates ->
                // Filter favorites and update _favorites based on initial data
                val currentFavorites = candidates.filter { it.isFavorite }
                _favorites.value = currentFavorites
            }
        }
    }
   /* init     */


    fun isFavorite(candidate: Candidate): Boolean {
        return _favorites.value.contains(candidate)
    }

    fun toggleFavorite(candidate: Candidate) { val currentFavorites = _favorites.value.toMutableList()

        if (currentFavorites.contains(candidate)) {
            // Remove from favorites
            currentFavorites.remove(candidate)
            candidate.isFavorite = false // Update candidate's isFavorite flag
        } else {
            // Add to favorites
            currentFavorites.add(candidate)
            candidate.isFavorite = true // Update candidate's isFavorite flag
        }

        // Update _favorites directly for immediate UI change
        _favorites.value = currentFavorites.toList()

        // Persist favorite status change in local database
        viewModelScope.launch {
            localCandidatRepository.updateCandidat(candidate) // Update locally
        }
    }

    fun updateFavoriteStatus(candidate: Candidate) {
        viewModelScope.launch {
            // Call to update the candidate's favorite status in the local database
            if (isFavorite(candidate)) {
                // Add to local database (e.g., using localCandidatRepository)
                //localCandidatRepository.insertCandidat(candidate)
                getAllCandidatesUseCase.execute()

            } else {
                // Remove from local database (e.g., using localCandidatRepository)
                // Update favorite status without deleting the candidate
                //localCandidatRepository.getLocalCandidats()
                getAllCandidatesUseCase.execute()

            }

            // Optionally, update remote server if needed
            // remoteCandidatRepository.updateFavoriteStatus(candidate)
        }
    }

    fun setSelectedCandidat(candidate: Candidate) {
        _selectedCandidat.value = candidate
    }

    // Add the selected candidate to the favorites list from DetailsFragment
    fun addToFavorites(candidate: Candidate) {
        // Set isFavorite to false when adding
        val updatedCandidate = candidate.copy(isFavorite = false)

        val updatedFavorites = _favorites.value.toMutableList()
        if (!updatedFavorites.contains(updatedCandidate)) {
            updatedFavorites.add(updatedCandidate)
            _favorites.value = updatedFavorites

            // Insert into the local database with updated isFavorite status
            viewModelScope.launch {
                addCandidateUseCase.execute(updatedCandidate)  // Insert with isFavorite = false
            }
        }
    }


    fun deleteCandidat(candidate: Candidate) {
        viewModelScope.launch {
            // Delete from Room (local database)
            deleteCandidateUseCase.execute(candidate)

            // Update StateFlow after removal
            val currentList = _favorites.value.toMutableList()
            currentList.remove(candidate)
            _favorites.value = currentList
        }
    }
}

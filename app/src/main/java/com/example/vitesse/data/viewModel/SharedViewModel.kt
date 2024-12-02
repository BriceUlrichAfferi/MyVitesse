package com.example.vitesse.data.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vitesse.model.Candidate
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
    private val deleteCandidateUseCase: DeleteCandidateUseCase,
    private val getAllCandidatesUseCase: GetAllCandidatesUseCase,
    private val updateCandidateUseCase: UpdateCandidateUseCase,

) : ViewModel() {

    private val _favorites = MutableStateFlow<List<Candidate>>(emptyList())
    val favorites: StateFlow<List<Candidate>> get() = _favorites

    private val _selectedCandidat = MutableStateFlow<Candidate?>(null)
    val selectedCandidat: StateFlow<Candidate?> get() = _selectedCandidat

    // Initialize favorites by loading from local database
    init {
        viewModelScope.launch {
            getAllCandidatesUseCase.execute().collect { candidates ->
                // Filter favorites and update _favorites based on initial data
                val currentFavorites = candidates.filter { it.isFavorite }
                _favorites.value = currentFavorites
            }
        }
    }
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
            updateCandidateUseCase.execute(candidate)
        }
    }
        fun setSelectedCandidat(candidate: Candidate) {
        _selectedCandidat.value = candidate
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

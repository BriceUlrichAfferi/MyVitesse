package com.example.vitesse.data.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vitesse.adapters.room.CandidateDtoDao
import com.example.vitesse.adapters.room.mapper.CandidateMapper
import com.example.vitesse.candidats.CandidateAction
import com.example.vitesse.model.Candidate
import com.example.vitesse.usecase.AddCandidateUseCase
import com.example.vitesse.usecase.DeleteCandidateUseCase
import com.example.vitesse.usecase.GetAllCandidatesUseCase
import com.example.vitesse.usecase.UpdateCandidateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LocalCandidatViewModel @Inject constructor(
    private val addCandidateUseCase: AddCandidateUseCase,
    private val deleteCandidateUseCase: DeleteCandidateUseCase,
    private val getAllCandidatesUseCase: GetAllCandidatesUseCase,
    private val updateCandidateUseCase: UpdateCandidateUseCase
) : ViewModel() {

    // Use a StateFlow to hold the list of candidates
    private val _candidateFlow = MutableStateFlow<List<Candidate>>(emptyList())
    val candidateFlow: StateFlow<List<Candidate>> = _candidateFlow.asStateFlow()

    private val _favorites = MutableStateFlow<List<Candidate>>(emptyList())
    val favorites: StateFlow<List<Candidate>> = _favorites.asStateFlow()

    private val _selectedCandidat = MutableStateFlow<Candidate?>(null)
    val selectedCandidat: StateFlow<Candidate?> get() = _selectedCandidat



    init {

        performAction(CandidateAction.LoadAll)
    }
    // Function to fetch candidate by ID



    fun performAction(action: CandidateAction) {
        viewModelScope.launch(Dispatchers.IO){
            when (action){
                is CandidateAction.LoadAll -> {
                    // Collect the Flow and set the list of candidates
                    getAllCandidatesUseCase.execute().collect { candidates ->
                        _candidateFlow.value = candidates
                    }
                }
                is CandidateAction.AddCandidate -> {
                    addCandidateUseCase.execute(action.candidate)
                    performAction(CandidateAction.LoadAll)
                }
                is CandidateAction.DeleteCandidate -> {
                    deleteCandidateUseCase.execute(action.candidate)
                    performAction(CandidateAction.LoadAll)
                }

                is CandidateAction.UpdateCandidate -> {
                    updateCandidateUseCase.execute(action.candidate)
                    performAction(CandidateAction.LoadAll)
                }
            }

        }
    }





    fun selectedCandidat (candidate: Candidate){
        _selectedCandidat.value = candidate
    }
}
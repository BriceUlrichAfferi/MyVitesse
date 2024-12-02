package com.example.vitesse.data.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vitesse.utils.CandidateAction
import com.example.vitesse.model.Candidate
import com.example.vitesse.usecase.AddCandidateUseCase
import com.example.vitesse.usecase.DeleteCandidateUseCase
import com.example.vitesse.usecase.GetAllCandidatesUseCase
import com.example.vitesse.usecase.UpdateCandidateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LocalCandidatViewModel @Inject constructor(
    private val addCandidateUseCase: AddCandidateUseCase,
    private val deleteCandidateUseCase: DeleteCandidateUseCase,
    private val getAllCandidatesUseCase: GetAllCandidatesUseCase,
    private val updateCandidateUseCase: UpdateCandidateUseCase
) : ViewModel() {

    private val _candidateFlow = MutableStateFlow<List<Candidate>>(emptyList())
    val candidateFlow: StateFlow<List<Candidate>> = _candidateFlow.asStateFlow()

    init {

        performAction(CandidateAction.LoadAll)
    }

    fun performAction(action: CandidateAction) {
        viewModelScope.launch(Dispatchers.IO){
            when (action){
                is CandidateAction.LoadAll -> {
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
}
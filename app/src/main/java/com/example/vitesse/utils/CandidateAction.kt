package com.example.vitesse.utils

import com.example.vitesse.model.Candidate

sealed interface CandidateAction {
    object LoadAll : CandidateAction
    data class AddCandidate(val candidate: Candidate): CandidateAction
    data class UpdateCandidate(val candidate: Candidate): CandidateAction
    data class DeleteCandidate(val candidate: Candidate): CandidateAction
}
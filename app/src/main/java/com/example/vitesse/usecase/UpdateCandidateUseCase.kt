package com.example.vitesse.usecase

import com.example.vitesse.data.repository.LocalCandidatRepository
import com.example.vitesse.model.Candidate
import javax.inject.Inject

class UpdateCandidateUseCase @Inject constructor (private val localCandidatRepository: LocalCandidatRepository) {

    suspend fun execute (candidate: Candidate) {
        localCandidatRepository.updateCandidat(candidate)
    }

}
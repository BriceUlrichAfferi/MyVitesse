package com.example.vitesse.usecase

import com.example.vitesse.data.repository.LocalCandidatRepository
import com.example.vitesse.model.Candidate
import javax.inject.Inject

class DeleteCandidateUseCase @Inject constructor(private val localCandidatRepository: LocalCandidatRepository) {
    suspend fun execute(candidate: Candidate){
        localCandidatRepository.deleteCandidat(candidate)
    }

    suspend fun execute(candidatId: Int){
        localCandidatRepository.deleteCandidatById(candidatId)

    }

}
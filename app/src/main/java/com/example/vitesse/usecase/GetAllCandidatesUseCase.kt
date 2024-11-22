package com.example.vitesse.usecase

import com.example.vitesse.data.repository.LocalCandidatRepository
import com.example.vitesse.model.Candidate
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllCandidatesUseCase @Inject constructor( private val localCandidatRepository: LocalCandidatRepository) {

    suspend fun execute(): Flow<List<Candidate>> {
        return localCandidatRepository.getLocalCandidats()
    }

}
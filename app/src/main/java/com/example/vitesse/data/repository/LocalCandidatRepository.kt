package com.example.vitesse.data.repository
import com.example.vitesse.room.CandidateDtoDao
import com.example.vitesse.mapper.CandidateMapper
import com.example.vitesse.model.Candidate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalCandidatRepository (
    private val candidateDtoDao: CandidateDtoDao
) {
    // Retrieve candidates from the local Room database as Flow for real-time updates
    fun getLocalCandidats(): Flow<List<Candidate>> {
        return candidateDtoDao.getAllCandidates() // This should return a Flow<List<CandidateDto>>
            .map { candidateDtos -> candidateDtos.map { CandidateMapper.fromDto(it) } }
    }

    // Insert a new candidate into the database if it doesn't already exist and the identifiant is not empty
    suspend fun insertCandidat(candidate: Candidate) {
        if (candidate.email.isNotEmpty()) {
            val existingCandidat = candidateDtoDao.getCandidateByEmail(candidate.email)
            if (existingCandidat == null) {

                // Ensure the candidate is not marked as favorite before inserting
                val candidateToInsert = candidate.copy(isFavorite = false)

                CandidateMapper.toDto(candidateToInsert).let { candidateDtoDao.insert(it) }
            }
        }
    }
    // Update an existing candidate in the database
    suspend fun updateCandidat(candidate: Candidate) {
        val candidateDto = CandidateMapper.toDto(candidate)
        candidateDtoDao.updateCandidate(candidateDto)
    }
    // Delete an existing candidate from the database
    suspend fun deleteCandidat(candidate: Candidate) {
        val candidateDto = CandidateMapper.toDto(candidate)
        candidateDtoDao.deleteCandidate(candidateDto)
    }
    suspend fun deleteCandidatById(candidatId: Int) {
        candidateDtoDao.deleteCandidateById(candidatId)
    }
}

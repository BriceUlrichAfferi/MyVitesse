package com.example.vitesse.data.repository

import android.util.Log
import com.example.vitesse.data.service.CandidatApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/*class CandidatRepository @Inject constructor(
    private val candidatApi: CandidatApi,
    private val candidatDao: CandidatDao
) {

    suspend fun fetchCandidats(): List<Candidat> {
        return candidatApi.getCandidat()
    }

    suspend fun getLocalCandidats(): Flow<List<Candidat>> {
        return candidatDao.getAllCandidats()
    }

    suspend fun insertCandidat(candidat: Candidat) {
        // Check if the candidate already exists
        val existingCandidat = candidatDao.getCandidatByIdentifiant(candidat.identifiant)
        if (existingCandidat == null) {
            candidatDao.insert(candidat)
            Log.d("CandidatRepository", "Inserted candidat: $candidat")
        } else {
            Log.d("CandidatRepository", "Candidate already exists: ${candidat.identifiant}")
        }
    }
}*/

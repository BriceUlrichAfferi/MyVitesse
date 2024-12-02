package com.example.vitesse.data.repository


import com.example.vitesse.data.service.fakeApi.CandidatApi
import com.example.vitesse.model.Candidate
import javax.inject.Inject

class RemoteCandidatRepository @Inject constructor(
    private val candidatApi: CandidatApi
) {
    // Fetch candidates from the API
    suspend fun fetchCandidats(): List<Candidate> {
        return candidatApi.getCandidat()
    }

    // Add this method to add a candidate to the API
    suspend fun addCandidat(candidate: Candidate) {
        candidatApi.addCandidat(candidate) // Call the API's addCandidat method
    }

}

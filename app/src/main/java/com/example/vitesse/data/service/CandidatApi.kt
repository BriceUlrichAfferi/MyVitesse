package com.example.vitesse.data.service

import com.example.vitesse.model.Candidate

interface CandidatApi {
   suspend fun getCandidat(): List<Candidate>
   suspend fun addCandidat(candidate: Candidate) // Add this method

}

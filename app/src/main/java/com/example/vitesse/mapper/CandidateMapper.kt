package com.example.vitesse.mapper

import com.example.vitesse.entity.CandidateDto
import com.example.vitesse.model.Candidate

object CandidateMapper {
    fun fromDto(candidateDto: CandidateDto): Candidate{
        return Candidate(
            id = candidateDto.id,
            picture = candidateDto.picture,
            note = candidateDto.identifiant,
            description = candidateDto.description,
            firstName = candidateDto.firstName,
            lastName = candidateDto.lastName,
            phone = candidateDto.phone,
            email = candidateDto.email,
            anivDat = candidateDto.anivDate,
            isFavorite = candidateDto.isFavorite

        )
    }
    fun toDto(candidate: Candidate): CandidateDto{
        return CandidateDto(
            id = candidate.id,
            picture = candidate.picture,
            identifiant = candidate.note,
            description = candidate.description,
            firstName = candidate.firstName,
            lastName = candidate.lastName,
            phone = candidate.phone,
            email = candidate.email,
            anivDate = candidate.anivDat,
            isFavorite = candidate.isFavorite
        )
    }
}
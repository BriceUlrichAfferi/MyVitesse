package com.example.vitesse.adapters.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import com.example.vitesse.entity.CandidateDto
import kotlinx.coroutines.flow.Flow

@Dao
interface CandidateDtoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(candidate: CandidateDto): Long

    @Query("SELECT * FROM candidate WHERE email = :email LIMIT 1")
    suspend fun getCandidateByEmail(email: String): CandidateDto?

    @Query("SELECT * FROM candidate")
    fun getAllCandidates(): Flow<List<CandidateDto>>

    @Update
    suspend fun updateCandidate(candidate: CandidateDto) // Update uses CandidateDto

    @Delete
    suspend fun deleteCandidate(candidate: CandidateDto) // Delete uses CandidateDto

    @Query("DELETE FROM candidate WHERE id = :candidateId")
    suspend fun deleteCandidateById(candidateId: Int)

    @Query("SELECT * FROM candidate WHERE favoris = 1")  // Using `favoris` (favorite) field
    fun getAllFavorites(): Flow<List<CandidateDto>>

}

package com.example.vitesse.adapters.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/*@Dao
interface CandidatDao {

    // Insert a Candidat entity instead of a String
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(candidat: Candidat): Long

    @Query("SELECT * FROM candidat_table WHERE email = :email LIMIT 1")
    suspend fun getCandidatByEmail(email: String): Candidat?


    @Query("SELECT * FROM candidat_table")
    fun getAllCandidats(): Flow<List<Candidat>>

    @Update
    suspend fun updateCandidat(candidat: Candidat)

    @Delete
    suspend fun deleteCandidat(candidat: Candidat)

    // Alternatively, delete by ID if that fits your model better
    @Query("DELETE FROM candidat_table WHERE id = :candidatId")
    suspend fun deleteCandidatById(candidatId: Int)

    @Query("SELECT * FROM candidat_table")
     fun getAllFavorites(): Flow<List<Candidat>>


}*/

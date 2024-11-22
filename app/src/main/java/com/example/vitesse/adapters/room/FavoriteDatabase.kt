package com.example.vitesse.adapters.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.vitesse.entity.CandidateDto

@Database(entities = [CandidateDto::class], version = 1)
abstract class FavoriteDatabase : RoomDatabase() {
    abstract fun candidateDtoDao(): CandidateDtoDao
}
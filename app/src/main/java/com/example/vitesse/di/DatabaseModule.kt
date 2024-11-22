package com.example.vitesse.di

import android.content.Context
import androidx.room.Room
import com.example.vitesse.adapters.room.AppDatabase
import com.example.vitesse.adapters.room.CandidateDtoDao
import com.example.vitesse.adapters.room.DatabaseMigrations // If you have migrations
import com.example.vitesse.entity.CandidateDto
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "candidat_database"
        )
            .addMigrations(DatabaseMigrations.MIGRATION_1_2) // Add your migrations here if needed
            .fallbackToDestructiveMigration() // Optional: allows destructive migration
            .build()
    }

    @Provides
    @Singleton
    fun provideCandidatDao(appDatabase: AppDatabase): CandidateDtoDao {
        return appDatabase.candidateDtoDao()  // Assuming you have this method in your database
    }
}

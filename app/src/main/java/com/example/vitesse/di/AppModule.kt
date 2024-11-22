package com.example.vitesse.di

import android.app.Application
import android.content.Context
import com.example.vitesse.adapters.room.CandidateDtoDao
import com.example.vitesse.data.repository.LocalCandidatRepository
import com.example.vitesse.data.repository.RemoteCandidatRepository
import com.example.vitesse.data.service.CandidatApi
import com.example.vitesse.data.service.CandidatsfakeApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCandidatApi(): CandidatApi {
        return CandidatsfakeApi() // Provide your fake API
    }


    @Provides
    @Singleton
    fun provideContext(app: Application): Context {
        return app.applicationContext // Provide application context
    }

    @Provides
    @Singleton
    fun provideCandidatRepository(api: CandidatApi): RemoteCandidatRepository {
        return RemoteCandidatRepository(api) // Pass the dao to the repository
    }

    @Provides
    @Singleton
    fun localCandidatRepository(candidateDtoDao: CandidateDtoDao): LocalCandidatRepository {
        return LocalCandidatRepository(candidateDtoDao) // Pass the dao to the repository
    }


}

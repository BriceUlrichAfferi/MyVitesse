package com.example.vitesse.adapters.room.mapper

import android.content.Context
import androidx.room.Room
import com.example.vitesse.adapters.room.FavoriteDatabase

object DatabaseManager {

    private var instance: FavoriteDatabase? = null

    fun getDatabase(context: Context): FavoriteDatabase {
        if (instance == null) {
            synchronized(FavoriteDatabase::class.java) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FavoriteDatabase::class.java,
                        "favorite_database"
                    ).build()
                }
            }
        }
        return instance!!
    }
}

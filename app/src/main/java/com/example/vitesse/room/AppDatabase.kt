package com.example.vitesse.room


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.vitesse.entity.CandidateDto

                                                // Increment the version
@Database(entities = [ CandidateDto::class], version = 7, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    //abstract fun candidatDao(): CandidatDao
    abstract fun candidateDtoDao(): CandidateDtoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database.db" // Unique database name
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }


}



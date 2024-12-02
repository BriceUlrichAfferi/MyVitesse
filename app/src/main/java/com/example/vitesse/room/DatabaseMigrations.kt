package com.example.vitesse.room

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseMigrations {
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Add your migration logic here
            database.execSQL("ALTER TABLE candidat_table ADD COLUMN new_column_name TEXT")
        }
    }
}

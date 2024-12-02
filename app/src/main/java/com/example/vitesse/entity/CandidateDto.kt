package com.example.vitesse.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "candidate")
data class CandidateDto(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "picture")
    val picture: String = "",

    @ColumnInfo(name = "identifiant")
    val identifiant: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "first_name")
    val firstName: String,

    @ColumnInfo(name = "last_name")
    val lastName: String,

    @ColumnInfo(name = "phone")
    val phone: String,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "anniversaire")
    val anivDate: String,

    @ColumnInfo(name = "favoris")
    val isFavorite: Boolean = false
)

package com.example.vitesse.model

data class Candidate(
    val id: Int = 0,
    val picture: String = "",
    val note: String,
    val description: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val email: String,
    val anivDat: String,
    var isFavorite: Boolean = false
)
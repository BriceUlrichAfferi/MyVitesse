package com.example.vitesse.model

data class Candidate(
    val id: Int = 0, // Default to 0 if it’s a new candidate without an ID
    val picture: String = "", // Default empty string
    val note: String,
    val description: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val email: String,
    val anivDat: String,
    var isFavorite: Boolean = false
)
package com.example.vitesse.utils

import androidx.room.Query

interface FilterableCandidates {
    fun filterCandidates (query: String)
}
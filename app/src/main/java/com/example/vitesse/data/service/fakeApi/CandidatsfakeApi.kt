package com.example.vitesse.data.service.fakeApi

import com.example.vitesse.model.Candidate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class CandidatsfakeApi : CandidatApi {

    // Override the getCandidat method from the CandidatApi interface
    override suspend fun getCandidat(): List<Candidate> {
        return withContext(Dispatchers.IO) {
            // Simulate a network delay
            delay(200)

            // Return a list of fake Candidate objects
            listOf(
                Candidate(
                    id = 1,  // Assign a unique ID to each candidate
                    picture = "https://xsgames.co/randomusers/assets/avatars/male/71.jpg",  // Assuming picture is a String URL
                    note = "John Doe",
                    description = "Here I am",
                    firstName = "john",
                    lastName = "DOE",
                    phone = "07689671",
                    email = "jody@yahoo.com",
                    anivDat = "17/05/1989",
                    isFavorite = false
                ),
                Candidate(
                    id = 2,  // Assign a unique ID
                    picture = "https://xsgames.co/randomusers/assets/avatars/female/32.jpg",
                    note = "Marie Dominique AGRE",
                    description = "Hello, I'm Jane",
                    firstName = "john",
                    lastName = "Marie",
                    phone = "07689671",
                    email = "jody@yahoo.com",
                    anivDat = "17/05/1989",
                    isFavorite = false
                )
            )
        }
    }

    // New method to add a candidate
    override suspend fun addCandidat(candidat: Candidate) {
        withContext(Dispatchers.IO) {
            delay(200) // Simulate API delay
            // Log the candidate added (just for the fake API)
            println("Candidate added to fake API: ${candidat.firstName} ${candidat.lastName}")
        }
    }
}

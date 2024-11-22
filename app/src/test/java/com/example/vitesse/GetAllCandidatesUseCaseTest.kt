package com.example.vitesse

import com.example.vitesse.data.repository.LocalCandidatRepository
import com.example.vitesse.model.Candidate
import com.example.vitesse.usecase.GetAllCandidatesUseCase
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import kotlinx.coroutines.flow.first

@RunWith(JUnit4::class)
class GetAllCandidatesUseCaseTest {

    @Mock
    private lateinit var localCandidatRepository: LocalCandidatRepository

    private lateinit var getAllCandidatesUseCase: GetAllCandidatesUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        getAllCandidatesUseCase = GetAllCandidatesUseCase(localCandidatRepository)
    }

    @After
    fun tearDown() {
        Mockito.framework().clearInlineMocks()
    }


    @Test
    fun `when repository returns candidates, use case should return them`() = runBlocking {
        // Arrange
        val fakeCandidates = listOf(
            Candidate(
                id = 1,
                picture = "https://example.com/picture1.jpg",
                identifiant = "CAND123",
                description = "Software Engineer",
                firstName = "John",
                lastName = "Doe",
                phone = "1234567890",
                email = "johndoe@example.com",
                anivDate = "1990-01-01",
                isFavorite = true
            ),
            Candidate(
                id = 2,
                picture = "https://example.com/picture2.jpg",
                identifiant = "CAND124",
                description = "Data Scientist",
                firstName = "Alice",
                lastName = "Smith",
                phone = "1122334455",
                email = "alice.smith@example.com",
                anivDate = "1998-12-12",
                isFavorite = false
            )
        )
        Mockito.`when`(localCandidatRepository.getLocalCandidats()).thenReturn(flowOf(fakeCandidates))

        // Act
        val result = getAllCandidatesUseCase.execute().first() // Collect the Flow to get the List

        // Assert
        assertEquals(fakeCandidates, result)
    }


    @Test
    fun `when repository returns empty list, use case should return empty list`() = runBlocking {
        // Arrange
        Mockito.`when`(localCandidatRepository.getLocalCandidats()).thenReturn(flowOf(emptyList()))

        // Act
        val result = getAllCandidatesUseCase.execute().first() // Collect the flow to get the list

        // Assert
        assertTrue(result.isEmpty()) // Now 'result' is a List, so you can use isEmpty()
    }
}
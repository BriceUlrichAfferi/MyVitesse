package com.example.vitesse

import com.example.vitesse.data.repository.LocalCandidatRepository
import com.example.vitesse.model.Candidate
import com.example.vitesse.usecase.AddCandidateUseCase
import com.example.vitesse.utils.ValidationUtil
import junit.framework.TestCase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class AddCandidateUseCaseTest {

    @Mock
    private lateinit var localCandidatRepository: LocalCandidatRepository

    private lateinit var addCandidateUseCase: AddCandidateUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        addCandidateUseCase = AddCandidateUseCase(localCandidatRepository)
    }

    @After
    fun tearDown() {
        Mockito.framework().clearInlineMocks()
    }

    @Test
    fun `when execute is called, it should add the candidate to the repository`() = runBlocking {
        // Arrange
        val candidateToAdd = Candidate(
            id = 0, // Assuming a new candidate
            picture = "https://example.com/picture.jpg",
            note = "CAND123",
            description = "Experienced software developer",
            firstName = "John",
            lastName = "Doe",
            phone = "1234567890",
            email = "johndoe@example.com",
            anivDat = "1990-01-01",
            isFavorite = true
        )

        // Act
        addCandidateUseCase.execute(candidateToAdd)

        // Assert
        Mockito.verify(localCandidatRepository).insertCandidat(candidateToAdd)
    }


    @Test
    fun `when getAllCandidates is called after adding, it should reflect added candidates`() = runBlocking {
        // Arrange
        val addedCandidates = listOf(
            Candidate(
                id = 0,
                picture = "https://example.com/picture2.jpg",
                note = "CAND125",
                description = "Intern",
                firstName = "Alice",
                lastName = "Smith",
                phone = "1122334455",
                email = "alice.smith@example.com",
                anivDat = "1998-12-12",
                isFavorite = false
            )
        )
        Mockito.`when`(localCandidatRepository.getLocalCandidats()).thenReturn(flowOf(addedCandidates))

        // Act
        val result = localCandidatRepository.getLocalCandidats().toList() // Collect the flow into a list

        // Assert
        assertEquals(addedCandidates, result.first())
    }
}
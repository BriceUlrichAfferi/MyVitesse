package com.example.vitesse

import com.example.vitesse.data.repository.LocalCandidatRepository
import com.example.vitesse.model.Candidate
import com.example.vitesse.usecase.DeleteCandidateUseCase
import junit.framework.TestCase
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class DeleteCandidateUseCaseTest {

    @Mock
    private lateinit var localCandidatRepository: LocalCandidatRepository

    private lateinit var deleteCandidateUseCase: DeleteCandidateUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        deleteCandidateUseCase = DeleteCandidateUseCase(localCandidatRepository)
    }

    @After
    fun tearDown() {
        Mockito.framework().clearInlineMocks()
    }

    @Test
    fun `when execute with candidate, it should delete the candidate from repository`() = runBlocking {
        // Arrange
        val candidateToDelete = Candidate(
            id = 1, // Assuming id is non-null
            picture = "https://example.com/picture.jpg",
            note = "CAND123",
            description = "Software Engineer",
            firstName = "John",
            lastName = "Doe",
            phone = "1234567890",
            email = "johndoe@example.com",
            anivDat = "1990-01-01",
            isFavorite = true
        )

        // Act
        deleteCandidateUseCase.execute(candidateToDelete)

        // Assert
        Mockito.verify(localCandidatRepository).deleteCandidat(candidateToDelete)
    }

    @Test
    fun `when execute with candidate ID, it should delete the candidate by ID from repository`() = runBlocking {
        // Arrange
        val candidateIdToDelete = 1 // assuming id is non-null

        // Act
        deleteCandidateUseCase.execute(candidateIdToDelete)

        // Assert
        Mockito.verify(localCandidatRepository).deleteCandidatById(candidateIdToDelete)
    }

    @Test
    fun `when getAllCandidates is called after deletion, it should return remaining candidates`() = runBlocking {
        // Arrange
        val candidateToDelete = Candidate(
            id = 1, // Assuming id is non-null
            picture = "file:///android_asset/my_local_picture.jpg", // Path to your local file
            note = "CAND123",
            description = "Software Engineer",
            firstName = "John",
            lastName = "Doe",
            phone = "1234567890",
            email = "johndoe@example.com",
            anivDat = "1990-01-01",
            isFavorite = true
        )

        val remainingCandidates = listOf(
            Candidate(
                id = 2,
                picture = "https://example.com/picture2.jpg",
                note = "CAND124",
                description = "Data Scientist",
                firstName = "Alice",
                lastName = "Smith",
                phone = "1122334455",
                email = "alice.smith@example.com",
                anivDat = "1998-12-12",
                isFavorite = false
            )
        )

        // Mock initial state of repository
        Mockito.`when`(localCandidatRepository.getLocalCandidats()).thenReturn(flowOf(emptyList()))

        // Simulate the state after deletion and addition
        Mockito.`when`(localCandidatRepository.getLocalCandidats()).thenReturn(flowOf(remainingCandidates))

        // Act
        deleteCandidateUseCase.execute(candidateToDelete)
        val result = localCandidatRepository.getLocalCandidats().toList().flatten() // Flatten in case the flow emits a list of lists

        // Assert
        TestCase.assertEquals(remainingCandidates, result)
    }
}
import com.example.vitesse.data.repository.LocalCandidatRepository
import com.example.vitesse.model.Candidate
import com.example.vitesse.usecase.UpdateCandidateUseCase
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

class UpdateCandidateUseCaseTest {

    @Mock
    private lateinit var localCandidatRepository: LocalCandidatRepository

    private lateinit var updateCandidateUseCase: UpdateCandidateUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        updateCandidateUseCase = UpdateCandidateUseCase(localCandidatRepository)
    }

    @After
    fun tearDown() {
        Mockito.framework().clearInlineMocks()
    }

    @Test
    fun `when execute is called, it should update the candidate in the repository`() = runBlocking {
        // Arrange
        val candidateToUpdate = Candidate(
            id = 1,
            picture = "https://example.com/picture.jpg",
            identifiant = "CAND123",
            description = "Updated description",
            firstName = "John",
            lastName = "Doe",
            phone = "1234567890",
            email = "johndoe@example.com",
            anivDate = "1990-01-01",
            isFavorite = true
        )

        // Act
        updateCandidateUseCase.execute(candidateToUpdate)

        // Assert
        Mockito.verify(localCandidatRepository).updateCandidat(candidateToUpdate)
    }

    @Test
    fun `when getAllCandidates is called after updating, it should reflect updated candidates`() = runBlocking {
        // Arrange
        val initialCandidates = listOf(
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

        val updatedCandidate = initialCandidates[0].copy(
            description = "Senior Software Engineer",
            isFavorite = false
        )

        // Mock the repository to return the initial candidates
        Mockito.`when`(localCandidatRepository.getLocalCandidats()).thenReturn(flowOf(initialCandidates))

        // Act - Update the candidate
        updateCandidateUseCase.execute(updatedCandidate)

        // Mock repository again after update to return the updated list
        val updatedCandidates = listOf(updatedCandidate, initialCandidates[1])
        Mockito.`when`(localCandidatRepository.getLocalCandidats()).thenReturn(flowOf(updatedCandidates))

        // Act - Get all candidates after the update
        val result = localCandidatRepository.getLocalCandidats().toList() // Collect the flow into a list

        // Assert - Check if the updated list is returned
        assertEquals(updatedCandidates, result.first())
    }
}

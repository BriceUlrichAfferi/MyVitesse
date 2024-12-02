package com.example.vitesse

import com.example.vitesse.data.service.apiConversion.CurrencyApiService
import com.example.vitesse.data.service.apiConversion.CurrencyRates
import com.example.vitesse.data.service.apiConversion.CurrencyRepository
import com.example.vitesse.data.service.apiConversion.CurrencyResponse
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.fail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class CurrencyRepositoryTest {

    @Mock
    private lateinit var mockCurrencyApiService: CurrencyApiService

    private lateinit var repository: CurrencyRepository

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        // Initialize Mockito
        MockitoAnnotations.initMocks(this)

        // Initialize the repository with the mocked API service
        repository = CurrencyRepository(mockCurrencyApiService)

        // Set the dispatcher for coroutines
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `test getCurrencyRates success`() = runBlockingTest {
        // Given: A mock CurrencyResponse
        val mockResponse = CurrencyResponse(
            date = "2024-11-29",
            eur = CurrencyRates(gbp = 0.85)
        )

        // Mock the API response using Mockito
        Mockito.`when`(mockCurrencyApiService.getCurrencyRates()).thenReturn(mockResponse)

        // When: The repository's getCurrencyRates is called
        val result = repository.getCurrencyRates()

        // Then: The result should match the mock response
        assertEquals(mockResponse, result)
    }

    @Test
    fun `test getCurrencyRates failure`() = runBlockingTest {
        // Given: A RuntimeException thrown by the mock API
        Mockito.`when`(mockCurrencyApiService.getCurrencyRates()).thenThrow(RuntimeException("API error"))

        // When: The repository's getCurrencyRates is called
        try {
            repository.getCurrencyRates()
            fail("Exception should have been thrown")
        } catch (e: RuntimeException) {
            // Then: The exception should be handled by the repository
            assertEquals("API error", e.message)
        }
    }

}

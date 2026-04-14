package com.example.weatherapptask.usecases

import com.example.weatherapptask.domain.model.CitySuggestion
import com.example.weatherapptask.domain.repository.WeatherRepository
import com.example.weatherapptask.domain.usecase.SearchCitiesUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class SearchCitiesUseCaseTest {

    private val repository = mockk<WeatherRepository>()
    private val useCase = SearchCitiesUseCase(repository)

    @Test
    fun valid_query_returns_data() = runTest {
        val cities = listOf(
            CitySuggestion("1", "Warszawa", "Polska", "Mazowieckie")
        )

        coEvery { repository.searchCities("Warszawa") } returns cities

        val result = useCase("Warszawa")

        Assert.assertTrue(result.isSuccess)
        Assert.assertEquals(cities, result.getOrNull())
    }

    @Test
    fun invalid_query_returns_error() = runTest {
        val result = useCase("123")

        Assert.assertTrue(result.isFailure)
        coVerify(exactly = 0) { repository.searchCities(any()) }
    }
}
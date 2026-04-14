package com.example.weatherapptask.repositories

import com.example.weatherapptask.MainDispatcherRule
import com.example.weatherapptask.data.local.dao.SearchHistoryDao
import com.example.weatherapptask.data.remote.AccuWeatherApi
import com.example.weatherapptask.data.remote.dto.AdministrativeAreaDto
import com.example.weatherapptask.data.remote.dto.CitySuggestionDto
import com.example.weatherapptask.data.remote.dto.CountryDto
import com.example.weatherapptask.data.repositories.WeatherRepositoryImpl
import com.example.weatherapptask.domain.model.CitySuggestion
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherRepositoryImplTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val api = mockk<AccuWeatherApi>()
    private val dao = mockk<SearchHistoryDao>(relaxed = true)

    private val repository = WeatherRepositoryImpl(
        api,
        dao,
        dispatcherRule.dispatcher
    )

    @Test
    fun searchCities_maps_correctly() = runTest {
        coEvery { api.autocompleteCities("War") } returns listOf(
            CitySuggestionDto(
                key = "1",
                localizedName = "Warszawa",
                country = CountryDto("Polska"),
                administrativeArea = AdministrativeAreaDto("Mazowieckie")
            )
        )

        val result = repository.searchCities("War")

        TestCase.assertEquals(1, result.size)
        TestCase.assertEquals("Warszawa", result.first().name)
    }

    @Test
    fun saveSearch_inserts_and_trims() = runTest {
        val city = CitySuggestion("1", "Warszawa", "Polska", "Mazowieckie")

        repository.saveSearch(city)

        coVerify { dao.insert(any()) }
        coVerify { dao.trimToLastTen() }
    }
}
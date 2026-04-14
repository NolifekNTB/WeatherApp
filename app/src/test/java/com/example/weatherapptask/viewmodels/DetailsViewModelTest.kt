package com.example.weatherapptask.viewmodels

import androidx.lifecycle.SavedStateHandle
import com.example.weatherapptask.MainDispatcherRule
import com.example.weatherapptask.domain.model.WeatherDetails
import com.example.weatherapptask.domain.repository.WeatherRepository
import com.example.weatherapptask.ui.details.DetailsViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailsViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val repository = mockk<WeatherRepository>()

    @Test
    fun loads_data_successfully() = runTest {
        val details = WeatherDetails(
            cityName = "Warszawa",
            temperatureCelsius = 20.0,
            weatherText = "Sunny",
            hasPrecipitation = false,
            isDayTime = true,
            relativeHumidity = 50,
            cloudCover = 20,
            headline = "Nice",
            forecast = emptyList()
        )

        coEvery { repository.getWeatherDetails(any()) } returns details

        val savedStateHandle = SavedStateHandle(
            mapOf(
                "cityKey" to "1",
                "cityName" to "Warszawa",
                "admin" to "Mazowieckie",
                "country" to "Polska"
            )
        )

        val vm = DetailsViewModel(savedStateHandle, repository)

        advanceUntilIdle()

        assertEquals(details, vm.uiState.value.data)
    }

    @Test
    fun error_sets_error_state() = runTest {
        coEvery { repository.getWeatherDetails(any()) } throws RuntimeException()

        val savedStateHandle = SavedStateHandle(
            mapOf(
                "cityKey" to "1",
                "cityName" to "Warszawa",
                "admin" to "Mazowieckie",
                "country" to "Polska"
            )
        )

        val vm = DetailsViewModel(savedStateHandle, repository)

        advanceUntilIdle()

        assertNotNull(vm.uiState.value.error)
    }
}
package com.example.weatherapptask

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.weatherapptask.domain.model.DailyForecast
import com.example.weatherapptask.domain.model.WeatherDetails
import com.example.weatherapptask.ui.details.DetailsScreenContent
import com.example.weatherapptask.ui.details.DetailsUiState
import org.junit.Rule
import org.junit.Test

class DetailsScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private fun mockData(temp: Double) = WeatherDetails(
        cityName = "Warszawa",
        temperatureCelsius = temp,
        weatherText = "Słonecznie",
        hasPrecipitation = false,
        isDayTime = true,
        relativeHumidity = 50,
        cloudCover = 20,
        headline = "Ciepło",
        forecast = listOf(
            DailyForecast(
                date = "2026-04-13",
                minTemp = 10.0,
                maxTemp = 20.0,
                dayPhrase = "Słońce",
                nightPhrase = "Czysto",
                precipitationProbability = 0,
                cloudCover = 10
            )
        )
    )

    @Test
    fun weather_data_is_displayed() {
        composeRule.setContent {
            MaterialTheme {
                DetailsScreenContent(
                    state = DetailsUiState(
                        isLoading = false,
                        data = mockData(15.0)
                    ),
                    onBack = {}
                )
            }
        }

        composeRule.onNodeWithText("Warszawa").assertExists()
        composeRule.onNodeWithText("15.0 °C").assertExists()
        composeRule.onNodeWithText("Słonecznie").assertExists()
    }

    @Test
    fun error_state_is_displayed() {
        composeRule.setContent {
            MaterialTheme {
                DetailsScreenContent(
                    state = DetailsUiState(
                        isLoading = false,
                        error = "Błąd"
                    ),
                    onBack = {}
                )
            }
        }

        composeRule
            .onNodeWithText("Błąd")
            .assertExists()
    }
}
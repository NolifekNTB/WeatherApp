package com.example.weatherapptask

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.weatherapptask.domain.model.CitySuggestion
import com.example.weatherapptask.ui.search.SearchScreenContent
import com.example.weatherapptask.ui.search.SearchUiState
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun text_field_exits() {
        composeRule.setContent {
            MaterialTheme {
                SearchScreenContent(
                    state = SearchUiState(),
                    onQueryChange = {},
                    onCityClick = {}
                )
            }
        }

        composeRule
            .onNodeWithText("Wyszukaj miasto").assertIsDisplayed()
    }

    @Test
    fun suggestions_are_displayed() {
        val cities = listOf(
            CitySuggestion("1", "Warszawa", "Polska", "Mazowieckie")
        )

        composeRule.setContent {
            MaterialTheme {
                SearchScreenContent(
                    state = SearchUiState(
                        query = "Wa",
                        suggestions = cities
                    ),
                    onQueryChange = {},
                    onCityClick = {}
                )
            }
        }

        composeRule
            .onNodeWithText("Warszawa")
            .assertExists()
    }

    @Test
    fun error_is_displayed() {
        composeRule.setContent {
            MaterialTheme {
                SearchScreenContent(
                    state = SearchUiState(
                        query = "123",
                        error = "Niepoprawna nazwa miasta"
                    ),
                    onQueryChange = {},
                    onCityClick = {}
                )
            }
        }

        composeRule
            .onNodeWithText("Niepoprawna nazwa miasta")
            .assertExists()
    }

    @Test
    fun clicking_city_triggers_callback() {
        val cities = listOf(
            CitySuggestion("1", "Warszawa", "Polska", "Mazowieckie")
        )

        var clicked = false

        composeRule.setContent {
            MaterialTheme {
                SearchScreenContent(
                    state = SearchUiState(
                        query = "Wa",
                        suggestions = cities
                    ),
                    onQueryChange = {},
                    onCityClick = { clicked = true }
                )
            }
        }

        composeRule
            .onNodeWithText("Warszawa")
            .performClick()

        assert(clicked)
    }
}
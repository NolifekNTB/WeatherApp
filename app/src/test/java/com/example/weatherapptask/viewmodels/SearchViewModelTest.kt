package com.example.weatherapptask.viewmodels

import com.example.weatherapptask.MainDispatcherRule
import com.example.weatherapptask.domain.model.CitySuggestion
import com.example.weatherapptask.domain.usecase.ObserveSearchHistoryUseCase
import com.example.weatherapptask.domain.usecase.SaveSearchQueryUseCase
import com.example.weatherapptask.domain.usecase.SearchCitiesUseCase
import com.example.weatherapptask.ui.search.SearchViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val searchUseCase = mockk<SearchCitiesUseCase>()
    private val saveUseCase = mockk<SaveSearchQueryUseCase>(relaxed = true)
    private val historyUseCase = mockk<ObserveSearchHistoryUseCase>()

    @Test
    fun typing_valid_query_updates_suggestions() = runTest {
        val cities = listOf(
            CitySuggestion("1", "Warszawa", "Polska", "Mazowieckie")
        )

        every { historyUseCase() } returns flowOf(emptyList())
        coEvery { searchUseCase("War") } returns Result.success(cities)

        val vm = SearchViewModel(
            searchUseCase,
            saveUseCase,
            historyUseCase
        )

        vm.onQueryChange("War")

        advanceUntilIdle()

        val state = vm.uiState.value
        assertEquals(cities, state.suggestions)
    }

    @Test
    fun invalid_query_sets_error() = runTest {
        every { historyUseCase() } returns flowOf(emptyList())
        coEvery { searchUseCase("123") } returns Result.failure(Exception("error"))

        val vm = SearchViewModel(
            searchUseCase,
            saveUseCase,
            historyUseCase
        )

        vm.onQueryChange("123")

        advanceUntilIdle()

        assertNotNull(vm.uiState.value.error)
    }
}
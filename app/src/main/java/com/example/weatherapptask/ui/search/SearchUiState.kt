package com.example.weatherapptask.ui.search

import com.example.weatherapptask.domain.model.CitySuggestion


data class SearchUiState(
    val query: String = "",
    val suggestions: List<CitySuggestion> = emptyList(),
    val history: List<CitySuggestion> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
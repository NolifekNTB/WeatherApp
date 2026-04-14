package com.example.weatherapptask.ui.details

import com.example.weatherapptask.domain.model.WeatherDetails

data class DetailsUiState(
    val isLoading: Boolean = true,
    val data: WeatherDetails? = null,
    val error: String? = null
)
package com.example.weatherapptask.domain.repository

import com.example.weatherapptask.domain.model.CitySuggestion
import com.example.weatherapptask.domain.model.WeatherDetails
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun searchCities(query: String): List<CitySuggestion>
    suspend fun getWeatherDetails(city: CitySuggestion): WeatherDetails
    fun observeSearchHistory(): Flow<List<CitySuggestion>>
    suspend fun saveSearch(city: CitySuggestion)
}
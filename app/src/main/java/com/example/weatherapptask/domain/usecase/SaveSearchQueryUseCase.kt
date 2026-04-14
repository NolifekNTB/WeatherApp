package com.example.weatherapptask.domain.usecase

import com.example.weatherapptask.domain.model.CitySuggestion
import com.example.weatherapptask.domain.repository.WeatherRepository
import javax.inject.Inject

class SaveSearchQueryUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(query: CitySuggestion) {
        repository.saveSearch(query)
    }
}

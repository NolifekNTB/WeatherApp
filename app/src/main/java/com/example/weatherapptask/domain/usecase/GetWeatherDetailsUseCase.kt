package com.example.weatherapptask.domain.usecase

import com.example.weatherapptask.domain.model.CitySuggestion
import com.example.weatherapptask.domain.model.WeatherDetails
import com.example.weatherapptask.domain.repository.WeatherRepository
import javax.inject.Inject

class GetWeatherDetailsUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(locationKey: CitySuggestion): WeatherDetails {
        return repository.getWeatherDetails(locationKey)
    }
}

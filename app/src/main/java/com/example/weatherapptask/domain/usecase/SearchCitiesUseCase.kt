package com.example.weatherapptask.domain.usecase

import com.example.weatherapptask.domain.model.CitySuggestion
import com.example.weatherapptask.domain.repository.WeatherRepository
import javax.inject.Inject

class SearchCitiesUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    private val cityRegex = Regex("^[A-Za-zĄąĆćĘęŁłŃńÓóŚśŹźŻż .'-]{2,50}$")

    suspend operator fun invoke(query: String): Result<List<CitySuggestion>> {
        val normalized = query.trim()
        if (!cityRegex.matches(normalized)) {
            return Result.failure(IllegalArgumentException("Niepoprawna nazwa miasta"))
        }
        return runCatching { repository.searchCities(normalized) }
    }
}
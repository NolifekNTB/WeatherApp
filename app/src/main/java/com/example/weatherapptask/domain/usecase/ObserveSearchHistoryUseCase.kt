package com.example.weatherapptask.domain.usecase

import com.example.weatherapptask.domain.model.CitySuggestion
import com.example.weatherapptask.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveSearchHistoryUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    operator fun invoke(): Flow<List<CitySuggestion>> {
        return repository.observeSearchHistory()
    }
}

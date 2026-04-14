package com.example.weatherapptask.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapptask.domain.model.CitySuggestion
import com.example.weatherapptask.domain.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: WeatherRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailsUiState())
    val uiState: StateFlow<DetailsUiState> = _uiState.asStateFlow()

    init {
        val cityKey: String = checkNotNull(savedStateHandle["cityKey"])
        val cityName: String = checkNotNull(savedStateHandle["cityName"])
        val admin: String = checkNotNull(savedStateHandle["admin"])
        val country: String = checkNotNull(savedStateHandle["country"])

        viewModelScope.launch {
            runCatching {
                repository.getWeatherDetails(
                    CitySuggestion(
                        key = cityKey,
                        name = cityName,
                        administrativeArea = admin,
                        country = country
                    )
                )
            }.onSuccess {
                _uiState.value = DetailsUiState(isLoading = false, data = it)
            }.onFailure {
                _uiState.value = DetailsUiState(isLoading = false, error = it.message)
            }
        }
    }
}
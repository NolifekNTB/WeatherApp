package com.example.weatherapptask.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapptask.domain.model.CitySuggestion
import com.example.weatherapptask.domain.usecase.ObserveSearchHistoryUseCase
import com.example.weatherapptask.domain.usecase.SaveSearchQueryUseCase
import com.example.weatherapptask.domain.usecase.SearchCitiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchCities: SearchCitiesUseCase,
    private val saveSearch: SaveSearchQueryUseCase,
    observeSearchHistory: ObserveSearchHistoryUseCase
) : ViewModel() {

    private val query = MutableStateFlow("")
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        observeSearchHistory()
            .onEach { history ->
                _uiState.update { it.copy(history = history) }
            }
            .launchIn(viewModelScope)

        query
            .debounce(400)
            .distinctUntilChanged()
            .onEach { query ->
                if (query.isBlank()) {
                    _uiState.update { it.copy(suggestions = emptyList(), error = null, isLoading = false) }
                    return@onEach
                }

                _uiState.update { it.copy(isLoading = true, error = null) }
                val result = searchCities(query)
                result.onSuccess { cities ->
                    _uiState.update { it.copy(isLoading = false, suggestions = cities, error = null) }
                }.onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            suggestions = emptyList(),
                            error = throwable.message ?: "Wystąpił błąd"
                        )
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun onQueryChange(value: String) {
        _uiState.update { it.copy(query = value) }
        query.value = value
    }

    fun onCitySelected(city: CitySuggestion) {
        viewModelScope.launch {
            saveSearch(city)
        }
    }
}

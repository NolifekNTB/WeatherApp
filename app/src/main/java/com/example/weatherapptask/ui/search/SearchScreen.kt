package com.example.weatherapptask.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherapptask.R
import com.example.weatherapptask.domain.model.CitySuggestion

@Composable
fun SearchScreen(
    onCityClick: (CitySuggestion) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    SearchScreenContent(
        onCityClick = {
            viewModel.onCitySelected(it)
            onCityClick(it)
        },
        onQueryChange = viewModel::onQueryChange,
        state = state,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenContent(
    onCityClick: (CitySuggestion) -> Unit,
    onQueryChange: (String) -> Unit,
    state: SearchUiState,
) {

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.search_screen_header)) })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = state.query,
                onValueChange = onQueryChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.search_screen_text_field_header)) },
                singleLine = true,
                isError = state.error != null,
                supportingText = {
                    state.error?.let { Text(it) }
                }
            )

            Spacer(Modifier.height(16.dp))

            if (state.query.isBlank() && state.history.isNotEmpty()) {
                Text(stringResource(R.string.search_screen_history), style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(state.history, key = {it.key}) { city ->
                        CityRow(city = city) {
                            onCityClick(city)
                        }
                    }
                }
            } else {
                if (state.isLoading) {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(state.suggestions, key = { it.key }) { city ->
                        CityRow(city = city) {
                            onCityClick(city)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CityRow(city: CitySuggestion, onClick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(city.name, style = MaterialTheme.typography.titleMedium)
            Text(city.fullDisplayName, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchScreenPreviewSuccess() {
    val mockCities = listOf(
        CitySuggestion(
            key = "274663",
            name = "Warszawa",
            country = "Polska",
            administrativeArea = "Mazowieckie"
        ),
        CitySuggestion(
            key = "262685",
            name = "Łódź",
            country = "Polska",
            administrativeArea = "Łódzkie"
        )
    )

    MaterialTheme {
        SearchScreenContent(
            state = SearchUiState(
                query = "Wa",
                suggestions = mockCities
            ),
            onQueryChange = {},
            onCityClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchScreenPreviewError() {
    MaterialTheme {
        SearchScreenContent(
            state = SearchUiState(
                query = "123",
                error = "Niepoprawna nazwa miasta"
            ),
            onQueryChange = {},
            onCityClick = {}
        )
    }
}
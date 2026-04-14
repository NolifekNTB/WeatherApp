package com.example.weatherapptask.ui.details

import android.telephony.ims.SipDetails
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherapptask.R
import com.example.weatherapptask.domain.model.DailyForecast
import com.example.weatherapptask.domain.model.WeatherDetails

@Composable
fun DetailsScreen(
    onBack: () -> Unit,
    viewModel: DetailsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    DetailsScreenContent(
        state = state,
        onBack = onBack,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreenContent(
    onBack: () -> Unit,
    state: DetailsUiState,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.details_screen_header)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.details_screen_back_button))
                    }
                }
            )
        }
    ) { padding ->
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            state.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(state.error)
                }
            }

            else -> {
                val data = state.data ?: return@Scaffold
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        ElevatedCard(Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(16.dp)) {
                                Text(data.cityName, style = MaterialTheme.typography.titleLarge)
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    text = "${data.temperatureCelsius} °C",
                                    style = MaterialTheme.typography.displaySmall,
                                    color = temperatureColor(data.temperatureCelsius)
                                )
                                Text(data.weatherText)
                                Text(stringResource(R.string.details_screen_humidity, formatPercent(data.relativeHumidity)))
                                Text(stringResource(R.string.details_screen_cloudy, formatPercent(data.cloudCover)))
                                Text(stringResource(R.string.details_screen_rainfall, formatYesNo(data.hasPrecipitation)))
                            }
                        }
                    }

                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                text = stringResource(R.string.details_screen_next_days_forecast),
                                style = MaterialTheme.typography.titleMedium,
                                fontSize = 20.sp,
                            )
                        }
                    }

                    items(data.forecast) { day ->
                        ElevatedCard(Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(day.date, style = MaterialTheme.typography.titleSmall)
                                Text("MaxTemp ${day.maxTemp}°C", style = MaterialTheme.typography.titleSmall)
                                Text("MinTemp ${day.minTemp}°C", style = MaterialTheme.typography.titleSmall)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun temperatureColor(tempCelsius: Double): Color {
    return when {
        tempCelsius < 10 -> Color.Blue
        tempCelsius <= 20 -> Color.Black
        else -> Color.Red
    }
}

@Composable
private fun formatPercent(value: Int?): String {
    return value?.toString() ?: stringResource(R.string.common_dash)
}

@Composable
private fun formatYesNo(value: Boolean): String {
    return stringResource(
        if (value) R.string.common_yes else R.string.common_no
    )
}

@Preview(showBackground = true)
@Composable
private fun DetailsScreenPreviewSuccess() {
    val mockDetails = WeatherDetails(
        cityName = "Warszawa, Mazowieckie, Polska",
        temperatureCelsius = 22.5,
        weatherText = "Częściowe zachmurzenie",
        hasPrecipitation = false,
        isDayTime = true,
        relativeHumidity = 58,
        cloudCover = 35,
        headline = "Najbliższe dni będą ciepłe i suche.",
        forecast = listOf(
            DailyForecast(
                date = "2026-04-13",
                minTemp = 11.0,
                maxTemp = 22.0,
                dayPhrase = "Słonecznie",
                nightPhrase = "Bezchmurnie",
                precipitationProbability = 5,
                cloudCover = 10
            )
        )
    )

    MaterialTheme {
        DetailsScreenContent(
            state = DetailsUiState(
                isLoading = false,
                data = mockDetails
            ),
            onBack = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailsScreenPreviewError() {
    MaterialTheme {
        DetailsScreenContent(
            state = DetailsUiState(
                isLoading = false,
                error = "Nie udało się pobrać pogody"
            ),
            onBack = {}
        )
    }
}
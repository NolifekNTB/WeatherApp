package com.example.weatherapptask.data.repositories

import android.util.Log
import com.example.weatherapptask.data.local.dao.SearchHistoryDao
import com.example.weatherapptask.data.local.entities.SearchHistoryEntity
import com.example.weatherapptask.data.remote.AccuWeatherApi
import com.example.weatherapptask.di.IoDispatcher
import com.example.weatherapptask.domain.model.CitySuggestion
import com.example.weatherapptask.domain.model.DailyForecast
import com.example.weatherapptask.domain.model.WeatherDetails
import com.example.weatherapptask.domain.repository.WeatherRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: AccuWeatherApi,
    private val dao: SearchHistoryDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : WeatherRepository {
    override suspend fun searchCities(query: String): List<CitySuggestion> = withContext(ioDispatcher) {
        api.autocompleteCities(query).map {
            CitySuggestion(
                key = it.key,
                name = it.localizedName,
                country = it.country.localizedName,
                administrativeArea = it.administrativeArea.localizedName
            )
        }
    }

    override suspend fun getWeatherDetails(city: CitySuggestion): WeatherDetails = withContext(ioDispatcher) {
        val current = api.getCurrentConditions(city.key).first()
        val forecast = api.getFiveDayForecast(city.key)

        WeatherDetails(
            cityName = city.fullDisplayName,
            temperatureCelsius = current.temperature.metric.value,
            weatherText = current.weatherText,
            hasPrecipitation = current.hasPrecipitation,
            isDayTime = current.isDayTime,
            relativeHumidity = current.relativeHumidity,
            cloudCover = current.cloudCover,
            headline = forecast.headline.text,
            forecast = forecast.dailyForecasts.map {
                DailyForecast(
                    date = it.date,
                    minTemp = it.temperature.minimum.value,
                    maxTemp = it.temperature.maximum.value,
                    dayPhrase = it.day.iconPhrase,
                    nightPhrase = it.night.iconPhrase,
                    precipitationProbability = it.day.precipitationProbability,
                    cloudCover = it.day.cloudCover
                )
            }
        )
    }

    override fun observeSearchHistory(): Flow<List<CitySuggestion>> {
        return dao.observeHistory().map { list ->
            list.map {
                CitySuggestion(
                    key = it.cityKey,
                    name = it.cityName,
                    country = it.countryName,
                    administrativeArea = it.administrativeArea
                )
            }
        }
    }

    override suspend fun saveSearch(city: CitySuggestion) = withContext(ioDispatcher) {
        dao.insert(
            SearchHistoryEntity(
                cityKey = city.key,
                cityName = city.name,
                countryName = city.country,
                administrativeArea = city.administrativeArea,
                searchedAt = System.currentTimeMillis()
            )
        )
        dao.trimToLastTen()
    }
}
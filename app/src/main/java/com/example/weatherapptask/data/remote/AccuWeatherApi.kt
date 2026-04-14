package com.example.weatherapptask.data.remote

import com.example.weatherapptask.BuildConfig
import com.example.weatherapptask.data.remote.dto.CitySuggestionDto
import com.example.weatherapptask.data.remote.dto.CurrentConditionsDto
import com.example.weatherapptask.data.remote.dto.DailyForecastResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AccuWeatherApi {

    @GET("locations/v1/cities/autocomplete")
    suspend fun autocompleteCities(
        @Query("q") query: String,
        @Query("language") language: String = "pl-pl",
        @Query("apikey") apiKey: String = BuildConfig.API_KEY
    ): List<CitySuggestionDto>

    @GET("currentconditions/v1/{locationKey}")
    suspend fun getCurrentConditions(
        @Path("locationKey") locationKey: String,
        @Query("language") language: String = "pl-pl",
        @Query("details") details: Boolean = true,
        @Query("apikey") apiKey: String = BuildConfig.API_KEY
    ): List<CurrentConditionsDto>

    @GET("forecasts/v1/daily/5day/{locationKey}")
    suspend fun getFiveDayForecast(
        @Path("locationKey") locationKey: String,
        @Query("language") language: String = "pl-pl",
        @Query("metric") metric: Boolean = true,
        @Query("details") details: Boolean = true,
        @Query("apikey") apiKey: String = BuildConfig.API_KEY
    ): DailyForecastResponseDto
}
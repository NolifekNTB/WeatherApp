package com.example.weatherapptask.domain.model

data class WeatherDetails(
    val cityName: String,
    val temperatureCelsius: Double,
    val weatherText: String,
    val hasPrecipitation: Boolean,
    val isDayTime: Boolean,
    val relativeHumidity: Int?,
    val cloudCover: Int?,
    val headline: String,
    val forecast: List<DailyForecast>
)

data class DailyForecast(
    val date: String,
    val minTemp: Double,
    val maxTemp: Double,
    val dayPhrase: String,
    val nightPhrase: String,
    val precipitationProbability: Int?,
    val cloudCover: Int?
)
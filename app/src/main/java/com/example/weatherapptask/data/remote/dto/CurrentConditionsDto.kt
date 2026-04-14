package com.example.weatherapptask.data.remote.dto

import com.squareup.moshi.Json

data class CurrentConditionsDto(
    @Json(name = "WeatherText") val weatherText: String,
    @Json(name = "HasPrecipitation") val hasPrecipitation: Boolean,
    @Json(name = "IsDayTime") val isDayTime: Boolean,
    @Json(name = "Temperature") val temperature: TemperatureContainerDto,
    @Json(name = "RelativeHumidity") val relativeHumidity: Int? = null,
    @Json(name = "CloudCover") val cloudCover: Int? = null
)

data class TemperatureContainerDto(
    @Json(name = "Metric") val metric: TemperatureValueDto
)

data class TemperatureValueDto(
    @Json(name = "Value") val value: Double,
    @Json(name = "Unit") val unit: String
)

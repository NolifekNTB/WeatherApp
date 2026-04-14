package com.example.weatherapptask.data.remote.dto

import com.squareup.moshi.Json

data class DailyForecastResponseDto(
    @Json(name = "Headline") val headline: HeadlineDto,
    @Json(name = "DailyForecasts") val dailyForecasts: List<DailyForecastDto>
)

data class HeadlineDto(
    @Json(name = "Text") val text: String
)

data class DailyForecastDto(
    @Json(name = "Date") val date: String,
    @Json(name = "Temperature") val temperature: DailyTemperatureDto,
    @Json(name = "Day") val day: DayNightDto,
    @Json(name = "Night") val night: DayNightDto
)

data class DailyTemperatureDto(
    @Json(name = "Minimum") val minimum: TemperatureValueDto,
    @Json(name = "Maximum") val maximum: TemperatureValueDto
)

data class DayNightDto(
    @Json(name = "IconPhrase") val iconPhrase: String,
    @Json(name = "HasPrecipitation") val hasPrecipitation: Boolean,
    @Json(name = "PrecipitationProbability") val precipitationProbability: Int? = null,
    @Json(name = "CloudCover") val cloudCover: Int? = null
)
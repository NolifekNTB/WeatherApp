package com.example.weatherapptask.data.remote.dto

import com.squareup.moshi.Json

data class CitySuggestionDto(
    @Json(name = "Key") val key: String,
    @Json(name = "LocalizedName") val localizedName: String,
    @Json(name = "Country") val country: CountryDto,
    @Json(name = "AdministrativeArea") val administrativeArea: AdministrativeAreaDto
)

data class CountryDto(
    @Json(name = "LocalizedName") val localizedName: String
)

data class AdministrativeAreaDto(
    @Json(name = "LocalizedName") val localizedName: String
)
package com.example.weatherapptask.domain.model

data class CitySuggestion(
    val key: String,
    val name: String,
    val country: String,
    val administrativeArea: String
) {
    val fullDisplayName: String
        get() = "$name, $administrativeArea, $country"
}
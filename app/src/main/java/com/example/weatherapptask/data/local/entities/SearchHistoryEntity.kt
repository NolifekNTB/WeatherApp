package com.example.weatherapptask.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_history")
data class SearchHistoryEntity(
    @PrimaryKey val cityKey: String,
    val cityName: String,
    val countryName: String,
    val administrativeArea: String,
    val searchedAt: Long
)
package com.example.weatherapptask.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weatherapptask.data.local.dao.SearchHistoryDao
import com.example.weatherapptask.data.local.entities.SearchHistoryEntity

@Database(
    entities = [SearchHistoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun searchHistoryDao(): SearchHistoryDao
}
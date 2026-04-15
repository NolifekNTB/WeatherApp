package com.example.weatherapptask.di

import android.os.Build
import com.example.weatherapptask.data.formatter.JavaTimeFormatter
import com.example.weatherapptask.data.formatter.LegacyDateFormatter
import com.example.weatherapptask.domain.formatter.DateFormatter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object FormatterModule {

    @Provides
    fun provideDateFormatter(): DateFormatter {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            JavaTimeFormatter()
        } else {
            LegacyDateFormatter()
        }
    }
}
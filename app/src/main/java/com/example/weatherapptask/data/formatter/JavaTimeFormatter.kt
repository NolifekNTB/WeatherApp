package com.example.weatherapptask.data.formatter

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.weatherapptask.domain.formatter.DateFormatter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class JavaTimeFormatter : DateFormatter {
    override fun format(input: String): String {
        val format = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        return LocalDateTime.parse(input, format)
            .toLocalDate()
            .toString()
    }
}
package com.example.weatherapptask.data.formatter

import com.example.weatherapptask.domain.formatter.DateFormatter
import java.text.SimpleDateFormat
import java.util.Locale

class LegacyDateFormatter : DateFormatter {
    private val input = SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ssZ",
        Locale.getDefault()
    )

    private val output = SimpleDateFormat(
        "yyyy-MM-dd",
        Locale.getDefault()
    )

    override fun format(input: String): String {
        return try {
            val date = this@LegacyDateFormatter.input.parse(input)
            output.format(date!!)
        } catch (e: Exception) {
            "-"
        }
    }
}
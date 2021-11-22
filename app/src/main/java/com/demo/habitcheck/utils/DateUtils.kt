package com.demo.habitcheck.utils

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

object DateUtils {
    val getCurrentDateTime: Date
        get() = Calendar.getInstance().time

    fun getFormattedDateString(date: Date?): String? {
        try {
            var spf = SimpleDateFormat("dd MMM yyyy HH:mm:ss")
            val dateString = spf.format(date)
            val newDate = spf.parse(dateString)
            return spf.format(newDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }

    @SuppressLint("SimpleDateFormat")
    fun convertDate(dateInMilliseconds: String, pattern: String): String {
        if (dateInMilliseconds.isBlank()) return ""
        return SimpleDateFormat(pattern).format(dateInMilliseconds.toLong())
    }

    fun getDate(milliSeconds: Long, dateFormat: String?): String? {
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat(dateFormat)

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }
}
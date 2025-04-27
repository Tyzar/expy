package com.nokotogi.android.expy.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

const val fullDateTimeFormat = "EEEE, dd MMMM yyyy"

fun formatLocalDate(localDate: LocalDate, format: String): String {
    val formatter = DateTimeFormatter.ofPattern(format)
    return formatter.format(localDate)
}

fun toEpochMillis(localDate: LocalDate): Long {
    return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
}

fun toEpochMillisUTC(localDate: LocalDate): Long {
    return localDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
}

fun toLocalDate(utcMillis: Long): LocalDate {
    return Instant.ofEpochMilli(utcMillis).atZone(ZoneId.systemDefault()).toLocalDate()
}
package com.nokotogi.android.expy.utils

import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

const val fullDateTimeFormat = "EEEE, dd MMM yyyy, HH:mm"

fun formatToSystemTimeZone(dateTime: OffsetDateTime, format: String): String {
    val systemZoneOffset = OffsetDateTime.now(ZoneId.systemDefault()).offset
    val off = dateTime.toInstant().atOffset(systemZoneOffset)
    val formatter = DateTimeFormatter.ofPattern(format)
    return formatter.format(off)
}
package com.nokotogi.android.expy.utils

import java.text.NumberFormat
import java.util.Locale

fun formatCurrency(value: Double, locale: Locale = Locale("in", "ID")): String {
    val formatter = NumberFormat.getCurrencyInstance(locale).apply {
        maximumFractionDigits = if (value % 1 == 0.0) 0 else 2
    }
    return formatter.format(value)
}
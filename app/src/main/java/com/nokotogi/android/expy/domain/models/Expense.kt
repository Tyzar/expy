package com.nokotogi.android.expy.domain.models

import java.time.LocalDate

data class Expense(
    val id: Int,
    val expenseName: String,
    val amount: Double,
    val expenseDate: LocalDate,
    val category: String
)
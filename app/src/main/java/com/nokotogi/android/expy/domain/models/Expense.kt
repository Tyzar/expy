package com.nokotogi.android.expy.domain.models

import java.time.OffsetDateTime

data class Expense(
    val id: Int,
    val expenseName: String,
    val amount: Double,
    val expenseDate: OffsetDateTime,
    val category: String
)
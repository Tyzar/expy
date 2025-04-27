package com.nokotogi.android.expy.data.db.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nokotogi.android.expy.domain.models.Expense
import com.nokotogi.android.expy.utils.toEpochMillis
import com.nokotogi.android.expy.utils.toLocalDate

@Entity(tableName = "expense")
data class TExpense(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(typeAffinity = ColumnInfo.INTEGER)
    val id: Int = 0,
    @ColumnInfo
    val expenseName: String,
    @ColumnInfo
    val amount: Double,
    @ColumnInfo
    val expenseDate: Long,
    @ColumnInfo
    val category: String
)

fun TExpense.toDomain(): Expense {
    return Expense(
        id = id,
        expenseName = expenseName,
        expenseDate = toLocalDate(expenseDate),
        amount = amount,
        category = category
    )
}

fun Expense.toEntity(expenseId: Int? = null): TExpense {
    return if (expenseId == null) TExpense(
        expenseName = expenseName,
        amount = amount,
        expenseDate = toEpochMillis(expenseDate),
        category = category
    ) else TExpense(
        id = id,
        expenseName = expenseName,
        amount = amount,
        expenseDate = toEpochMillis(expenseDate),
        category = category
    )
}

package com.nokotogi.android.expy.data.db.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nokotogi.android.expy.domain.models.ExpenseCategory

@Entity(tableName = "category")
data class TCategory(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(typeAffinity = ColumnInfo.INTEGER)
    val id: Int = 0,
    val desc: String
)

fun TCategory.toDomain(): ExpenseCategory {
    return ExpenseCategory(id, desc)
}

package com.nokotogi.android.expy.data.db.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nokotogi.android.expy.data.db.room.daos.CategoryDao
import com.nokotogi.android.expy.data.db.room.daos.ExpenseDao
import com.nokotogi.android.expy.data.db.room.entities.TCategory
import com.nokotogi.android.expy.data.db.room.entities.TExpense

@Database(entities = [TExpense::class, TCategory::class], version = 1, exportSchema = true)
abstract class AppRoomDb : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun expenseDao(): ExpenseDao
}

fun buildAppRoomDb(context: Context): AppRoomDb {
    return Room.databaseBuilder(context, AppRoomDb::class.java, "expy-db").build()
}
package com.nokotogi.android.expy.data.db.room.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nokotogi.android.expy.data.db.room.entities.TExpense
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Insert
    fun insert(tExpense: TExpense)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(tExpense: TExpense)

    @Query("SELECT * FROM expense where id = :expenseId")
    fun get(expenseId: Int): TExpense?

    @Query("DELETE FROM expense WHERE id in (:expenseIds)")
    fun deleteBatch(expenseIds: List<Int>)

    @Query("SELECT * FROM expense")
    fun getAll(): Flow<List<TExpense>>
}
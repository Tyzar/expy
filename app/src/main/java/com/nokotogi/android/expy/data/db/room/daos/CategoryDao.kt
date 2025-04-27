package com.nokotogi.android.expy.data.db.room.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nokotogi.android.expy.data.db.room.entities.TCategory

@Dao
interface CategoryDao {
    @Insert
    fun insert(tCategory: TCategory)

    @Query("SELECT * FROM category WHERE `desc` = :desc")
    fun getByDesc(desc: String): List<TCategory>

    @Query("DELETE from category WHERE id = :categoryId")
    fun delete(categoryId: Int)

    @Query("SELECT * FROM category where `desc` LIKE :keyword ORDER BY id DESC")
    fun search(keyword: String): List<TCategory>

    @Query("SELECT * FROM category ORDER BY id DESC LIMIT :limit")
    fun getAll(limit: Int): List<TCategory>
}
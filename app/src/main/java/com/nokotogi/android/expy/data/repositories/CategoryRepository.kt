package com.nokotogi.android.expy.data.repositories

import com.nokotogi.android.expy.data.db.room.AppRoomDb
import com.nokotogi.android.expy.data.db.room.entities.TCategory
import com.nokotogi.android.expy.data.db.room.entities.toDomain
import com.nokotogi.android.expy.domain.models.ExpenseCategory
import com.nokotogi.android.expy.domain.repositories.CategoryRepoError
import com.nokotogi.android.expy.domain.repositories.ICategoryRepository
import com.nokotogi.mantra.either.Either
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CategoryRepository @Inject constructor(appRoomDb: AppRoomDb) : ICategoryRepository {
    private val categoryDao = appRoomDb.categoryDao()

    override suspend fun save(category: String): Either<CategoryRepoError, Unit> =
        withContext(Dispatchers.IO) {
            try {
                categoryDao.insert(TCategory(desc = category))
                return@withContext Either.Right(Unit)
            } catch (e: Exception) {
                return@withContext Either.Left(CategoryRepoError.SaveError)
            }
        }

    override suspend fun getByFullDesc(desc: String): List<ExpenseCategory> =
        withContext(Dispatchers.IO) {
            try {
                return@withContext categoryDao.getByDesc(desc).map {
                    it.toDomain()
                }
            } catch (e: Exception) {
                return@withContext emptyList()
            }
        }

    override suspend fun delete(categoryId: Int): Either<CategoryRepoError, Unit> =
        withContext(Dispatchers.IO) {
            try {
                categoryDao.delete(categoryId)
                return@withContext Either.Right(Unit)
            } catch (e: Exception) {
                return@withContext Either.Left(CategoryRepoError.DeleteError)
            }
        }

    override suspend fun searchByKeyword(keyword: String): Either<CategoryRepoError, List<ExpenseCategory>> =
        withContext(Dispatchers.IO) {
            try {
                val result = categoryDao.search("%$keyword%")
                return@withContext Either.Right(result.map { it.toDomain() })
            } catch (e: Exception) {
                return@withContext Either.Left(CategoryRepoError.GetError)
            }
        }

    override suspend fun getRecent(limit: Int): List<ExpenseCategory> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                categoryDao.getAll(limit).map { it.toDomain() }
            } catch (e: Exception) {
                emptyList()
            }
        }
}
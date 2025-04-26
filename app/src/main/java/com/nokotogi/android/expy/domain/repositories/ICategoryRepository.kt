package com.nokotogi.android.expy.domain.repositories

import com.nokotogi.android.expy.domain.models.ExpenseCategory
import com.nokotogi.android.expy.domain.errors.BaseError
import com.nokotogi.mantra.either.Either

sealed class CategoryRepoError : BaseError {
    data object SaveError : CategoryRepoError()
    data object GetError : CategoryRepoError()
    data object DeleteError : CategoryRepoError()
}

interface ICategoryRepository {
    suspend fun save(category: String): Either<CategoryRepoError, Unit>
    suspend fun getByFullDesc(desc: String): List<ExpenseCategory>
    suspend fun delete(categoryId: Int): Either<CategoryRepoError, Unit>
    suspend fun searchByKeyword(keyword: String): Either<CategoryRepoError, List<ExpenseCategory>>
    suspend fun getRecent(limit: Int): List<ExpenseCategory>
}
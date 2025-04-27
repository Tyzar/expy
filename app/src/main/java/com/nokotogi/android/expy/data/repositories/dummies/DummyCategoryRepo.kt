package com.nokotogi.android.expy.data.repositories.dummies

import com.nokotogi.android.expy.domain.models.ExpenseCategory
import com.nokotogi.android.expy.domain.repositories.CategoryRepoError
import com.nokotogi.android.expy.domain.repositories.ICategoryRepository
import com.nokotogi.mantra.either.Either
import javax.inject.Inject

class DummyCategoryRepo @Inject constructor() : ICategoryRepository {
    private val categories = listOf(
        ExpenseCategory(1, "Transport"),
        ExpenseCategory(2, "Meal"),
        ExpenseCategory(3, "Internet"),
        ExpenseCategory(4, "Entertainment")
    )

    override suspend fun save(category: String): Either<CategoryRepoError, Unit> {
        return Either.Right(Unit)
    }

    override suspend fun getByFullDesc(desc: String): List<ExpenseCategory> {
        return emptyList()
    }

    override suspend fun delete(categoryId: Int): Either<CategoryRepoError, Unit> {
        return Either.Right(Unit)
    }

    override suspend fun searchByKeyword(keyword: String): Either<CategoryRepoError, List<ExpenseCategory>> {
        val result = categories.filter {
            it.desc.contains(keyword)
        }

        return Either.Right(result)
    }

    override suspend fun getRecent(limit: Int): List<ExpenseCategory> {
        return categories
    }
}
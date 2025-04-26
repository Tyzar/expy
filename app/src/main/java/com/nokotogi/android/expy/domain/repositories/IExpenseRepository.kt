package com.nokotogi.android.expy.domain.repositories

import com.nokotogi.android.expy.domain.models.Expense
import com.nokotogi.android.expy.domain.errors.BaseError
import com.nokotogi.mantra.either.Either

sealed class ExpenseRepoError : BaseError {
    data object InsertError : ExpenseRepoError()
    data object UpdateError : ExpenseRepoError()
    data object GetError : ExpenseRepoError()
    data object DeleteError : ExpenseRepoError()
}

interface IExpenseRepository {
    suspend fun insert(expense: Expense): Either<ExpenseRepoError, Unit>
    suspend fun update(expense: Expense): Either<ExpenseRepoError, Unit>
    suspend fun get(expenseId: Int): Expense?
    suspend fun deleteBatch(expenseIds: List<Int>): Either<ExpenseRepoError, Unit>
}
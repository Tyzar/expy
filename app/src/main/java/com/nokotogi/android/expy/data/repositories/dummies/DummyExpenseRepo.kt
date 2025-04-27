package com.nokotogi.android.expy.data.repositories.dummies

import com.nokotogi.android.expy.domain.models.Expense
import com.nokotogi.android.expy.domain.repositories.ExpenseRepoError
import com.nokotogi.android.expy.domain.repositories.IExpenseRepository
import com.nokotogi.mantra.either.Either
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class DummyExpenseRepo @Inject constructor() : IExpenseRepository {
    override fun watchExpenseData(): Flow<List<Expense>> {
        TODO("Not yet implemented")
    }

    override suspend fun insert(expense: Expense): Either<ExpenseRepoError, Unit> {
        return Either.Right(Unit)
    }

    override suspend fun update(expense: Expense): Either<ExpenseRepoError, Unit> {
        return Either.Right(Unit)
    }

    override suspend fun get(expenseId: Int): Expense? {
        return Expense(
            id = 1,
            expenseName = "Grab",
            expenseDate = LocalDate.now().minusDays(3),
            amount = 54000.0,
            category = "Transport"
        )
    }

    override suspend fun deleteBatch(expenseIds: List<Int>): Either<ExpenseRepoError, Unit> {
        return Either.Right(Unit)
    }
}
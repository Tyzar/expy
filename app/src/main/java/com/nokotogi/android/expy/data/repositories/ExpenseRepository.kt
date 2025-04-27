package com.nokotogi.android.expy.data.repositories

import com.nokotogi.android.expy.data.db.room.AppRoomDb
import com.nokotogi.android.expy.data.db.room.entities.toDomain
import com.nokotogi.android.expy.data.db.room.entities.toEntity
import com.nokotogi.android.expy.domain.models.Expense
import com.nokotogi.android.expy.domain.repositories.ExpenseRepoError
import com.nokotogi.android.expy.domain.repositories.IExpenseRepository
import com.nokotogi.mantra.either.Either
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExpenseRepository @Inject constructor(appRoomDb: AppRoomDb) : IExpenseRepository {
    private val expenseDao = appRoomDb.expenseDao()
    override fun watchExpenseData(): Flow<List<Expense>> {
        return expenseDao.getAll().map { result ->
            result.map {
                it.toDomain()
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun insert(expense: Expense): Either<ExpenseRepoError, Unit> =
        withContext(Dispatchers.IO) {
            try {
                expenseDao.insert(expense.toEntity())
                return@withContext Either.Right(Unit)
            } catch (e: Exception) {
                return@withContext Either.Left(ExpenseRepoError.InsertError)
            }
        }

    override suspend fun update(expense: Expense): Either<ExpenseRepoError, Unit> =
        withContext(Dispatchers.IO) {
            try {
                expenseDao.update(expense.toEntity(expenseId = expense.id))
                return@withContext Either.Right(Unit)
            } catch (e: Exception) {
                return@withContext Either.Left(ExpenseRepoError.UpdateError)
            }
        }

    override suspend fun get(expenseId: Int): Expense? = withContext(Dispatchers.IO) {
        return@withContext try {
            expenseDao.get(expenseId)?.toDomain()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun deleteBatch(expenseIds: List<Int>): Either<ExpenseRepoError, Unit> =
        withContext(Dispatchers.IO) {
            try {
                expenseDao.deleteBatch(expenseIds)
                return@withContext Either.Right(Unit)
            } catch (e: Exception) {
                return@withContext Either.Left(ExpenseRepoError.DeleteError)
            }
        }
}
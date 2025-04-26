package com.nokotogi.android.expy.domain.add_edit_expense

import com.nokotogi.android.expy.domain.models.Expense
import com.nokotogi.android.expy.domain.repositories.ICategoryRepository
import com.nokotogi.android.expy.domain.repositories.IExpenseRepository
import com.nokotogi.mantra.either.Either
import javax.inject.Inject

class AddNewExpense @Inject constructor(
    private val expenseRepo: IExpenseRepository,
    private val categoryRepo: ICategoryRepository
) {
    suspend fun execute(form: EditExpenseForm): Either<SaveExpenseError, Unit> {
        //validate form
        val invalidInfo = form.validateAll()
        if (invalidInfo.isNotEmpty()) {
            return Either.Left(SaveExpenseError.FormInvalidError(invalidInfo))
        }

        // Check same category desc
        val sameCategoryResult = categoryRepo.getByFullDesc(form.category!!)
        if (sameCategoryResult.isEmpty()) {
            //insert this category
            categoryRepo.save(form.category)
        }

        val expense = Expense(
            id = -1,//indicate new expense
            expenseName = form.expenseName!!,
            amount = form.amount!!.toDouble(),
            expenseDate = form.date!!,
            category = form.category
        )
        return expenseRepo.insert(expense).mapLeft {
            SaveExpenseError.Other
        }
    }
}
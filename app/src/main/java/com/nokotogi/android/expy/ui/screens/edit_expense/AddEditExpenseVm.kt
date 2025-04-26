package com.nokotogi.android.expy.ui.screens.edit_expense

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nokotogi.android.expy.domain.add_edit_expense.AddNewExpense
import com.nokotogi.android.expy.domain.add_edit_expense.EditExpense
import com.nokotogi.android.expy.domain.add_edit_expense.EditExpenseForm
import com.nokotogi.android.expy.domain.add_edit_expense.FormInvalid
import com.nokotogi.android.expy.domain.add_edit_expense.FormLabel
import com.nokotogi.android.expy.domain.add_edit_expense.SaveExpenseError
import com.nokotogi.android.expy.domain.repositories.IExpenseRepository
import com.nokotogi.mantra.either.Either
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import javax.inject.Inject

enum class ScreenInitializedState {
    Initial,
    Loaded
}

@HiltViewModel
class AddEditExpenseVm @Inject constructor(
    private val addNewExpense: AddNewExpense,
    private val editExpense: EditExpense,
    private val expenseRepository: IExpenseRepository
) :
    ViewModel() {

    private var expenseId: Int? = null

    private val mInitializedState = MutableStateFlow(ScreenInitializedState.Initial)
    val initState = mInitializedState.asStateFlow()

    private val mFormState = MutableStateFlow(EditExpenseForm())
    val formState = mFormState.asStateFlow()

    private val mInvalidInfo = mutableStateMapOf<FormLabel, FormInvalid>()
    val invalidInfo = mInvalidInfo.toMap()

    private val mSaveResultEvent = MutableSharedFlow<Either<String, String>>()
    val saveResultEvent = mSaveResultEvent.asSharedFlow()

    fun getDetail(expenseId: Int) {
        this.expenseId = expenseId
        viewModelScope.launch {
            val result = expenseRepository.get(expenseId)
            if (result != null) {
                mFormState.value = mFormState.value.copy(
                    expenseName = result.expenseName,
                    amount = result.amount.toString(),
                    date = result.expenseDate,
                    category = result.category
                )
                mInitializedState.value = ScreenInitializedState.Loaded
            }
        }
    }

    fun onExpenseNameChanged(value: String) {
        mFormState.value = mFormState.value.copy(
            expenseName = value
        )

        when (val invalid = mFormState.value.validateExpenseName()) {
            null -> mInvalidInfo.remove(FormLabel.ExpenseName)
            else -> mInvalidInfo[FormLabel.ExpenseName] = invalid
        }
    }

    fun onAmountChanged(value: String) {
        mFormState.value = mFormState.value.copy(
            amount = value
        )

        when (val invalid = mFormState.value.validateExpenseAmount()) {
            null -> mInvalidInfo.remove(FormLabel.Amount)
            else -> mInvalidInfo[FormLabel.Amount] = invalid
        }
    }

    fun onDateChanged(date: OffsetDateTime) {
        mFormState.value = mFormState.value.copy(
            date = date
        )

        when (val invalid = mFormState.value.validateExpenseDate()) {
            null -> mInvalidInfo.remove(FormLabel.ExpenseDate)
            else -> mInvalidInfo[FormLabel.ExpenseDate] = invalid
        }
    }

    fun onCategoryChanged(category: String) {
        mFormState.value = mFormState.value.copy(
            category = category
        )

        when (val invalid = mFormState.value.validateExpenseCategory()) {
            null -> mInvalidInfo.remove(FormLabel.Category)
            else -> mInvalidInfo[FormLabel.Category] = invalid
        }
    }

    fun saveExpense() {
        if (mInvalidInfo.isNotEmpty()) {
            return
        }

        viewModelScope.launch {
            val result =
                if (expenseId == null) addNewExpense.execute(mFormState.value) else editExpense.execute(
                    mFormState.value,
                    expenseId!!
                )
            when (result) {
                is Either.Left -> {
                    when (result.leftValue) {
                        is SaveExpenseError.FormInvalidError -> {
                            mInvalidInfo.clear()
                            mInvalidInfo.putAll(
                                (result.leftValue as SaveExpenseError.FormInvalidError).invalidInfo
                            )
                        }

                        SaveExpenseError.Other -> mSaveResultEvent.emit(Either.Left("Failed to save expense"))
                    }
                }

                is Either.Right -> mSaveResultEvent.emit(Either.Right("Expense is saved"))
            }
        }
    }

}
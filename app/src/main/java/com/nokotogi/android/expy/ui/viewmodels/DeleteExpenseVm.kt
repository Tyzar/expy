package com.nokotogi.android.expy.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nokotogi.android.expy.domain.repositories.IExpenseRepository
import com.nokotogi.mantra.either.Either
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeleteExpenseVm @Inject constructor(private val expenseRepo: IExpenseRepository) :
    ViewModel() {

    private val mDeleteResult = MutableSharedFlow<Either<String, String>>()
    val deleteResult = mDeleteResult.asSharedFlow()

    fun delete(expenseIds: List<Int>) {
        viewModelScope.launch {
            when (expenseRepo.deleteBatch(expenseIds)) {
                is Either.Left -> mDeleteResult.emit(Either.Left("Failed to delete expense"))
                is Either.Right -> mDeleteResult.emit(Either.Right("Expense is deleted"))
            }
        }
    }
}
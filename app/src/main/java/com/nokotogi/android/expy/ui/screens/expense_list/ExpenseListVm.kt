package com.nokotogi.android.expy.ui.screens.expense_list

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nokotogi.android.expy.data.repositories.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ExpenseListVm @Inject constructor(private val expenseRepository: ExpenseRepository) :
    ViewModel() {

    val expenseListState = expenseRepository.watchExpenseData()
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    private val mSelection = mutableStateListOf<Int>()

    fun getSelection(): List<Int> {
        return mSelection.toList()
    }

    fun toggleSelectExpense(expenseId: Int) {
        if (mSelection.contains(expenseId)) {
            mSelection.remove(expenseId)
        } else {
            mSelection.add(expenseId)
        }
    }

    fun removeAllSelections() {
        mSelection.clear()
    }
}
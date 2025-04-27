package com.nokotogi.android.expy.ui.screens.expense_list

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nokotogi.android.expy.data.repositories.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ExpenseListVm @Inject constructor(expenseRepository: ExpenseRepository) :
    ViewModel() {

    val expenseListState = expenseRepository.watchExpenseData()
        .map { expenseList ->
            expenseList.groupBy {
                it.expenseDate
            }.toSortedMap(compareByDescending { it })
        }
        .flowOn(Dispatchers.Default)
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            emptyMap()
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
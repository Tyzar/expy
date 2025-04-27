package com.nokotogi.android.expy.ui.screens.edit_expense

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nokotogi.android.expy.domain.models.ExpenseCategory
import com.nokotogi.android.expy.domain.repositories.ICategoryRepository
import com.nokotogi.mantra.either.Either
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryVm @Inject constructor(private val categoryRepo: ICategoryRepository) :
    ViewModel() {

    private val mCategories = mutableStateListOf<ExpenseCategory>()

    fun getCategories(): List<ExpenseCategory> {
        return mCategories.toList()
    }

    fun initialize() {
        viewModelScope.launch {
            mCategories.addAll(categoryRepo.getRecent(limit = 10))
        }
    }

    fun search(keyword: String) {
        viewModelScope.launch {
            when (val result = categoryRepo.searchByKeyword(keyword)) {
                is Either.Left -> Log.d("ExpyDebug", "Failed to search category")
                is Either.Right -> {
                    mCategories.clear()
                    mCategories.addAll(result.rightValue)
                }
            }
        }
    }

    fun delete(categoryId: Int) {
        viewModelScope.launch {
            when (categoryRepo.delete(categoryId)) {
                is Either.Left -> Log.d("ExpyDebug", "Failed to delete category")
                is Either.Right -> mCategories.removeIf {
                    it.id == categoryId
                }
            }
        }
    }
}
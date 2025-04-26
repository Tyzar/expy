package com.nokotogi.android.expy.domain.add_edit_expense

import com.nokotogi.android.expy.domain.errors.BaseError

sealed class SaveExpenseError : BaseError {
    data class FormInvalidError(val invalidInfo: Map<FormLabel, FormInvalid>) : SaveExpenseError()
    data object Other : SaveExpenseError()
}
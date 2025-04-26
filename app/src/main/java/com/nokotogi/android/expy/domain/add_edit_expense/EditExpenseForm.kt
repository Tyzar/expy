package com.nokotogi.android.expy.domain.add_edit_expense

import java.time.OffsetDateTime

enum class FormInvalid {
    FieldEmpty,
    AmountNotNumber,
    ExpenseDateInFuture
}

enum class FormLabel {
    ExpenseName,
    Amount,
    ExpenseDate,
    Category
}

data class EditExpenseForm(
    val expenseName: String? = null,
    val amount: String? = null,
    val date: OffsetDateTime? = null,
    val category: String? = null
) {
    fun isFormEmpty(): Boolean {
        return expenseName == null && amount == null && date == null && category == null
    }

    /** Validate expense name.
     * Constraints: - Required
     *              - Cannot empty
     * */
    fun validateExpenseName(): FormInvalid? {
        if (expenseName.isNullOrBlank() || expenseName.isEmpty())
            return FormInvalid.FieldEmpty

        return null
    }

    /** Validate expense amount.
     * Constraints: - Required
     *              - Type: number
     * */
    fun validateExpenseAmount(): FormInvalid? {
        if (amount.isNullOrBlank() || amount.isEmpty())
            return FormInvalid.FieldEmpty
        if (amount.toDoubleOrNull() == null)
            return FormInvalid.AmountNotNumber

        return null
    }

    /** Validate expense date.
     * Constraints: - Required
     *              - Max: Future date not allowed
     * */
    fun validateExpenseDate(): FormInvalid? {
        if (date == null)
            return FormInvalid.FieldEmpty
        if (date.isAfter(OffsetDateTime.now())) {
            return FormInvalid.ExpenseDateInFuture
        }

        return null
    }

    /** Validate category.
     * Constraints: - Required
     *              - Cannot empty
     * */
    fun validateExpenseCategory(): FormInvalid? {
        if (category.isNullOrBlank() || category.isEmpty())
            return FormInvalid.FieldEmpty

        return null
    }

    fun validateAll(): Map<FormLabel, FormInvalid> {
        val mInvalidInfo = mutableMapOf<FormLabel, FormInvalid>()
        validateExpenseName()?.let {
            mInvalidInfo[FormLabel.ExpenseName] = it
        }

        validateExpenseAmount()?.let {
            mInvalidInfo[FormLabel.Amount] = it
        }

        validateExpenseDate()?.let {
            mInvalidInfo[FormLabel.ExpenseDate] = it
        }

        validateExpenseCategory()?.let {
            mInvalidInfo[FormLabel.Category] = it
        }

        return mInvalidInfo
    }
}
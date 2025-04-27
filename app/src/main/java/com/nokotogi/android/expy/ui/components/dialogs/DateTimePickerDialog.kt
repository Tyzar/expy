package com.nokotogi.android.expy.ui.components.dialogs

import android.util.Log
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import com.nokotogi.android.expy.utils.toEpochMillis
import com.nokotogi.android.expy.utils.toEpochMillisUTC
import com.nokotogi.android.expy.utils.toLocalDate
import java.time.Instant
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDatePickerDialog(
    onSelected: (LocalDate) -> Unit,
    onDismissRequest: () -> Unit,
    initialDate: LocalDate? = null
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = if (initialDate == null)
            toEpochMillisUTC(LocalDate.now())
        else toEpochMillisUTC(
            initialDate
        ),
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val localDate = toLocalDate(utcTimeMillis)
                return !localDate.isAfter(LocalDate.now())
            }

            override fun isSelectableYear(year: Int): Boolean {
                val nowYear = LocalDate.now().year
                return year > (nowYear - 1) && year <= nowYear
            }
        }
    )

    DatePickerDialog(onDismissRequest = onDismissRequest, confirmButton = {
        TextButton(onClick = {
            if (datePickerState.selectedDateMillis != null) {
                val date = toLocalDate(datePickerState.selectedDateMillis!!)
                onSelected(date)
                onDismissRequest()
            }
        }) {
            Text(text = "Ok")
        }
    }, dismissButton = {
        TextButton(onClick = onDismissRequest) {
            Text(text = "Cancel")
        }
    }) {
        DatePicker(state = datePickerState)
    }
}
package com.nokotogi.android.expy.ui.screens.edit_expense

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.nokotogi.android.expy.R
import com.nokotogi.android.expy.domain.add_edit_expense.FormInvalid
import com.nokotogi.android.expy.domain.add_edit_expense.FormLabel
import com.nokotogi.android.expy.ui.components.dialogs.AppDatePickerDialog
import com.nokotogi.android.expy.ui.components.dialogs.DeleteConfirmDialog
import com.nokotogi.android.expy.ui.components.dialogs.ExpenseCategorySheet
import com.nokotogi.android.expy.ui.components.textfields.LabelTextField
import com.nokotogi.android.expy.ui.components.topbars.NavTopBar
import com.nokotogi.android.expy.ui.routes.ExpenseListRoute
import com.nokotogi.android.expy.ui.viewmodels.DeleteExpenseVm
import com.nokotogi.android.expy.utils.formatLocalDate
import com.nokotogi.android.expy.utils.fullDateTimeFormat
import com.nokotogi.mantra.either.Either
import kotlinx.coroutines.launch

@Composable
fun EditExpenseScreen(
    rootNavController: NavHostController,
    expenseId: Int? = null,
    addEditExpenseVm: AddEditExpenseVm,
    categoryVm: CategoryVm,
    deleteExpenseVm: DeleteExpenseVm
) {
    /*** ViewModel states */
    val initState by addEditExpenseVm.initState.collectAsStateWithLifecycle()
    val formState by addEditExpenseVm.formState.collectAsStateWithLifecycle()
    val deleteResultState by deleteExpenseVm.deleteResult.collectAsStateWithLifecycle(
        initialValue = Either.Right(
            ""
        )
    )
    val saveResultState by addEditExpenseVm.saveResultEvent.collectAsStateWithLifecycle(
        initialValue = Either.Right(
            ""
        )
    )

    /** Overlay states */
    var showActionPopup by remember {
        mutableStateOf(false)
    }
    var showDeleteConfirmDialog by remember {
        mutableStateOf(false)
    }
    var showDatePicker by remember {
        mutableStateOf(false)
    }
    var showCategorySheet by remember {
        mutableStateOf(false)
    }
    val snackBarHostState = remember {
        SnackbarHostState()
    }

    /*** Initialize handler*/
    LaunchedEffect(initState) {
        when (initState) {
            ScreenInitializedState.Initial -> {
                launch {
                    if (expenseId != null)
                        addEditExpenseVm.getDetail(expenseId)
                }

                launch {
                    categoryVm.initialize()
                }
            }

            ScreenInitializedState.Loaded -> {}
        }
    }

    /*** Save result handler */
    LaunchedEffect(saveResultState) {
        when (saveResultState) {
            is Either.Left -> {
                snackBarHostState.showSnackbar(
                    message = (saveResultState as Either.Left<String, String>).leftValue,
                    actionLabel = "Ok",
                    withDismissAction = true,
                )
            }

            is Either.Right -> {
                if ((saveResultState as Either.Right<String, String>).rightValue.isEmpty()) {
                    return@LaunchedEffect
                }

                launch {
                    snackBarHostState.showSnackbar(
                        message = (saveResultState as Either.Right<String, String>).rightValue,
                        actionLabel = "Ok",
                        withDismissAction = true
                    )
                }

                rootNavController.popBackStack()
            }
        }
    }

    /*** Delete result handler */
    LaunchedEffect(deleteResultState) {
        when (deleteResultState) {
            is Either.Left -> {
                snackBarHostState.showSnackbar(
                    message = (saveResultState as Either.Left<String, String>).leftValue,
                    actionLabel = "Ok",
                    withDismissAction = true,
                )
            }

            is Either.Right -> {
                if ((saveResultState as Either.Right<String, String>).rightValue.isEmpty()) {
                    return@LaunchedEffect
                }

                launch {
                    snackBarHostState.showSnackbar(
                        message = (saveResultState as Either.Right<String, String>).rightValue,
                        actionLabel = "Ok",
                        withDismissAction = true
                    )
                }

                rootNavController.popBackStack()
            }
        }
    }

    /*** TextField Un-focus handler */
    val focusManager = LocalFocusManager.current
    val interactionSource = remember {
        MutableInteractionSource()
    }

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .clickable(interactionSource = interactionSource, indication = null) {
            focusManager.clearFocus()
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        topBar = {
            NavTopBar(onBack = {
                rootNavController.navigate(ExpenseListRoute) {
                    popUpTo(rootNavController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            },
                title = if (expenseId == null) "New" else "Edit",
                actions = if (expenseId != null) {
                    {
                        Box {
                            IconButton(onClick = { showActionPopup = true }) {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    painter = painterResource(id = R.drawable.ic_more),
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    contentDescription = null
                                )
                            }

                            DropdownMenu(
                                expanded = showActionPopup,
                                onDismissRequest = { showActionPopup = false }) {
                                DropdownMenuItem(leadingIcon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_delete),
                                        contentDescription = null
                                    )
                                }, contentPadding = PaddingValues(8.dp), text = {
                                    Text(
                                        text = "Delete",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }, onClick = {
                                    showActionPopup = false
                                    showDeleteConfirmDialog = true
                                })
                            }
                        }
                    }
                } else {
                    null
                })
        }, bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, end = 16.dp, bottom = 32.dp, start = 16.dp)
            ) {
                ElevatedButton(
                    modifier = Modifier.fillMaxWidth(), onClick = {
                        addEditExpenseVm.saveExpense()
                    }, colors = ButtonDefaults.elevatedButtonColors().copy(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(text = "Save")
                }
            }
        }) { paddingValues: PaddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
        ) {
            LabelTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                label = "Expense",
                placeholder = "What it spent on",
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                value = formState.expenseName ?: "",
                errorInfo = when (addEditExpenseVm.getInvalidInfo()[FormLabel.ExpenseName]) {
                    FormInvalid.FieldEmpty -> "Expense name must be filled"
                    else -> null
                },
                onValueChange = {
                    addEditExpenseVm.onExpenseNameChanged(it)
                })

            Spacer(modifier = Modifier.height(32.dp))

            LabelTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                label = "Amount",
                placeholder = "How much",
                prefixIcon = {
                    Text(text = "Rp", style = MaterialTheme.typography.bodyMedium)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                }),
                value = formState.amount ?: "",
                errorInfo = when (addEditExpenseVm.getInvalidInfo()[FormLabel.Amount]) {
                    FormInvalid.FieldEmpty -> "Amount must be filled"
                    FormInvalid.AmountNotNumber -> "Amount must be in numeric"
                    else -> null
                },
                onValueChange = {
                    addEditExpenseVm.onAmountChanged(it)
                })

            Spacer(modifier = Modifier.height(32.dp))

            LabelTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                label = "Expense At",
                placeholder = "Choose the datetime",
                prefixIcon = {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(id = R.drawable.ic_calendar),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                value = if (formState.date == null) "" else formatLocalDate(
                    formState.date!!,
                    fullDateTimeFormat
                ),
                errorInfo = when (addEditExpenseVm.getInvalidInfo()[FormLabel.ExpenseDate]) {
                    FormInvalid.FieldEmpty -> "Expense date must be filled"
                    FormInvalid.ExpenseDateInFuture -> "Future date is not allowed"
                    else -> null
                },
                readOnly = true,
                onTap = {
                    showDatePicker = true
                },
                onValueChange = {})

            Spacer(modifier = Modifier.height(32.dp))

            LabelTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                label = "Category",
                placeholder = "Add or select category",
                suffixIcon = {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                value = formState.category ?: "",
                errorInfo = when (addEditExpenseVm.getInvalidInfo()[FormLabel.Category]) {
                    FormInvalid.FieldEmpty -> "Expense category must be filled"
                    else -> null
                },
                readOnly = true,
                onTap = {
                    showCategorySheet = true
                },
                onValueChange = {})
        }
    }

    if (showDeleteConfirmDialog) {
        DeleteConfirmDialog(
            title = "Delete expense?",
            content = "This expense will be deleted.",
            onConfirm = {
                showDeleteConfirmDialog = false
                if (expenseId != null)
                    deleteExpenseVm.delete(listOf(expenseId))
            }, onCancel = {
                showDeleteConfirmDialog = false
            })
    }

    if (showDatePicker) {
        AppDatePickerDialog(
            initialDate = formState.date,
            onSelected = {
                addEditExpenseVm.onDateChanged(it)
            },
            onDismissRequest = { showDatePicker = false })
    }

    if (showCategorySheet) {
        ExpenseCategorySheet(
            modifier = Modifier.fillMaxWidth(),
            categories = categoryVm.getCategories(),
            onSelected = {
                Log.d("ExpyDebug", "Selected category: ${it.desc}")
                addEditExpenseVm.onCategoryChanged(it.desc)
            },
            onSearchChanged = {
                categoryVm.search(it)
            },
            onDeleteCategory = {
                if (it.id != null)
                    categoryVm.delete(it.id)
            },
            onDismissRequest = {
                showCategorySheet = false
            })
    }
}
package com.nokotogi.android.expy.ui.screens.expense_list

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.nokotogi.android.expy.R
import com.nokotogi.android.expy.ui.components.dialogs.DeleteConfirmDialog
import com.nokotogi.android.expy.ui.components.expense_items.ExpenseItemView
import com.nokotogi.android.expy.ui.components.topbars.MainTopBar
import com.nokotogi.android.expy.ui.components.topbars.SelectionTopBar
import com.nokotogi.android.expy.ui.routes.EditExpenseRoute
import com.nokotogi.android.expy.ui.viewmodels.DeleteExpenseVm
import com.nokotogi.mantra.either.Either
import java.time.LocalDate
import java.time.OffsetDateTime

@Composable
fun ExpenseListScreen(
    rootNavController: NavHostController,
    expenseListVm: ExpenseListVm,
    deleteExpenseVm: DeleteExpenseVm
) {
    /** ViewModels states */
    val expenseListState by expenseListVm.expenseListState.collectAsStateWithLifecycle()
    val deleteResultState by deleteExpenseVm.deleteResult.collectAsStateWithLifecycle(
        initialValue = Either.Right(
            ""
        )
    )

    /** Overlay states */
    var showDeleteConfirmDialog by remember {
        mutableStateOf(false)
    }
    val snackBarHostState = remember {
        SnackbarHostState()
    }

    LaunchedEffect(deleteResultState) {
        when (deleteResultState) {
            is Either.Left -> {
                expenseListVm.removeAllSelections()
                snackBarHostState.showSnackbar((deleteResultState as Either.Left<String, String>).leftValue)
            }

            is Either.Right -> {
                if ((deleteResultState as Either.Right<String, String>).rightValue.isEmpty()) {
                    return@LaunchedEffect
                }

                expenseListVm.removeAllSelections()
                snackBarHostState.showSnackbar((deleteResultState as Either.Right<String, String>).rightValue)
            }
        }
    }

    /** Touch */
    val hapticFeedback = LocalHapticFeedback.current

    Scaffold(modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        topBar = {
            if (expenseListVm.getSelection().isEmpty()) {
                MainTopBar(appName = stringResource(id = R.string.app_name))
            } else {
                SelectionTopBar(
                    onCancel = {
                        expenseListVm.removeAllSelections()
                    },
                    description = "${expenseListVm.getSelection().size} selected"
                ) {
                    IconButton(onClick = { showDeleteConfirmDialog = true }) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(id = R.drawable.ic_delete),
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = null
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.offset(x = (-16).dp, y = (-8).dp),
                onClick = {
                    rootNavController.navigate(EditExpenseRoute())
                },
                shape = MaterialTheme.shapes.extraLarge,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = null
                )
            }
        }) { paddingValues: PaddingValues ->
        when {
            expenseListState.isEmpty() -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    textAlign = TextAlign.Center,
                    text = "Create new expense note by tap '+' button"
                )
            }

            else -> LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues), contentPadding = PaddingValues(
                    top = 16.dp,
                    end = 16.dp,
                    bottom = 160.dp,
                    start = 16.dp
                ), verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        modifier = Modifier
                            .fillParentMaxWidth(),
                        text = "My Expenses",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                items(count = expenseListState.size, key = { expenseListState[it].id }) {
                    val expense = expenseListState[it]
                    val isSelected = expenseListVm.getSelection().contains(expense.id)

                    ExpenseItemView(
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .combinedClickable(onClick = {
                                if (expenseListVm
                                        .getSelection()
                                        .isNotEmpty()
                                ) {
                                    expenseListVm.toggleSelectExpense(expense.id)
                                    return@combinedClickable
                                }
                                rootNavController.navigate(EditExpenseRoute(expenseId = expense.id))
                            }, onLongClick = {
                                if (expenseListVm
                                        .getSelection()
                                        .isNotEmpty()
                                ) {
                                    return@combinedClickable
                                }

                                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                expenseListVm.toggleSelectExpense(expense.id)
                            }),
                        expenseName = expense.expenseName,
                        expenseAt = expense.expenseDate,
                        amount = expense.amount,
                        category = expense.category,
                        inSelectMode = expenseListVm.getSelection().isNotEmpty(),
                        isSelected = isSelected
                    )
                }
            }
        }
    }

    if (showDeleteConfirmDialog) {
        DeleteConfirmDialog(title = "Delete expense?",
            content = "${
                if (expenseListVm.getSelection().size == 1)
                    "This expense" else "These expenses"
            } will be deleted",
            onConfirm = {
                showDeleteConfirmDialog = false
                deleteExpenseVm.delete(expenseListVm.getSelection())
            },
            onCancel = {
                showDeleteConfirmDialog = false
            })
    }
}
package com.nokotogi.android.expy.ui.routes

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.nokotogi.android.expy.ui.screens.edit_expense.EditExpenseScreen
import com.nokotogi.android.expy.ui.screens.expense_list.ExpenseListScreen
import kotlinx.serialization.Serializable

@Serializable
data object RootRoute

@Serializable
data object ExpenseListRoute

@Serializable
data class EditExpenseRoute(val expenseId: Int? = null)

fun NavGraphBuilder.rootNavGraph(navController: NavHostController) {
    navigation<RootRoute>(
        startDestination = ExpenseListRoute,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }) {
        composable<ExpenseListRoute> {
            ExpenseListScreen(rootNavController = navController)
        }

        composable<EditExpenseRoute> { backStackEntry ->
            val expenseId = backStackEntry.toRoute<EditExpenseRoute>().expenseId
            EditExpenseScreen(
                rootNavController = navController,
                expenseId = expenseId,
                hiltViewModel(),
                hiltViewModel()
            )
        }
    }
}


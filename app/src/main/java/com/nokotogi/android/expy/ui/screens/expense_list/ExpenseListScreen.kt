package com.nokotogi.android.expy.ui.screens.expense_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.nokotogi.android.expy.R
import com.nokotogi.android.expy.ui.components.expense_items.ExpenseItemView
import com.nokotogi.android.expy.ui.components.topbars.MainTopBar
import com.nokotogi.android.expy.ui.routes.EditExpenseRoute
import java.time.OffsetDateTime

@Composable
fun ExpenseListScreen(rootNavController: NavHostController) {
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            MainTopBar(appName = stringResource(id = R.string.app_name))
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.offset(x = (-16).dp, y = (-32).dp),
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
        LazyColumn(
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

            items(count = 15, key = { it }) {
                ExpenseItemView(
                    modifier = Modifier
                        .fillParentMaxWidth(),
                    expenseName = "Expense $it",
                    expenseAt = OffsetDateTime.now(),
                    amount = 53000.0,
                    category = "Transport"
                )
            }
        }
    }
}
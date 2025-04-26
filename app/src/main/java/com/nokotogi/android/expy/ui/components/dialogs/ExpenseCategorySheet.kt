package com.nokotogi.android.expy.ui.components.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nokotogi.android.expy.R
import com.nokotogi.android.expy.domain.models.ExpenseCategory
import com.nokotogi.android.expy.ui.components.textfields.LabelTextField
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce

@OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)
@Composable
fun ExpenseCategorySheet(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceContainerLow,
    categories: List<ExpenseCategory>,
    onSelected: (ExpenseCategory) -> Unit,
    onDeleteCategory: (ExpenseCategory) -> Unit,
    onSearchChanged: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    var newCategoryText by remember {
        mutableStateOf<String?>(null)
    }

    LaunchedEffect(Unit) {
        snapshotFlow {
            newCategoryText
        }.debounce(500).collect {
            if (it != null)
                onSearchChanged(it)
        }
    }

    ModalBottomSheet(
        modifier = modifier,
        containerColor = backgroundColor,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        dragHandle = {
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(0.2f)
                    .padding(vertical = 16.dp),
                thickness = 4.dp
            )
        },
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Category",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(24.dp))
            LabelTextField(
                value = newCategoryText ?: "",
                placeholder = "Add or search category...",
                backgroundColor = backgroundColor,
                suffixIcon = {
                    IconButton(onClick = {
                        if (newCategoryText.isNullOrEmpty())
                            return@IconButton

                        onSelected(
                            ExpenseCategory(
                                desc = newCategoryText!!
                            )
                        )
                        onDismissRequest()
                    }) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(id = R.drawable.ic_add),
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = null
                        )
                    }
                },
                onValueChange = {
                    newCategoryText = it
                })
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(count = categories.size) {
                    Row(
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .clickable {
                                onSelected(categories[it])
                                onDismissRequest()
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = categories[it].desc,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        IconButton(
                            onClick = {
                                onDeleteCategory(categories[it])
                                onDismissRequest()
                            }) {
                            Icon(
                                tint = MaterialTheme.colorScheme.onSurface,
                                painter = painterResource(id = R.drawable.ic_close),
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }
    }
}
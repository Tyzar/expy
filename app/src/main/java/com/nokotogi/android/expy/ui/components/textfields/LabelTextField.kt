package com.nokotogi.android.expy.ui.components.textfields

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LabelTextField(
    modifier: Modifier = Modifier,
    label: String? = null,
    value: String,
    errorInfo: String? = null,
    prefixIcon: @Composable (() -> Unit)? = null,
    suffixIcon: @Composable (() -> Unit)? = null,
    placeholder: String? = null,
    readOnly: Boolean = false,
    onTap: (() -> Unit)? = null,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge.copy(
        color = MaterialTheme.colorScheme.onSurface
    ),
    onValueChange: (String) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collectLatest { interaction ->
            when (interaction) {
                is PressInteraction.Release -> {
                    if (readOnly && onTap != null)
                        onTap()
                }
            }
        }
    }

    Column(modifier = modifier) {
        if (label != null) {
            Text(text = label, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
        }
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = backgroundColor,
                ),
            value = value,
            readOnly = readOnly,
            textStyle = textStyle,
            cursorBrush = SolidColor(
                value = textStyle.color
            ),
            singleLine = true,
            onValueChange = onValueChange,
            interactionSource = interactionSource,
            keyboardActions = keyboardActions,
            keyboardOptions = keyboardOptions
        ) { innerTextField ->
            Row(
                Modifier.padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (prefixIcon != null) {
                    prefixIcon()
                    Spacer(modifier = Modifier.width(12.dp))
                }
                Box(modifier = Modifier.weight(1f)) {
                    if (placeholder != null && value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(
                                    alpha = 0.8f
                                )
                            )
                        )
                    }
                    innerTextField()
                }
                if (suffixIcon != null) {
                    Spacer(modifier = Modifier.width(12.dp))
                    suffixIcon()
                }
            }
        }
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
        )

        if (errorInfo != null)
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = errorInfo,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
    }
}
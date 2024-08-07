package com.example.playgroundyandexschool.ui.edit_todo_fragment_compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.playgroundyandexschool.R
import com.example.playgroundyandexschool.ui.models.Priority
import com.example.playgroundyandexschool.utils.theme.AppTodoTheme
import com.example.playgroundyandexschool.utils.theme.bodyTextStyle
import com.example.playgroundyandexschool.utils.theme.subHeadTextStyle

/**
 * Компонент PriorityMenu отображает выпадающее меню для выбора приоритета задачи.
 */
@Composable
fun PriorityMenu(
    changePriority: (Priority) -> Unit,
    priorityText: String,
) {
    var dropDownMenuVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .padding(vertical = 16.dp, horizontal = 16.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .noRippleClickable { dropDownMenuVisible = true }
        ) {
            Text(
                text = stringResource(R.string.priority),
                style = bodyTextStyle.copy(color = AppTodoTheme.colors.labelPrimary),
            )
            Text(
                text = priorityText,
                style = subHeadTextStyle,
                color = if (priorityText == stringResource(R.string.priority_high)) AppTodoTheme.colors.colorRed
                else AppTodoTheme.colors.labelTertiary
            )
        }
        DropdownMenu(
            expanded = dropDownMenuVisible,
            onDismissRequest = { dropDownMenuVisible = false },
            modifier = Modifier.background(AppTodoTheme.colors.backElevated)
        ) {
            DropdownMenuItem(
                text = {
                    Text(text = stringResource(R.string.priority_no))
                },
                onClick = { changePriority(Priority.NO); dropDownMenuVisible = false },
            )
            DropdownMenuItem(
                text = {
                    Text(text = stringResource(R.string.priority_low))
                },
                onClick = { changePriority(Priority.LOW); dropDownMenuVisible = false }
            )
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(R.string.priority_high),
                        color = AppTodoTheme.colors.colorRed
                    )
                },
                onClick = { changePriority(Priority.HIGH); dropDownMenuVisible = false }
            )
        }
    }
}


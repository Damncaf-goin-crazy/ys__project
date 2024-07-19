package com.example.playgroundyandexschool.ui.edit_todo_fragment_compose

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriorityMenu(
    changePriority: (Priority) -> Unit,
    priorityText: String,
) {
    var dropDownMenuVisible by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    val priorityHighText = stringResource(R.string.priority_high)
    val priorityHighOpacity = remember { androidx.compose.animation.core.Animatable(0.6f) }

    LaunchedEffect(priorityText) {
        if (priorityText == priorityHighText) {
            priorityHighOpacity.animateTo(1f, animationSpec = tween(durationMillis = 1500))
        } else {
            priorityHighOpacity.snapTo(0.6f)
        }
    }

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
                color = if (priorityText == stringResource(R.string.priority_high)) AppTodoTheme.colors.colorRed.copy(
                    alpha = priorityHighOpacity.value
                )
                else AppTodoTheme.colors.labelTertiary
            )
        }

        if (dropDownMenuVisible) {
            ModalBottomSheet(
                onDismissRequest = {
                    dropDownMenuVisible = false
                },
                sheetState = sheetState,
                containerColor = AppTodoTheme.colors.backElevated
            ) {
                Column(
                    modifier = Modifier
                        .background(color = AppTodoTheme.colors.backElevated)
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.priority_no),
                        style = bodyTextStyle,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                changePriority(Priority.NO)
                                dropDownMenuVisible = false
                            }
                            .padding(vertical = 8.dp)
                    )
                    Text(
                        text = stringResource(R.string.priority_low),
                        style = bodyTextStyle,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                changePriority(Priority.LOW)
                                dropDownMenuVisible = false
                            }
                            .padding(vertical = 8.dp)
                    )
                    Text(
                        text = stringResource(R.string.priority_high),
                        style = bodyTextStyle.copy(color = AppTodoTheme.colors.colorRed),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                changePriority(Priority.HIGH)
                                dropDownMenuVisible = false
                            }
                            .padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}


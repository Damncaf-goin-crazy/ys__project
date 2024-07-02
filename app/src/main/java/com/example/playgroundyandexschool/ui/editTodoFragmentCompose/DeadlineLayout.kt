package com.example.playgroundyandexschool.ui.editTodoFragmentCompose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.playgroundyandexschool.R
import com.example.playgroundyandexschool.utils.classes.ViewUtils
import com.example.playgroundyandexschool.utils.theme.AppTodoTheme
import com.example.playgroundyandexschool.utils.theme.BlueForTrack
import com.example.playgroundyandexschool.utils.theme.bodyTextStyle
import com.example.playgroundyandexschool.utils.theme.subHeadTextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeadlineLayout(
    changeDeadline: (Long?) -> Unit,
    getDeadline: () -> Long?,
) {
    var datePickerVisible by remember { mutableStateOf(false) }
    var deadline = getDeadline()
    var formattedDeadline by remember {
        mutableStateOf(
            if (deadline != null) ViewUtils.convertMillisToDateString(
                deadline
            ) else ""
        )
    }

    if (datePickerVisible) {
        val datePickerState = rememberDatePickerState(deadline ?: System.currentTimeMillis())
        DatePickerDialog(
            onDismissRequest = {
                datePickerVisible = false
            },
            confirmButton = {
                TextButton(onClick = {
                    changeDeadline(datePickerState.selectedDateMillis)
                    deadline = datePickerState.selectedDateMillis
                    formattedDeadline = ViewUtils.convertMillisToDateString(deadline)
                    datePickerVisible = false
                }) {
                    Text(text = stringResource(R.string.ready))
                }

            },
            dismissButton = {
                TextButton(onClick = { datePickerVisible = false }) {
                    Text(text = stringResource(R.string.cancel))
                }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column {
            Text(
                text = stringResource(R.string.do_until),
                style = bodyTextStyle.copy(color = AppTodoTheme.colors.labelPrimary),
            )
            if (formattedDeadline != "") {
                Text(
                    text = formattedDeadline,
                    style = subHeadTextStyle.copy(color = AppTodoTheme.colors.colorBlue),
                    modifier = Modifier.clickable {
                        datePickerVisible = true
                    }
                )
            }
        }
        Spacer(Modifier.weight(1f))
        Switch(
            checked = deadline != null || datePickerVisible,
            onCheckedChange = { checkedCurrent ->
                if (checkedCurrent) {
                    datePickerVisible = true
                } else {
                    changeDeadline(null)
                    formattedDeadline = ""
                }

            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = AppTodoTheme.colors.colorBlue,
                checkedTrackColor = BlueForTrack,
                uncheckedThumbColor = AppTodoTheme.colors.backElevated,
                uncheckedTrackColor = AppTodoTheme.colors.supportOverlay,
                uncheckedBorderColor = Color.Transparent,
                checkedBorderColor = Color.Transparent,
            )
        )
    }
}

package com.example.playgroundyandexschool.ui.edit_todo_fragment_compose

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.playgroundyandexschool.R
import com.example.playgroundyandexschool.utils.theme.AppTodoTheme
import com.example.playgroundyandexschool.utils.theme.buttonTextStyle

/**
 * Компонент DeleteButton отвечает за отображение кнопки удаления задачи.
 */

@Composable
fun DeleteButton(onClick: () -> Unit, state: Boolean) {
    var isPressed by remember { mutableStateOf(false) }

    val tint = if (state) {
        if (isPressed) AppTodoTheme.colors.colorRed.copy(alpha = 0.6f) else AppTodoTheme.colors.colorRed
    } else {
        AppTodoTheme.colors.colorGray
    }

    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                        onClick()
                    }
                )
            }
    ) {
        Icon(
            painter = painterResource(R.drawable.delete_red),
            contentDescription = stringResource(R.string.delete),
            tint = tint
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = stringResource(R.string.delete),
            modifier = Modifier.padding(start = 4.dp),
            color = tint,
            style = buttonTextStyle
        )
    }
}
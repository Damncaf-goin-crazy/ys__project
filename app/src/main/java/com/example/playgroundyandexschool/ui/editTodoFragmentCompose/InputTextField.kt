package com.example.playgroundyandexschool.ui.editTodoFragmentCompose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.playgroundyandexschool.R
import com.example.playgroundyandexschool.utils.theme.AppTodoTheme

@Composable
fun InputTextField(
    text: String,
    onChanged: (String) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 2.dp,
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        TextField(
            value = text,
            onValueChange = { value: String ->
                onChanged(value)
            },
            placeholder = {
                Text(
                    text = stringResource(R.string.empty_task_message),
                    style = TextStyle(color = AppTodoTheme.colors.labelTertiary)
                )
            },
            minLines = 3,
            singleLine = false,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = AppTodoTheme.colors.backSecondary,
                unfocusedContainerColor = AppTodoTheme.colors.backSecondary,
                disabledContainerColor = AppTodoTheme.colors.backSecondary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
    }

}
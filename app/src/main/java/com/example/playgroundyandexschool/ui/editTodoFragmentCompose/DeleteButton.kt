package com.example.playgroundyandexschool.ui.editTodoFragmentCompose

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.playgroundyandexschool.R
import com.example.playgroundyandexschool.utils.theme.AppTodoTheme
import com.example.playgroundyandexschool.utils.theme.buttonTextStyle

@Composable
fun DeleteButton(onClick: () -> Unit, state: Boolean) {
    Log.d("112121", (state).toString())

    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .clickable(enabled = state) {
                onClick()
            }
    ) {
        Icon(
            painter = painterResource(R.drawable.delete_red),
            contentDescription = stringResource(R.string.delete),
            tint = if (state) AppTodoTheme.colors.colorRed else AppTodoTheme.colors.colorGray
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = stringResource(R.string.delete),
            modifier = Modifier.padding(start = 4.dp),
            color = if (state) AppTodoTheme.colors.colorRed else AppTodoTheme.colors.colorGray,
            style = buttonTextStyle
        )
    }
}
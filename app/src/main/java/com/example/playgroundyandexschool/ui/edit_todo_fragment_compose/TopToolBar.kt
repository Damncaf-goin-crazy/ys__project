package com.example.playgroundyandexschool.ui.edit_todo_fragment_compose

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.playgroundyandexschool.R
import com.example.playgroundyandexschool.utils.theme.AppTodoTheme
import com.example.playgroundyandexschool.utils.theme.buttonTextStyle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Компонент TopToolbar представляет собой верхнюю панель инструментов с кнопками "Назад" и "Сохранить".
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopToolbar(
    navController: NavController,
    onSave: () -> Job,
    scrollState: ScrollState
) {
    val scope = CoroutineScope(Dispatchers.Main)

    var clickable by remember {
        mutableStateOf(true)
    }
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(
                onClick = {
                    navController.popBackStack()
                }
            ) {
                Icon(
                    painterResource(R.drawable.close),
                    contentDescription = null,
                    tint = AppTodoTheme.colors.labelPrimary
                )
            }
        },
        actions = {
            Box(
                modifier = Modifier.clickable(enabled = clickable) {
                    clickable = false
                    scope.launch {
                        val saveJob = onSave()
                        saveJob.join()
                        navController.popBackStack()
                    }
                },
            ) {
                Text(
                    text = stringResource(R.string.save),
                    style = buttonTextStyle.copy(color = AppTodoTheme.colors.colorBlue)
                )
            }

        }, scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = AppTodoTheme.colors.backPrimary
        ),
        modifier = Modifier
            .shadow(elevation = calculateElevation(scrollValue = scrollState.value.toFloat()))
    )
}

private fun calculateElevation(scrollValue: Float): Dp {
    return if (scrollValue > 0.dp.value) 4.dp else 0.dp
}
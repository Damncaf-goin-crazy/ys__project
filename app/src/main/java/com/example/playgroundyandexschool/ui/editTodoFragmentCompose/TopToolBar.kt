package com.example.playgroundyandexschool.ui.editTodoFragmentCompose

import androidx.compose.foundation.ScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.playgroundyandexschool.R
import com.example.playgroundyandexschool.utils.theme.AppTodoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopToolbar(
    navController: NavController,
    onSave: () -> Unit,
    scrollState: ScrollState
) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    painterResource(R.drawable.close),
                    contentDescription = null,
                    tint = AppTodoTheme.colors.labelPrimary
                )
            }
        },
        actions = {
            TextButton(onClick = {
                onSave()
                navController.popBackStack()
            }) {
                Text(
                    text = stringResource(R.string.save),
                    style = TextStyle(
                        color = AppTodoTheme.colors.colorBlue,
                        fontSize = 14.sp
                    ),
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
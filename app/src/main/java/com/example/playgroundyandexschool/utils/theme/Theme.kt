package com.example.playgroundyandexschool.utils.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class TodoAppColors(
    supportSeparator: Color,
    supportOverlay: Color,
    labelPrimary: Color,
    labelSecondary: Color,
    labelTertiary: Color,
    labelDisable: Color,
    colorRed: Color,
    colorGreen: Color,
    colorBlue: Color,
    colorGray: Color,
    colorGrayLight: Color,
    colorWhite: Color,
    backPrimary: Color,
    backSecondary: Color,
    backElevated: Color,
) {
    var supportSeparator by mutableStateOf(supportSeparator)
        private set
    var supportOverlay by mutableStateOf(supportOverlay)
        private set
    var labelPrimary by mutableStateOf(labelPrimary)
        private set
    var labelSecondary by mutableStateOf(labelSecondary)
        private set
    var labelTertiary by mutableStateOf(labelTertiary)
        private set
    var labelDisable by mutableStateOf(labelDisable)
        private set
    var colorRed by mutableStateOf(colorRed)
        private set
    var colorGreen by mutableStateOf(colorGreen)
        private set
    var colorBlue by mutableStateOf(colorBlue)
        private set
    var colorGray by mutableStateOf(colorGray)
        private set
    var colorGrayLight by mutableStateOf(colorGrayLight)
        private set
    var colorWhite by mutableStateOf(colorWhite)
        private set
    var backPrimary by mutableStateOf(backPrimary)
        private set
    var backSecondary by mutableStateOf(backSecondary)
        private set
    var backElevated by mutableStateOf(backElevated)
        private set

    fun update(other: TodoAppColors) {
        supportSeparator = other.supportSeparator
        supportOverlay = other.supportOverlay
        labelPrimary = other.labelPrimary
        labelSecondary = other.labelSecondary
        labelTertiary = other.labelTertiary
        labelDisable = other.labelDisable
        colorRed = other.colorRed
        colorGreen = other.colorGreen
        colorBlue = other.colorBlue
        colorGray = other.colorGray
        colorGrayLight = other.colorGrayLight
        colorWhite = other.colorWhite
        backPrimary = other.backPrimary
        backSecondary = other.backSecondary
        backElevated = other.backElevated
    }

}

private val LightColorScheme = TodoAppColors(
    supportSeparator = SupportLightSeparator,
    supportOverlay = SupportLightOverlay,
    labelPrimary = LabelLightPrimary,
    labelSecondary = LabelLightSecondary,
    labelTertiary = LabelLightTertiary,
    labelDisable = LabelLightDisable,
    colorRed = ColorLightRed,
    colorGreen = ColorLightGreen,
    colorBlue = ColorLightBlue,
    colorGray = ColorLightGray,
    colorGrayLight = ColorLightGrayLight,
    colorWhite = ColorLightWhite,
    backPrimary = BackLightPrimary,
    backSecondary = BackLightSecondary,
    backElevated = BackLightElevated,
)

private val DarkColorScheme = TodoAppColors(
    supportSeparator = SupportDarkSeparator,
    supportOverlay = SupportDarkOverlay,
    labelPrimary = LabelDarkPrimary,
    labelSecondary = LabelDarkSecondary,
    labelTertiary = LabelDarkTertiary,
    labelDisable = LabelDarkDisable,
    colorRed = ColorDarkRed,
    colorGreen = ColorDarkGreen,
    colorBlue = ColorDarkBlue,
    colorGray = ColorDarkGray,
    colorGrayLight = ColorDarkGrayLight,
    colorWhite = ColorDarkWhite,
    backPrimary = BackDarkPrimary,
    backSecondary = BackDarkSecondary,
    backElevated = BackDarkElevated,
)

internal val LocalCustomColors =
    staticCompositionLocalOf<TodoAppColors> { error("No colors was provided") }

@Composable
fun ProvideTodoAppColors(
    colors: TodoAppColors,
    content: @Composable () -> Unit
) {
    val colorPalette = remember { colors }
    colorPalette.update(colors)
    CompositionLocalProvider(LocalCustomColors provides colorPalette, content = content)
}

object AppTodoTheme {
    val colors: TodoAppColors
        @Composable
        get() = LocalCustomColors.current

}

@Composable
fun AppTodoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    ProvideTodoAppColors(colors = colorScheme) {
        MaterialTheme(
            content = content
        )
    }
}

@Composable
fun TextWithColorBox(text: String, textColor: Color, color: Color) {
    Box(
        modifier = Modifier
            .size(100.dp, 100.dp)
            .padding(8.dp)
            .background(color = color),
        contentAlignment = Alignment.BottomStart

    ) {
        Text(text = text, color = textColor, fontSize = 16.sp)
    }
}


@Preview(name = "Light Theme Preview")
@Composable
fun LightAppTodoThemePreview() {
    AppTodoTheme(darkTheme = false) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.LightGray,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextWithColorBox(
                        "Support Separator",
                        AppTodoTheme.colors.labelPrimary,
                        AppTodoTheme.colors.supportSeparator
                    )
                    TextWithColorBox(
                        "Support Overlay",
                        AppTodoTheme.colors.labelPrimary,
                        AppTodoTheme.colors.supportOverlay
                    )

                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextWithColorBox(
                        "Label Primary",
                        AppTodoTheme.colors.colorWhite,
                        AppTodoTheme.colors.labelPrimary
                    )
                    TextWithColorBox(
                        "Label Secondary",
                        AppTodoTheme.colors.colorWhite,
                        AppTodoTheme.colors.labelSecondary
                    )
                    TextWithColorBox(
                        "Label Tertiary",
                        AppTodoTheme.colors.colorWhite,
                        AppTodoTheme.colors.labelTertiary
                    )
                    TextWithColorBox(
                        "Label Disable",
                        AppTodoTheme.colors.labelPrimary,
                        AppTodoTheme.colors.labelDisable
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextWithColorBox(
                        "Color Red",
                        AppTodoTheme.colors.colorWhite,
                        AppTodoTheme.colors.colorRed
                    )
                    TextWithColorBox(
                        "Color Green",
                        AppTodoTheme.colors.colorWhite,
                        AppTodoTheme.colors.colorGreen
                    )
                    TextWithColorBox(
                        "Color\nBlue",
                        AppTodoTheme.colors.colorWhite,
                        AppTodoTheme.colors.colorBlue
                    )
                    TextWithColorBox(
                        "Color Gray",
                        AppTodoTheme.colors.colorWhite,
                        AppTodoTheme.colors.colorGray
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextWithColorBox(
                        "Color Gray Light",
                        AppTodoTheme.colors.labelPrimary,
                        AppTodoTheme.colors.colorGrayLight
                    )
                    TextWithColorBox(
                        "Color White",
                        AppTodoTheme.colors.labelPrimary,
                        AppTodoTheme.colors.colorWhite
                    )

                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextWithColorBox(
                        "Back Primary",
                        AppTodoTheme.colors.labelPrimary,
                        AppTodoTheme.colors.backPrimary
                    )
                    TextWithColorBox(
                        "Back Secondary",
                        AppTodoTheme.colors.labelPrimary,
                        AppTodoTheme.colors.backSecondary
                    )
                    TextWithColorBox(
                        "Back Elevated",
                        AppTodoTheme.colors.labelPrimary,
                        AppTodoTheme.colors.backElevated
                    )

                }
            }
        }
    }
}


@Preview(name = "Dark Theme Preview")
@Composable
fun DarkAppTodoThemePreview() {
    AppTodoTheme(darkTheme = true) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.LightGray,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextWithColorBox(
                        "Support Separator",
                        AppTodoTheme.colors.backPrimary,
                        AppTodoTheme.colors.supportSeparator
                    )
                    TextWithColorBox(
                        "Support Overlay",
                        AppTodoTheme.colors.labelPrimary,
                        AppTodoTheme.colors.supportOverlay
                    )

                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextWithColorBox(
                        "Label Primary",
                        AppTodoTheme.colors.backPrimary,
                        AppTodoTheme.colors.labelPrimary
                    )
                    TextWithColorBox(
                        "Label Secondary",
                        AppTodoTheme.colors.backPrimary,
                        AppTodoTheme.colors.labelSecondary
                    )
                    TextWithColorBox(
                        "Label Tertiary",
                        AppTodoTheme.colors.backPrimary,
                        AppTodoTheme.colors.labelTertiary
                    )
                    TextWithColorBox(
                        "Label Disable",
                        AppTodoTheme.colors.backPrimary,
                        AppTodoTheme.colors.labelDisable
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextWithColorBox(
                        "Color Red",
                        AppTodoTheme.colors.colorWhite,
                        AppTodoTheme.colors.colorRed
                    )
                    TextWithColorBox(
                        "Color Green",
                        AppTodoTheme.colors.colorWhite,
                        AppTodoTheme.colors.colorGreen
                    )
                    TextWithColorBox(
                        "Color Blue",
                        AppTodoTheme.colors.colorWhite,
                        AppTodoTheme.colors.colorBlue
                    )
                    TextWithColorBox(
                        "Color Gray",
                        AppTodoTheme.colors.colorWhite,
                        AppTodoTheme.colors.colorGray
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextWithColorBox(
                        "Color Gray Light",
                        AppTodoTheme.colors.labelPrimary,
                        AppTodoTheme.colors.colorGrayLight
                    )
                    TextWithColorBox(
                        "Color White",
                        AppTodoTheme.colors.backPrimary,
                        AppTodoTheme.colors.colorWhite
                    )

                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextWithColorBox(
                        "Back Primary",
                        AppTodoTheme.colors.labelPrimary,
                        AppTodoTheme.colors.backPrimary
                    )
                    TextWithColorBox(
                        "Back Secondary",
                        AppTodoTheme.colors.labelPrimary,
                        AppTodoTheme.colors.backSecondary
                    )
                    TextWithColorBox(
                        "Back Elevated",
                        AppTodoTheme.colors.labelPrimary,
                        AppTodoTheme.colors.backElevated
                    )

                }
            }
        }
    }
}

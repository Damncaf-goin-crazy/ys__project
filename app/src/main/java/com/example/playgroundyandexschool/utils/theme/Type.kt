package com.example.playgroundyandexschool.utils.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playgroundyandexschool.R

val robotoFamily = FontFamily(
    Font(R.font.regular),
    Font(R.font.medium),
    Font(R.font.bold),
)

val largeTitleTextStyle = TextStyle(
    fontFamily = robotoFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 32.sp,
)

val titleTextStyle = TextStyle(
    fontFamily = robotoFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 32.sp,
)

val buttonTextStyle = TextStyle(
    fontFamily = robotoFamily,
    fontSize = 14.sp,
    letterSpacing = 0.5.sp
)


val bodyTextStyle = TextStyle(
    fontFamily = robotoFamily,
    fontSize = 16.sp,
    letterSpacing = 0.5.sp
)

val subHeadTextStyle = TextStyle(
    fontFamily = robotoFamily,
    fontWeight = FontWeight.Light,
    fontSize = 14.sp,
    letterSpacing = 0.5.sp
)

@Preview(name = "Text Preview")
@Composable
fun TextPreview() {
    AppTodoTheme(darkTheme = true) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.LightGray,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(400.dp, 400.dp)
                        .padding(8.dp)
                        .background(color = AppTodoTheme.colors.colorWhite),
                    contentAlignment = Alignment.TopCenter

                ) {
                    Column {
                        Spacer(Modifier.height(32.dp))
                        Text(text = "Large title — 32/38", style = largeTitleTextStyle)
                        Spacer(Modifier.height(32.dp))
                        Text(text = "Title — 20/32", style = titleTextStyle)
                        Spacer(Modifier.height(32.dp))
                        Text(text = "BUTTON — 14/24", style = buttonTextStyle)
                        Spacer(Modifier.height(32.dp))
                        Text(text = "Body — 16/20", style = bodyTextStyle)
                        Spacer(Modifier.height(32.dp))
                        Text(text = "Subhead — 14/20", style = subHeadTextStyle)
                    }
                }

            }
        }
    }
}
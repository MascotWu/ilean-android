package cn.net.ilean.cms.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorPalette = darkColorScheme(
    primary = Color.Green,
    onPrimary = Color.Red,
)

private val LightColorPalette = lightColorScheme(
    primary = Color.Yellow,
)

@Composable
fun LeanApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit
) {
    val colorScheme = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    } else {
        if (darkTheme) DarkColorPalette else LightColorPalette
    }

    MaterialTheme(
        colorScheme = colorScheme, typography = Typography, shapes = Shapes, content = content
    )
}
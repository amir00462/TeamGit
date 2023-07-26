package ir.dunijet.teamgit.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorPalette = lightColors(
    primary = cPrimary
)

private val DarkColorPalette = lightColors(
    primary = cPrimary ,
)

@Composable
fun TeamGitTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {

    val colors = if(darkTheme)
        DarkColorPalette
    else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors ,
        typography = Typography ,
        shapes = Shapes ,
        content = content
    )

}
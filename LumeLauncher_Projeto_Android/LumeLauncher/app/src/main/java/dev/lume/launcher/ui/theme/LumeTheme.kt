package dev.lume.launcher.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val LumeDarkColorScheme = darkColorScheme(
    primary = LumeAccent,
    onPrimary = LumeBackgroundDeep,
    secondary = LumeMuted,
    onSecondary = LumeBackgroundDeep,
    background = LumeBackground,
    onBackground = LumeText,
    surface = LumeSurface,
    onSurface = LumeText,
    surfaceVariant = LumeSurfaceSoft,
    onSurfaceVariant = LumeMuted
)

@Composable
fun LumeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LumeDarkColorScheme,
        content = content
    )
}

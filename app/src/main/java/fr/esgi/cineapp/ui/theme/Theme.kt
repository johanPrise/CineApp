package fr.esgi.cineapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val CineDarkColorScheme = darkColorScheme(
    primary = CinePrimary,
    onPrimary = CineTextPrimary,
    primaryContainer = CineSurfaceVariant,
    onPrimaryContainer = CineTextPrimary,
    secondary = CinePrimary,
    onSecondary = CineTextPrimary,
    background = CineBackground,
    onBackground = CineTextPrimary,
    surface = CineSurface,
    onSurface = CineTextPrimary,
    surfaceVariant = CineSurfaceVariant,
    onSurfaceVariant = CineTextSecondary,
    error = CineError,
    onError = CineTextPrimary
)

@Composable
fun CineAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CineDarkColorScheme,
        typography = Typography,
        content = content
    )
}

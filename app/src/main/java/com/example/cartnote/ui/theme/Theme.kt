package com.example.cartnote.ui.theme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Kopplar paletten till Material 3:s färgroller
private val CartNoteColors = lightColorScheme(
    primary = DarkGreen,
    onPrimary = Color.White,
    secondary = Green,
    onSecondary = Color.White,
    primaryContainer = Mustard,
    onPrimaryContainer = DarkGreen,
    secondaryContainer = Orange,
    onSecondaryContainer = Color(0xFF2B1A00),
    tertiary = LightBlue,
    surfaceVariant = Color(0xFFE4F2F6),
    background = Color(0xFFFCFDF9),
    surface = Color(0xFFFCFDF9)
)

// Appens tema: alltid våra egna färger
@Composable
fun CartNoteTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CartNoteColors,
        typography = Typography,
        content = content
    )
}
package ru.ragefalcon.cheatsheetandroid.compose.settings

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

class ThemeColorItem(val name: String, val color: Color, val onColor: Color)

object ColorList {
//    @Composable
    fun  getThemeColorList(scheme: ColorScheme ): List<ThemeColorItem> = listOf(
        ThemeColorItem("primary",scheme.primary, scheme.onPrimary),
        ThemeColorItem("onPrimary",scheme.onPrimary, scheme.primary),
        ThemeColorItem("primaryContainer",scheme.primaryContainer, scheme.onPrimaryContainer),
        ThemeColorItem("onPrimaryContainer",scheme.onPrimaryContainer, scheme.primaryContainer),
        ThemeColorItem("secondary",scheme.secondary, scheme.onSecondary),
        ThemeColorItem("onSecondary",scheme.onSecondary, scheme.secondary),
        ThemeColorItem("secondaryContainer",scheme.secondaryContainer, scheme.onSecondaryContainer),
        ThemeColorItem("onSecondaryContainer",scheme.onSecondaryContainer, scheme.secondaryContainer),
        ThemeColorItem("tertiary",scheme.tertiary, scheme.onTertiary),
        ThemeColorItem("onTertiary",scheme.onTertiary, scheme.tertiary),
        ThemeColorItem("tertiaryContainer",scheme.tertiaryContainer, scheme.onTertiaryContainer),
        ThemeColorItem("onTertiaryContainer",scheme.onTertiaryContainer, scheme.tertiaryContainer),
        ThemeColorItem("error",scheme.error, scheme.onError),
        ThemeColorItem("onError",scheme.onError, scheme.error),
        ThemeColorItem("errorContainer",scheme.errorContainer, scheme.onErrorContainer),
        ThemeColorItem("onErrorContainer",scheme.onErrorContainer, scheme.errorContainer),
        ThemeColorItem("background",scheme.background, scheme.onBackground),
        ThemeColorItem("onBackground",scheme.onBackground, scheme.background),
        ThemeColorItem("surface",scheme.surface, scheme.onSurface),
        ThemeColorItem("onSurface",scheme.onSurface, scheme.surface),
        ThemeColorItem("surfaceVariant",scheme.surfaceVariant, scheme.onSurfaceVariant),
        ThemeColorItem("onSurfaceVariant",scheme.onSurfaceVariant, scheme.surfaceVariant),
        ThemeColorItem("inverseSurface",scheme.inverseSurface, scheme.inverseOnSurface),
        ThemeColorItem("inverseOnSurface",scheme.inverseOnSurface, scheme.inverseSurface),
        ThemeColorItem("outline",scheme.outline, scheme.primary)
    )
}
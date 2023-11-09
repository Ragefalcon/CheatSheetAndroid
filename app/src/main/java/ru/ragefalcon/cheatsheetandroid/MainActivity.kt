package ru.ragefalcon.cheatsheetandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import dagger.hilt.android.AndroidEntryPoint
import ru.ragefalcon.cheatsheetandroid.activity.LifecycleTestActivity
import ru.ragefalcon.cheatsheetandroid.compose.lifecycles.LifecyclesCheatList
import ru.ragefalcon.cheatsheetandroid.di.OrientationState
import ru.ragefalcon.cheatsheetandroid.ui.theme.CheatSheetAndroidTheme
import ru.ragefalcon.cheatsheetandroid.viewmodels.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var orientationState: OrientationState

    val settins: SettingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var checkFirstStart = true
        var startDestination: (NavController) -> Unit = { }
        if (LifecycleTestActivity.getStartStateFromSP(this)) {
            startDestination = { navContr ->
                if (checkFirstStart) navContr.navigate("lifecycleCheat/{${LifecyclesCheatList.ActivityCheat.name}}")
                checkFirstStart = false
            }
            LifecycleTestActivity.startLifecycleTestActivity(this)
        }

        setContent {
            val darkTheme = shouldUseDarkTheme(settins.userPreferences.screenSettings.value)
            CheatSheetAndroidTheme(
                darkTheme = darkTheme,
                androidTheme = shouldUseAndroidTheme(settins.userPreferences.screenSettings.value),
                disableDynamicTheming = shouldDisableDynamicTheming(settins.userPreferences.screenSettings.value)
            ) {
                CheatSheetApp(orientationState, startDestination)
            }
        }

    }

}


/**
 * Returns `true` if the Android theme should be used, as a function of the [uiState].
 */
@Composable
fun shouldUseAndroidTheme(
    uiState: UserScreenSettings,
): Boolean = when (uiState.brand) {
        ThemeBrand.DEFAULT -> false
        ThemeBrand.ANDROID -> true

}

/**
 * Returns `true` if the dynamic color is disabled, as a function of the [uiState].
 */
@Composable
fun shouldDisableDynamicTheming(
    uiState: UserScreenSettings,
): Boolean = !uiState.useDynamicColor

/**
 * Returns `true` if dark theme should be used, as a function of the [uiState] and the
 * current system context.
 */
@Composable
fun shouldUseDarkTheme(
    uiState: UserScreenSettings,
): Boolean = when (uiState.darkThemeConfig) {
    DarkThemeConfig.FOLLOW_SYSTEM -> isSystemInDarkTheme()
    DarkThemeConfig.LIGHT -> false
    DarkThemeConfig.DARK -> true

}

/**
 * The default light scrim, as defined by androidx and the platform:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=35-38;drc=27e7d52e8604a080133e8b842db10c89b4482598
 */
private val lightScrim = android.graphics.Color.argb(0xe6, 0xFF, 0xFF, 0xFF)

/**
 * The default dark scrim, as defined by androidx and the platform:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=40-44;drc=27e7d52e8604a080133e8b842db10c89b4482598
 */
private val darkScrim = android.graphics.Color.argb(0x80, 0x1b, 0x1b, 0x1b)

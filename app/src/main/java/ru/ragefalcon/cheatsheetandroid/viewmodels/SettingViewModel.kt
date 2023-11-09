package ru.ragefalcon.cheatsheetandroid.viewmodels

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Singleton

@HiltViewModel
class SettingViewModel @Inject constructor(
    val userPreferences: UserPreferences
): ViewModel() {

}


class UserPreferences(private val context: Context) {

    private val sharPrefFile = "UserPrefFile"
    private val sharPref_brand = "UserPreferenc_brand"
    private val sharPref_useDynamicColor = "UserPreferenc_useDynamicColor"
    private val sharPref_darkThemeConfig = "UserPreferenc_darkThemeConfig"
    private var _screenSettings = mutableStateOf(UserScreenSettings(ThemeBrand.DEFAULT, true, DarkThemeConfig.FOLLOW_SYSTEM))
    var screenSettings: State<UserScreenSettings> = _screenSettings


    init {
        val sharedPreferences = context.getSharedPreferences(sharPrefFile, MODE_PRIVATE)

        val brand = when (sharedPreferences.getString(sharPref_brand, ThemeBrand.DEFAULT.name)) {
            ThemeBrand.ANDROID.name -> ThemeBrand.ANDROID
            else -> ThemeBrand.DEFAULT
        }
        val useDynamicColor = sharedPreferences.getBoolean(sharPref_useDynamicColor, true)
        val darkThemeConfig = when (sharedPreferences.getString(sharPref_darkThemeConfig, DarkThemeConfig.FOLLOW_SYSTEM.name)) {
            DarkThemeConfig.LIGHT.name -> DarkThemeConfig.LIGHT
            DarkThemeConfig.DARK.name -> DarkThemeConfig.DARK
            else -> DarkThemeConfig.FOLLOW_SYSTEM
        }
        _screenSettings.value = UserScreenSettings(brand,useDynamicColor, darkThemeConfig)
    }

    fun setBrand(newBrand: ThemeBrand) {
        val sharedPreferencesEditor = context.getSharedPreferences(sharPrefFile, MODE_PRIVATE).edit()

        sharedPreferencesEditor.putString(sharPref_brand, newBrand.name)
        sharedPreferencesEditor.apply()

        _screenSettings.value = screenSettings.value.copy(brand = newBrand)
    }

    fun setUseDynamicColor(newUseDynamicColor: Boolean) {
        val sharedPreferencesEditor = context.getSharedPreferences(sharPrefFile, MODE_PRIVATE).edit()

        sharedPreferencesEditor.putBoolean(sharPref_useDynamicColor, newUseDynamicColor)
        sharedPreferencesEditor.apply()

        _screenSettings.value = screenSettings.value.copy(useDynamicColor = newUseDynamicColor)
    }

    fun setDarkThemeConfig(newDarkThemeConfig: DarkThemeConfig) {
        val sharedPreferencesEditor = context.getSharedPreferences(sharPrefFile, MODE_PRIVATE).edit()

        sharedPreferencesEditor.putString(sharPref_darkThemeConfig, newDarkThemeConfig.name)
        sharedPreferencesEditor.apply()

        _screenSettings.value = screenSettings.value.copy(darkThemeConfig = newDarkThemeConfig)
    }


}

data class UserScreenSettings(
    val brand: ThemeBrand,
    val useDynamicColor: Boolean,
    val darkThemeConfig: DarkThemeConfig,
)


enum class ThemeBrand {
    DEFAULT, ANDROID
}

enum class DarkThemeConfig {
    FOLLOW_SYSTEM, LIGHT, DARK
}

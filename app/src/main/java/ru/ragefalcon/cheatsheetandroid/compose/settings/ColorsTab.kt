package ru.ragefalcon.cheatsheetandroid.compose.settings

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.ragefalcon.cheatsheetandroid.compose.helpers.ItemUsefulLink
import ru.ragefalcon.cheatsheetandroid.compose.helpers.UsefulLinks
import ru.ragefalcon.cheatsheetandroid.di.OrientationState
import ru.ragefalcon.cheatsheetandroid.ui.theme.supportsDynamicTheming
import ru.ragefalcon.cheatsheetandroid.viewmodels.SettingViewModel

@Composable
fun ColorsTab(
    orientationState: OrientationState,
) {
    if (orientationState.orientation == Configuration.ORIENTATION_PORTRAIT) Column(
        Modifier.padding(40.dp).verticalScroll(rememberScrollState())
    ) {
        setPanel()
        colorList()
    } else Row(Modifier.padding(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState())) {
            setPanel()
        }
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState())) {
            colorList()
        }
    }
}

@Composable
private fun ColumnScope.setPanel(settingsViewModel: SettingViewModel = hiltViewModel<SettingViewModel>()) {
    val listLink = listOf(
        ItemUsefulLink(
            title = "Material Design 3",
            link = "https://m3.material.io/"
        ),
        ItemUsefulLink(
            title = "M3 Styles",
            link = "https://m3.material.io/styles"
        ),
        ItemUsefulLink(
            title = "Theme.kt in \"Now In Android\" on GitHub",
            link = "https://github.com/android/nowinandroid/blob/main/core/designsystem/src/main/kotlin/com/google/samples/apps/nowinandroid/core/designsystem/theme/Theme.kt"
        ),
    )
    UsefulLinks(listLink)
    SettingsPanel(
        settingsViewModel.userPreferences.screenSettings.value,
        supportsDynamicTheming(),
        settingsViewModel.userPreferences::setBrand,
        settingsViewModel.userPreferences::setUseDynamicColor,
        settingsViewModel.userPreferences::setDarkThemeConfig
    )
}

@Composable
private fun ColumnScope.colorList() {
    SettingsDialogSectionTitle("Theme colors")
    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
        ColorList.getThemeColorList(MaterialTheme.colorScheme).forEach {
//            Card(colors = CardDefaults.cardColors(it.color), elevation = CardDefaults.cardElevation(1.dp)) {
            Surface(shape = CardDefaults.shape, color = it.color, contentColor = it.onColor, shadowElevation = 2.dp){
                Text(
                    it.name,
//                    color = it.onColor,
                    modifier = Modifier
                        .padding(top = 5.dp, start = 15.dp, bottom = 20.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}
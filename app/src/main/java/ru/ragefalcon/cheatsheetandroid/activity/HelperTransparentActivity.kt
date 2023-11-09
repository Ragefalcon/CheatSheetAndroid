package ru.ragefalcon.cheatsheetandroid.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import ru.ragefalcon.cheatsheetandroid.R
import ru.ragefalcon.cheatsheetandroid.shouldDisableDynamicTheming
import ru.ragefalcon.cheatsheetandroid.shouldUseAndroidTheme
import ru.ragefalcon.cheatsheetandroid.shouldUseDarkTheme
import ru.ragefalcon.cheatsheetandroid.ui.theme.CheatSheetAndroidTheme
import ru.ragefalcon.cheatsheetandroid.viewmodels.SettingViewModel
import ru.ragefalcon.database.logging.LogApi
import javax.inject.Inject


@AndroidEntryPoint
class HelperTransparentActivity: ComponentActivity() {
    @Inject
    lateinit var logApi: LogApi

    val settins: SettingViewModel by viewModels()

    private var enableLogging = false
    private var destroyLogging = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableLogging = intent.getBooleanExtra("enableLogging", false)
        destroyLogging = intent.getBooleanExtra("destroyLogging", false)
        Log.d("MyTag", "destroyLogging = ${destroyLogging}")
        if (enableLogging) logApi.toCheatLog(
            LogApi.Tag.ACTIVITY_TR_LIFECYCLE,
            getString(R.string.label_oncreate),
            getString(R.string.comment_oncreate)
        )


        setContent {
            BackHandler {
                if (destroyLogging) logApi.toCheatLog(
                    LogApi.Tag.ACTION_ACTIVITY_LIFECYCLE,
                    getString(R.string.log_line_close_transparent_activity),
                    getString(R.string.log_comment_close_transparent_activity)
                )
                finish()
            }

            SideEffect {
                window.statusBarColor = Color.Transparent.toArgb()
                window.navigationBarColor = Color.Transparent.toArgb()
            }
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Surface(shape = RoundedCornerShape(5.dp), shadowElevation = 2.dp) {
                    val darkTheme = shouldUseDarkTheme(settins.userPreferences.screenSettings.value)
//                    CheatSheetAndroidTheme(
//                        darkTheme = darkTheme,
//                        androidTheme = shouldUseAndroidTheme(settins.userPreferences.screenSettings.value),
//                        disableDynamicTheming = shouldDisableDynamicTheming(settins.userPreferences.screenSettings.value)
//                    ){
                        Box(Modifier.padding(40.dp), contentAlignment = Alignment.Center) {
                            Button(onClick = {
                                onBackPressedDispatcher.onBackPressed()
                            }) {
                                Text(stringResource(R.string.button_text_close))
                            }
                        }
//                    }
                }
            }
        }
    }
    override fun onStart() {
        super.onStart()
        if (enableLogging) logApi.toCheatLog(
            LogApi.Tag.ACTIVITY_TR_LIFECYCLE,
            getString(R.string.label_onstart),
            getString(R.string.comment_onstart)
        )
    }

    override fun onRestart() {
        super.onRestart()
        if (enableLogging) logApi.toCheatLog(
            LogApi.Tag.ACTIVITY_TR_LIFECYCLE,
            getString(R.string.label_onrestart),
            getString(R.string.comment_onrestart)
        )
    }

    override fun onResume() {
        super.onResume()
        if (enableLogging) logApi.toCheatLog(
            LogApi.Tag.ACTIVITY_TR_LIFECYCLE,
            getString(R.string.label_onresume),
            getString(R.string.comment_onresume)
        )
    }

    override fun onPause() {
        super.onPause()
        Log.d("MyTag", "onPause - LogApi.Tag.ACTIVITY_TR_LIFECYCLE")
        if (enableLogging) logApi.toCheatLog(
            LogApi.Tag.ACTIVITY_TR_LIFECYCLE,
            getString(R.string.label_onpause),
            getString(R.string.comment_onpause)
        )
    }

    override fun onStop() {
        super.onStop()
        if (enableLogging) logApi.toCheatLog(
            LogApi.Tag.ACTIVITY_TR_LIFECYCLE,
            getString(R.string.label_onstop),
            getString(R.string.comment_onstop)
        )
    }

    override fun onDestroy() {
        if (enableLogging) logApi.toCheatLog(
            LogApi.Tag.ACTIVITY_TR_LIFECYCLE,
            getString(R.string.label_ondestroy),
            getString(R.string.comment_ondestroy)
        )

        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        logApi.toCheatLog(
            LogApi.Tag.ACTIVITY_TR_LIFECYCLE,
            getString(R.string.label_onsaveinstancestate),
            getString(R.string.comment_onsaveinstancestate)
        )
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        logApi.toCheatLog(
            LogApi.Tag.ACTIVITY_TR_LIFECYCLE,
            getString(R.string.label_onrestoreinstancestate),
            getString(R.string.comment_onrestoreinstancestate)
        )
    }

}
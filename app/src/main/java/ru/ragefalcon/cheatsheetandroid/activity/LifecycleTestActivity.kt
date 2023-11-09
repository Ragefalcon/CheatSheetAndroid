package ru.ragefalcon.cheatsheetandroid.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dagger.hilt.android.AndroidEntryPoint
import ru.ragefalcon.cheatsheetandroid.R
import ru.ragefalcon.cheatsheetandroid.compose.helpers.DrillingState
import ru.ragefalcon.cheatsheetandroid.compose.helpers.ExpandStateById
import ru.ragefalcon.cheatsheetandroid.compose.helpers.OrientationBox
import ru.ragefalcon.cheatsheetandroid.compose.lifecycles.activities.*
import ru.ragefalcon.cheatsheetandroid.di.OrientationState
import ru.ragefalcon.cheatsheetandroid.extensions.screenValue
import ru.ragefalcon.cheatsheetandroid.extensions.toStringFormat
import ru.ragefalcon.cheatsheetandroid.shouldDisableDynamicTheming
import ru.ragefalcon.cheatsheetandroid.shouldUseAndroidTheme
import ru.ragefalcon.cheatsheetandroid.shouldUseDarkTheme
import ru.ragefalcon.cheatsheetandroid.ui.theme.CheatSheetAndroidTheme
import ru.ragefalcon.cheatsheetandroid.ui.theme.MyColor
import ru.ragefalcon.cheatsheetandroid.viewmodels.SettingViewModel
import ru.ragefalcon.database.logging.LogApi
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class LifecycleTestActivity : ComponentActivity() {
    @Inject
    lateinit var orientationState: OrientationState

    @Inject
    lateinit var logApi: LogApi

    val settins: SettingViewModel by viewModels()

    companion object {
        const val shareNavFile = "Navigation_Stack"
        const val sharePrefNav = "LifecycleTestActivity_Start"

        fun getStartStateFromSP(context: Context): Boolean {
            val sharedPreferences = context.getSharedPreferences(shareNavFile, MODE_PRIVATE)
            return sharedPreferences.getBoolean(sharePrefNav, false)
        }

        fun startLifecycleTestActivity(activity: Activity) {
            val intent = Intent(activity, LifecycleTestActivity::class.java)
            activity.startActivity(intent)
        }
    }

    private fun removeSharPref() {
        val sharedPreferencesEditor = this.getSharedPreferences(shareNavFile, MODE_PRIVATE).edit()
        sharedPreferencesEditor.remove(sharePrefNav)
        sharedPreferencesEditor.apply()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logApi.toCheatLog(
            LogApi.Tag.ACTIVITY_LIFECYCLE, getString(R.string.label_oncreate), getString(R.string.comment_oncreate)
        )

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                removeSharPref()
                finish()
            }
        })

        val sharedPreferencesEditor = this.getSharedPreferences(shareNavFile, MODE_PRIVATE).edit()
        sharedPreferencesEditor.putBoolean(sharePrefNav, true)
        sharedPreferencesEditor.apply()


//        val sizeScreen = LocalConfiguration.current.run { screenWidthDp to screenHeightDp }
        val sizeScreen = this.screenValue()

        setContent {
            var stretch by rememberSaveable { mutableFloatStateOf(0.4F) }
            val mapExpandLog = rememberSaveable(saver = ExpandStateById.Saver) { ExpandStateById() }
            val mapExpandTest = rememberSaveable(saver = ExpandStateById.Saver) { ExpandStateById() }
            val expand = rememberSaveable(saver = DrillingState.saver()) { DrillingState(false) }
            val tagsState = rememberSaveable(saver = LogTagsState.Saver) {
                LogTagsState(
                    listOf(
                        LogApi.Tag.USER_ACTIVITY_LIFECYCLE,
                        LogApi.Tag.ACTION_ACTIVITY_LIFECYCLE,
                        LogApi.Tag.ACTIVITY_LIFECYCLE,
                        LogApi.Tag.ACTIVITY2_LIFECYCLE,
                        LogApi.Tag.ACTIVITY_TR_LIFECYCLE
                    )
                )
            }
            val darkTheme = shouldUseDarkTheme(settins.userPreferences.screenSettings.value)
            CheatSheetAndroidTheme(
                darkTheme = darkTheme,
                androidTheme = shouldUseAndroidTheme(settins.userPreferences.screenSettings.value),
                disableDynamicTheming = shouldDisableDynamicTheming(settins.userPreferences.screenSettings.value)
            ) {
                OrientationBox(orientationState) { modifierWeight ->
                    Scaffold(modifier = modifierWeight, topBar = {
                        TopAppBar(
                            title = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton({
                                        onBackPressedDispatcher.onBackPressed()
                                    }) {
                                        Icon(
                                            Icons.Filled.ArrowBack,
                                            contentDescription = stringResource(R.string.content_description_navigation_up)
                                        )
                                    }
                                    Text(this@LifecycleTestActivity.title.toString())
                                }
                            },
                        )
                    }) {
                        Surface(
                            modifier = Modifier.padding(it).fillMaxSize(), color = MaterialTheme.colorScheme.background
                        ) {
                            TestActivityList(mapExpandTest, expand) { lineText, comment ->
                                logApi.toCheatLog(
                                    LogApi.Tag.ACTION_ACTIVITY_LIFECYCLE, lineText, comment
                                )
                            }
                        }
                    }
                    LogView(
                        logApi,
                        Modifier,
                        sizeScreen,
                        orientationState.orientation == Configuration.ORIENTATION_LANDSCAPE,
                        stretch = stretch,
                        stretchChange = { stretch = it },
                        mapExpand = mapExpandLog,
                        tagsState = tagsState
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        logApi.toCheatLog(
            LogApi.Tag.ACTIVITY_LIFECYCLE, getString(R.string.label_onstart), getString(R.string.comment_onstart)
        )
    }

    override fun onRestart() {
        super.onRestart()
        logApi.toCheatLog(
            LogApi.Tag.ACTIVITY_LIFECYCLE, getString(R.string.label_onrestart), getString(R.string.comment_onrestart)
        )
    }

    override fun onResume() {
        super.onResume()
        logApi.toCheatLog(
            LogApi.Tag.ACTIVITY_LIFECYCLE, getString(R.string.label_onresume), getString(R.string.comment_onresume)
        )
    }

    override fun onPause() {
        super.onPause()
        Log.d("MyTag", "onPause - LogApi.Tag.ACTIVITY_LIFECYCLE")
        logApi.toCheatLog(
            LogApi.Tag.ACTIVITY_LIFECYCLE, getString(R.string.label_onpause), getString(R.string.comment_onpause)
        )
    }

    override fun onStop() {
        super.onStop()
        logApi.toCheatLog(
            LogApi.Tag.ACTIVITY_LIFECYCLE, getString(R.string.label_onstop), getString(R.string.comment_onstop)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        logApi.toCheatLog(
            LogApi.Tag.ACTIVITY_LIFECYCLE, getString(R.string.label_ondestroy), getString(R.string.comment_ondestroy)
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("testKey", "testValue")
        logApi.toCheatLog(
            LogApi.Tag.ACTIVITY_LIFECYCLE,
            getString(R.string.label_onsaveinstancestate),
            getString(R.string.comment_onsaveinstancestate)
        )
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        logApi.toCheatLog(
            LogApi.Tag.ACTIVITY_LIFECYCLE,
            getString(R.string.label_onrestoreinstancestate),
            getString(R.string.comment_onrestoreinstancestate)
        )
    }

}

@Composable
private fun TestActivityList(
    mapExpand: ExpandStateById, expand: DrillingState<Boolean>, actionLog: (lineText: String, comment: String) -> Unit
) {

    TestListForActivity.entries.toTypedArray().let { arrayTest ->
        LazyColumn(Modifier.padding(vertical = 10.dp)) {
            item {
                Box(
                    Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
//        elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(15.dp),
                        contentColor = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier
                            .animateContentSize()
                    ) {
                        SmallDescription(
                            """
                        Для всех Activity участвующих в тестах в соответствующих методах делаются записи в Logs:
                            - onCreate
                            - onStart
                            - onRestart
                            - onResume
                            - onPause
                            - onStop
                            - onDestroy
                            - onSaveInstanceState
                            - onRestoreInstanceState
                        
                        Можно посмотреть когда и какие методы вызываются в зависимости от того как происходит взаимодействие с пользователем.
                        Ниже приведены различные варианты сценариев, а также есть функционал для их реализации. 
                    """.trimIndent(),
                            expand
                        )
                    }
                }
            }
            items(arrayTest) { item ->
                ItemTestActivity(item, actionLog, mapExpand.getExpand(item.number.toLong())) {
                    mapExpand.setExpand(item.number.toLong(), it)
                }
            }
        }
    }
}


@Composable
fun SmallDescription(description: String, expand: DrillingState<Boolean>) {
//    var expand by remember { mutableStateOf(false) }
    val rotate by animateFloatAsState(if (expand.state) 180f else 0f, label = "rotate icon")
    val listButton: List<Pair<Int, String>> = listOf(
        R.drawable.vi_baseline_filter_alt_24 to "можно настроить какие записи будут отображаться",
        R.drawable.vi_baseline_power_input_24 to "добавляет в Logs строку  \"--- * --- * --- * ---\"",
        R.drawable.baseline_delete_24 to "полностью очищает Logs",
        R.drawable.vi_baseline_swipe_vertical_24 to "можно растянуть/сжать панель Logs потянув за заголовок"
    )
    Column(Modifier
        .padding(horizontal = 15.dp, vertical = 10.dp)
        .fillMaxWidth()
        .clip(RoundedCornerShape(10.dp))
        .animateContentSize(tween(300))
        .clickable {
            expand.changeState(expand.state.not())
        }) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Краткое описание",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 2.dp).weight(1f)
            )
            Icon(Icons.Filled.ArrowDropDown, contentDescription = null, modifier = Modifier.graphicsLayer {
                rotationZ = rotate
            })
        }
        if (expand.state) {
            Text(
                description,
//                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(start = 15.dp, top = 10.dp, bottom = 10.dp)
            )
            Text(
                "Памятка",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 5.dp, bottom = 10.dp)
            )
            listButton.forEach {
                Row(
                    modifier = Modifier.padding(start = 15.dp, bottom = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(painterResource(it.first), contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
                    Text(
                        it.second,
//                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(start = 5.dp)
                    )
                }
            }
        }
    }
}

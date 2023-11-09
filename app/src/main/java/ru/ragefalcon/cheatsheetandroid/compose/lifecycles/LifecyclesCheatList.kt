package ru.ragefalcon.cheatsheetandroid.compose.lifecycles

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.ragefalcon.cheatsheetandroid.activity.LifecycleTestActivity
import ru.ragefalcon.cheatsheetandroid.ui.theme.MyColor
import ru.ragefalcon.cheatsheetandroid.R
import ru.ragefalcon.cheatsheetandroid.compose.flowgame.FlowGame01
import ru.ragefalcon.cheatsheetandroid.compose.helpers.CardUsefulLink
import ru.ragefalcon.cheatsheetandroid.compose.helpers.ItemUsefulLink
import ru.ragefalcon.cheatsheetandroid.compose.helpers.UsefulLinks

enum class LifecyclesCheatList(val title: String) {
    ActivityCheat("Activities lifecycle"),
    FlowCheat("FlowCheat"),
    ServiceCheat("Services"),
    ContentProviderCheat("ContentProvider");

    companion object {
        fun getCheat(name: String): LifecyclesCheatList = entries.find { it.name == name } ?: ActivityCheat
    }

}


@Composable
fun LifecyclesCheatList.getScreen(modifier: Modifier = Modifier.fillMaxSize()) {
    val activity = (LocalContext.current as Activity)

    when (this) {
        LifecyclesCheatList.ActivityCheat -> {
            val listLink = listOf(
                ItemUsefulLink("Android Lifecycles Cheat Sheet" to "https://docs.google.com/drawings/d/1UDBkX4KE1K5ZWQ_wRw0ffXUSxzaaosddJxsRtRxDREQ/edit"),
                ItemUsefulLink("The activity lifecycle" to "https://developer.android.com/guide/components/activities/activity-lifecycle"),
                ItemUsefulLink("Android Activity Lifecycle and Fragment Lifecycle, States and Method Descriptions" to "https://basaransuleyman.medium.com/android-activity-lifecycle-and-fragment-lifecycle-states-and-method-descriptions-136efc3c2ff3")
            )
            Column(
                modifier = modifier.padding(bottom = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                UsefulLinks(listLink,Modifier.weight(1f).padding(horizontal = 15.dp, vertical = 15.dp))
                Button({
                    LifecycleTestActivity.startLifecycleTestActivity(activity)
                }) {
                    Text("Start test Activity with Logging", fontSize = 20.sp, modifier = Modifier.padding(10.dp))
                }

            }
        }

        LifecyclesCheatList.FlowCheat -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
            ) {
                FlowGame01()
            }
        }

        LifecyclesCheatList.ServiceCheat -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
            ) {
                Text("ServiceCheat")
            }
        }

        LifecyclesCheatList.ContentProviderCheat -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
            ) {
                Text("ContentProviderCheat")
            }
        }
    }
}

package ru.ragefalcon.cheatsheetandroid.compose.helpers

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.ragefalcon.cheatsheetandroid.di.OrientationState

@Composable
fun OrientationBox(orientationState: OrientationState, content: @Composable (Modifier) -> Unit) {
    if (orientationState.orientation == Configuration.ORIENTATION_PORTRAIT) Column {
        content(Modifier.weight(1f))
    } else Row {
        content(Modifier.weight(1f))
    }
}
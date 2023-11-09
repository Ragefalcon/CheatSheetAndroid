package ru.ragefalcon.cheatsheetandroid.compose.helpers

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.ragefalcon.cheatsheetandroid.R

@Composable
fun UsefulLinks(listLink: List<ItemUsefulLink>, modifier: Modifier = Modifier) {
    val activity = (LocalContext.current as Activity)
    Column(
        modifier = modifier
    ) {
        Text(
            stringResource(R.string.block_title_useful_links),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 5.dp, bottom = 10.dp).fillMaxWidth()
        )
        listLink.forEach { itemLink ->
            CardUsefulLink(activity, itemLink)
        }
    }
}
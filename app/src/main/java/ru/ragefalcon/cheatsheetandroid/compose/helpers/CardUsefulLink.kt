package ru.ragefalcon.cheatsheetandroid.compose.helpers

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.ragefalcon.cheatsheetandroid.R

@Composable
fun CardUsefulLink(activity: Activity, item: ItemUsefulLink) {

    Row(
        modifier = Modifier
            .padding(start = 15.dp, bottom = 5.dp)
            .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(15.dp))
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .clickable {
                val url = item.link
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                val pendingIntent = PendingIntent.getActivity(
                    activity,
                    23432,//NOTIFICATION_REQUEST_CODE,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE
                )

                pendingIntent.send()
            }.padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painterResource(R.drawable.vi_baseline_open_in_browser_24),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onTertiaryContainer
        )
        Text(
            item.title,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.padding(start = 5.dp)
        )
    }
}
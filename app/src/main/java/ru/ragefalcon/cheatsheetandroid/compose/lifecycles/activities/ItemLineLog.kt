package ru.ragefalcon.cheatsheetandroid.compose.lifecycles.activities

import LogLine
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.ragefalcon.cheatsheetandroid.extensions.toStringFormat
import ru.ragefalcon.cheatsheetandroid.ui.theme.CheatSheetAndroidTheme
import ru.ragefalcon.cheatsheetandroid.ui.theme.MyColor
import ru.ragefalcon.database.logging.LogApi
import java.util.*

fun LogApi.Tag.color(): Color = when (this) {
    LogApi.Tag.DEBUG -> MyColor.LightBrown
    LogApi.Tag.USER_ACTIVITY_LIFECYCLE -> MyColor.LightGreen
    LogApi.Tag.ACTIVITY_LIFECYCLE -> MyColor.LightYellow
    LogApi.Tag.ACTION_ACTIVITY_LIFECYCLE -> MyColor.WarmDarkYellow
    LogApi.Tag.ACTIVITY2_LIFECYCLE -> MyColor.WarmLightBrown
    LogApi.Tag.ACTIVITY_TR_LIFECYCLE -> MyColor.GrayGreen
}

@Composable
fun ItemLineLog(item: LogLine, expend: Boolean, expendChange: (Boolean) -> Unit) {
    val rotate by animateFloatAsState(if (expend) 180f else 0f)
    Column(Modifier.fillMaxWidth()
        .run {
            if (item.comment != "") animateContentSize().clickable {
                expendChange(expend.not())
            } else this
        }
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                Date(item.date).toStringFormat("HH:mm:ss-SSS"),
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.tertiary
            )
            Text(
                item.tag().logText,
                color = MyColor.AlmostBlack,
                modifier = Modifier
                    .padding(2.dp)
                    .background(item.tag().color(), RoundedCornerShape(5.dp))
                    .padding(horizontal = 5.dp)
            )
            Text(
                item.logText,
//                color = MyColor.AlmostBlack,
                modifier = Modifier.padding(start = 2.dp).weight(1f)
            )
            if (item.comment != "") Icon(
                Icons.Filled.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier.graphicsLayer {
                    rotationZ = rotate
                }
            )
        }
        if (expend)
            Text(
                item.comment,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(start = 5.dp, bottom = 10.dp)
            )
    }
}

@Preview(showBackground = true)
@Composable
fun ItemLineLogPreview() {
    CheatSheetAndroidTheme {
        ItemLineLog(
            LogLine(
                id = 1,
                date = 24124321421,
                tagTitle = LogApi.Tag.ACTIVITY_LIFECYCLE.tagTitle,
                logText = "Activity onCreate",
                comment = "Activity создана, но еще не видна."
            ),
            false,
            {}
        )
    }
}
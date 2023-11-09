package ru.ragefalcon.cheatsheetandroid.compose.lifecycles.activities

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import ru.ragefalcon.cheatsheetandroid.ui.theme.MyColor

@Composable
fun ItemTestActivity(
    item: TestListForActivity,
    actionLog: (lineText: String, comment: String) -> Unit,
    expend: Boolean,
    expendChange: (Boolean) -> Unit
) {
    val rotate by animateFloatAsState(if (expend) 180f else 0f)
    Box(
        Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
//        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Surface(
            color = MaterialTheme.colorScheme.secondary,
            shape = RoundedCornerShape(15.dp),
            contentColor = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier.padding(0.dp)
                .animateContentSize()
        ) {
            Column(Modifier.padding(5.dp)) {
                Row(
                    Modifier.clip(RoundedCornerShape(12.dp)).fillMaxWidth().clickable { expendChange(expend.not()) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (item.number != 0) Text(
                        "Test ${item.number}",
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier
                            .padding(2.dp)
                            .background(MaterialTheme.colorScheme.tertiaryContainer, RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp)
                    )
                    Text(
                        " ${item.title}",
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(start = 0.dp).weight(1f)
                    )
                    Icon(
                        Icons.Filled.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.graphicsLayer {
                            rotationZ = rotate
                        }
                    )
                }
                if (expend) item.openContent(this, actionLog)
            }
        }
    }
}
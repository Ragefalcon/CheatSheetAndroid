package ru.ragefalcon.cheatsheetandroid.compose.lifecycles.activities

import android.util.Size
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import ru.ragefalcon.cheatsheetandroid.R
import ru.ragefalcon.cheatsheetandroid.compose.helpers.ExpandStateById
import ru.ragefalcon.cheatsheetandroid.ui.theme.MyColor
import ru.ragefalcon.database.logging.LogApi

@Composable
fun LogView(
    logApi: LogApi,
    modifier: Modifier,
    sizeScreen: Size,
    horizontal: Boolean,
    stretch: Float,
    stretchChange: (Float) -> Unit,
    mapExpand: ExpandStateById,
    tagsState: LogTagsState
) {

    LaunchedEffect(Unit) {
        logApi.setTagFilter(tagsState.viewTags)
    }

    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        FilterDialog(
            horizontal = horizontal,
            allLogs = tagsState.viewTags,
            selected = tagsState.selectedTags.value,
            onDismiss = { showDialog = false },
            onResult = {
                tagsState.onChangeSelected(it)
                showDialog = false
            },
        )
    }

    val stateScroll = rememberLazyListState()

    (if (horizontal) sizeScreen.width else sizeScreen.height).let {
        val size = it
        Box(Modifier.background(MaterialTheme.colorScheme.background)){
            PlateLogView(modifier, horizontal, size * stretch) {
                HeaderLogView(horizontal = horizontal,
                    size = size,
                    stretch = stretch,
                    stretchChange = { stretchChange(it) },
                    userLogLine = {
                        logApi.toCheatLog(LogApi.Tag.USER_ACTIVITY_LIFECYCLE, " --- * --- * --- * --- ", "")
                    },
                    clearLog = {
                        logApi.clearLogByTag(tagsState.viewTags)
                    },
                    selectTag = { showDialog = true })
                DividerForLogView()
                logApi.mainLogLD.collectAsState().value.let { listLogs ->
                    LazyColumn(Modifier.fillMaxSize(), state = stateScroll) {
                        items(
                            items = listLogs,
                            key = { it.id }
                        ) { item ->
                            if (tagsState.selectedTags.value.contains(item.tag()))
                                ItemLineLog(item, mapExpand.getExpand(item.id)) {
                                    mapExpand.setExpand(item.id, it)
                                }
                            else Box(
                                Modifier.padding(vertical = 1.dp).height(2.dp).fillMaxWidth().background(item.tag().color())
                            )
                        }
                        item {
                            DividerForLogView()
                        }
                    }
                    LaunchedEffect(listLogs) {
                        if (listLogs.isNotEmpty()) stateScroll.animateScrollToItem(listLogs.size - 1)
                    }
                }
            }
        }
    }
}

@Composable
private fun DividerForLogView() {
    Box(
        Modifier.padding(top = 5.dp, bottom = 5.dp).background(MaterialTheme.colorScheme.onPrimaryContainer, RoundedCornerShape(5.dp))
            .height(3.dp).fillMaxWidth()
    )
}

@Composable
private fun PlateLogView(
    modifier: Modifier, horizontal: Boolean, size: Float, content: @Composable ColumnScope.() -> Unit
) {
    with(LocalDensity.current) {
        Surface(
            modifier.let {
                (size).toDp().let { sizeDp ->
                    if (horizontal) it.width(sizeDp) else it.height(sizeDp)
                }
            }.padding(10.dp),
            color = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            shape = RoundedCornerShape(20.dp),
            shadowElevation = 3.dp
        ) {
            Column(
                Modifier.padding(top = 10.dp, bottom = 20.dp).padding(horizontal = 20.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
private fun HeaderLogView(
    horizontal: Boolean,
    size: Int,
    stretch: Float,
    stretchChange: (Float) -> Unit,
    userLogLine: () -> Unit,
    clearLog: () -> Unit,
    selectTag: () -> Unit
) {
    val min = 0.3f
    val max = 0.7f
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
            .draggable(orientation = if (horizontal) Orientation.Horizontal else Orientation.Vertical,
                state = rememberDraggableState { delta ->
                    val newStretch = stretch - delta / size.toFloat()
                    when {
                        newStretch <= min -> stretchChange(min)
                        newStretch >= max -> stretchChange(max)
                        else -> stretchChange(newStretch)
                    }
                })
    ) {
        Icon(
            painter = painterResource(if (horizontal) R.drawable.vi_baseline_swipe_24 else R.drawable.vi_baseline_swipe_vertical_24),
            tint = MaterialTheme.colorScheme.secondary,
            contentDescription = if (horizontal) "Swipe horizontal" else "Swipe vertical",
            modifier = Modifier.width(25.dp).height(25.dp)
        )

        Text("Logs", fontSize = 25.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        IconButton({
            selectTag()
        }) {
            Icon(
                painter = painterResource(R.drawable.vi_baseline_filter_alt_24),
                tint = MaterialTheme.colorScheme.tertiary,
                contentDescription = "Filter Logs",
                modifier = Modifier.height(30.dp).width(30.dp)
            )
        }
        IconButton({
            userLogLine()
        }) {
            Icon(
                painter = painterResource(R.drawable.vi_baseline_power_input_24),
                tint = MaterialTheme.colorScheme.tertiary,
                contentDescription = "User line Logs",
                modifier = Modifier.height(30.dp).width(30.dp)
            )
        }
        IconButton({
            clearLog()
        }) {
            Icon(
                painter = painterResource(R.drawable.baseline_delete_24),
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = "Clear Logs",
                modifier = Modifier.height(30.dp).width(30.dp)
            )
        }
    }

}

@Composable
fun FilterLogDialog(
    horizontal: Boolean,
    allLogs: List<LogApi.Tag>,
    selected: List<LogApi.Tag>,
    onDismiss: () -> Unit,
    onResult: (List<LogApi.Tag>) -> Unit
) {
    val listTags = remember { mutableStateMapOf(*allLogs.map { it to selected.contains(it) }.toTypedArray()) }

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false), content = {
        Surface(shape = RoundedCornerShape(25.dp), modifier = Modifier.fillMaxWidth(0.8f)) {
            Column(Modifier.padding(25.dp), horizontalAlignment = Alignment.Start) {
                Text(stringResource(R.string.filter_log_title_panel), fontSize = 30.sp)

                if (horizontal) LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
//                    modifier = modifier.testTag("plant_list"),
                    contentPadding = PaddingValues(
                        horizontal = dimensionResource(id = R.dimen.card_side_margin),
                        vertical = dimensionResource(id = R.dimen.card_side_margin)
                    )
                ) {
                    items(items = listTags.toList().sortedBy { it.first.number }, key = { it.first.tagTitle }) { item ->
                        Row(
                            Modifier.padding(start = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Switch(
                                checked = item.second,
                                onCheckedChange = { listTags[item.first] = it }
                            )
                            Text(item.first.logTitle, Modifier.padding(start = 10.dp))
                        }
                    }
                } else
                    Column(
                        Modifier.padding(vertical = 10.dp)
                            .scrollable(rememberScrollState(), Orientation.Vertical)
                    ) {
                        listTags.toList().sortedBy { it.first.number }.forEach { item ->
                            Row(
                                Modifier.padding(start = 10.dp).fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Switch(
                                    checked = item.second,
                                    onCheckedChange = { listTags[item.first] = it }
                                )
                                Text(item.first.logTitle, Modifier.padding(start = 10.dp))
                            }
                        }
                    }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onDismiss) {
                        Text(stringResource(R.string.button_text_cancel))
                    }
                    Button(
                        onClick = { onResult(listTags.filter { it.value }.map { it.key }) },
                        Modifier.padding(start = 10.dp)
                    ) {
                        Text(stringResource(R.string.button_text_select))
                    }
                }
            }
        }
    })
}

@Composable
fun FilterDialog(
    horizontal: Boolean,
    allLogs: List<LogApi.Tag>,
    selected: List<LogApi.Tag>,
    onDismiss: () -> Unit,
    onResult: (List<LogApi.Tag>) -> Unit
) {
    val listTags = remember { mutableStateMapOf(*allLogs.map { it to selected.contains(it) }.toTypedArray()) }
    val configuration = LocalConfiguration.current
    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onDismiss,
        modifier = Modifier.widthIn(max = configuration.screenWidthDp.dp - 80.dp),
        confirmButton = {
            Row {
                TextButton(onDismiss) {
                    Text(stringResource(R.string.button_text_cancel))
                }
                Button(
                    onClick = { onResult(listTags.filter { it.value }.map { it.key }) },
                    Modifier.padding(start = 10.dp)
                ) {
                    Text(stringResource(R.string.button_text_select))
                }
            }
        },
        title = {
            Text(
                text = stringResource(R.string.filter_log_title_panel),
                style = MaterialTheme.typography.titleLarge,
            )
        },
        text = {
            Divider()
            Column(Modifier.verticalScroll(rememberScrollState())) {
                listTags.toList().sortedBy { it.first.number }.forEach { item ->
                    Row(
                        Modifier.padding(start = 10.dp).fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Switch(
                            checked = item.second,
                            onCheckedChange = { listTags[item.first] = it }
                        )
                        Text(item.first.logTitle, Modifier.padding(start = 10.dp))
                    }
                }
                Divider(Modifier.padding(top = 8.dp))
            }
        }
    )
}
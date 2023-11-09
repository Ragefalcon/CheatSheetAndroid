package ru.ragefalcon.cheatsheetandroid.compose.flowgame

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.ragefalcon.cheatsheetandroid.ui.theme.MyColor

@Composable
fun FlowGame01() {
    val rezList = remember { mutableStateListOf<EmitElementItem>() }
    var start by remember { mutableStateOf(false) }
    var mainOffset: () -> Offset = remember { { Offset.Zero } }
    var boxOffset1: () -> Offset = remember { { Offset.Zero } }
    var boxOffset2: () -> Offset = remember { { Offset.Zero } }
    var animBox by remember { mutableStateOf(false) }
    var animOffset by remember { mutableStateOf(IntOffset.Zero) }
    var animOffset2 by remember { mutableStateOf(IntOffset.Zero) }
    LaunchedEffect(animBox) {
        if (animBox) {
            delay(500)
            animBox = false
        }
    }
    Box(Modifier.fillMaxSize()) {
        Column(Modifier.onGloballyPositioned {
            mainOffset = { it.localToWindow(Offset.Zero) }
        }.padding(5.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            LaunchBox(
                start = start,
                onFinish = { start = false },
                saveOffset = { boxOffset1 = { it } },
                onEmitElement = {
                    rezList.add(it)
                    animOffset =
                        IntOffset((boxOffset1().x - mainOffset().x).toInt(), (boxOffset1().y - mainOffset().y).toInt())
                    animOffset2 =
                        IntOffset((boxOffset2().x - mainOffset().x).toInt(), (boxOffset2().y - mainOffset().y).toInt())
                    animBox = true
                }
            )
            Spacer(Modifier.weight(1f))
            PrintBox(rezList,
                saveOffset = { boxOffset2 = { it } })
            Row {
                Button(onClick = {
                    rezList.clear()
                    start = true
                }) {
                    Text("Start")
                }
            }
        }
        if (animBox) {
            var startO by remember { mutableStateOf(false) }
            val aX by animateFloatAsState(if(startO) animOffset2.x.toFloat() else animOffset.x.toFloat(), label = "", animationSpec = tween(400))
            val aY by animateFloatAsState(if(startO) animOffset2.y.toFloat() else animOffset.y.toFloat(), label = "", animationSpec = tween(400))
            Box(Modifier.absoluteOffset {
                IntOffset(aX.toInt(),aY.toInt())
//                animOffset
//            IntOffset((boxOffset1().x - mainOffset().x).toInt(), (boxOffset1().y - mainOffset().y).toInt())
            }) {
                EmitElement(EmitElementItem(RectangleShape, MyColor.WarmDarkYellow))
            }
            LaunchedEffect(Unit){
                startO = true
            }
        }
    }
}

@Composable
fun ConnectBox(saveOffset: (Offset) -> Unit) {
    Box(Modifier.border(1.dp, Color.Green).width(40.dp).height(40.dp).padding(5.dp).onGloballyPositioned {
        saveOffset(it.localToWindow(Offset.Zero))
    })
}

@Composable
fun LaunchBox(
    start: Boolean,
    onFinish: () -> Unit,
    saveOffset: (Offset) -> Unit,
    onEmitElement: (EmitElementItem) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var count by remember { mutableStateOf(0f) }
    val width by animateFloatAsState(
        targetValue = count, //if (count % 2 == 0) 1f else 0f,
        animationSpec = tween(durationMillis = 100, easing = FastOutSlowInEasing),
        label = "animated timer"
    )
    LaunchedEffect(start) {
        if (start) {
            coroutineScope.launch {
                for (i in 1..5) {
                    count = 0f
                    for (i in 1..10) {
                        delay(100)
                        count += 0.1f
                    }
                    onEmitElement(EmitElementItem(RectangleShape, MyColor.WarmDarkYellow))
                }
                onFinish()
            }
        }
    }
    Surface(shape = RoundedCornerShape(2.dp), shadowElevation = 2.dp) {
        Row {
            Column(
                Modifier.weight(1f).padding(5.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text("LaunchBox box")
                Box(Modifier.height(3.dp).fillMaxWidth(width).background(Color.Red))
            }
            ConnectBox(saveOffset)
        }
    }
}

@Composable
fun PrintBox(
    getList: List<EmitElementItem>,
    saveOffset: (Offset) -> Unit
) {
    Surface(shape = RoundedCornerShape(2.dp), shadowElevation = 2.dp) {
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
            Text("print box")
            Row(
                Modifier.horizontalScroll(rememberScrollState()).padding(5.dp).heightIn(30.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                ConnectBox(saveOffset)
                getList.forEach {
                    EmitElement(it)
                }
            }
        }
    }
}

data class EmitElementItem(val shape: Shape, val color: Color)

@Composable
fun EmitElement(item: EmitElementItem, num: Int? = null) {
    Box(Modifier.height(30.dp).width(30.dp).background(item.color, item.shape), contentAlignment = Alignment.Center) {
        num?.let {
            Text(it.toString())
        }
    }
}
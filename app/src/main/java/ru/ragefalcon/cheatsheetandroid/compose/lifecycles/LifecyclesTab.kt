package ru.ragefalcon.cheatsheetandroid.compose.lifecycles

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LifecyclesTab(onCheatClick: (LifecyclesCheatList)->Unit) {
    LazyColumn(modifier = Modifier.padding(20.dp).fillMaxSize()) {
        itemsIndexed(LifecyclesCheatList.entries.toTypedArray()){ index, item ->
            LifecycleCheatItem(item) {
                onCheatClick(item)
            }
        }
    }
}


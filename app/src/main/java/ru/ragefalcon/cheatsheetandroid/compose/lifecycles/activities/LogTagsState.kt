package ru.ragefalcon.cheatsheetandroid.compose.lifecycles.activities

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import ru.ragefalcon.database.logging.LogApi

class LogTagsState(val viewTags: List<LogApi.Tag>) {
    private val _selectedTags = mutableStateOf(viewTags)
    val selectedTags: State<List<LogApi.Tag>> = _selectedTags
    fun onChangeSelected(select: List<LogApi.Tag>) {
        _selectedTags.value = select
    }

    companion object {
        val Saver: Saver<LogTagsState, *> = listSaver(save = { listOf(it.viewTags, it.selectedTags.value) }, restore = {
            LogTagsState(it[0]).apply { onChangeSelected(it[1]) }
        })
    }
}
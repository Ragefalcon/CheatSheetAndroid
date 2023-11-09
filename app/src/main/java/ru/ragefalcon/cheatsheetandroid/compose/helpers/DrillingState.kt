package ru.ragefalcon.cheatsheetandroid.compose.helpers

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import java.io.Serializable

class DrillingState<T> (startValue: T) where T : Serializable {
    private val _drillState = mutableStateOf(startValue)

    val state: T by derivedStateOf { _drillState.value }

    fun changeState(newValue: T) {
        _drillState.value = newValue
    }

    companion object {
        fun <T> saver(): Saver<DrillingState<T>, *>  where T : Serializable = listSaver(save = { listOf(it._drillState.value) }, restore = {
            DrillingState<T>(it[0])
        })
    }
}
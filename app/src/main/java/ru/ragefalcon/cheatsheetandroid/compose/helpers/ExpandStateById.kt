package ru.ragefalcon.cheatsheetandroid.compose.helpers

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver

class ExpandStateById(startList: List<Pair<Long, Boolean>> = listOf()) {
    private val mapExpand = mutableStateMapOf<Long, Boolean>(*startList.toTypedArray())

    fun getExpand(id: Long): Boolean {
        mapExpand[id]?.let {
            return it
        } ?: run {
            mapExpand[id] = false
            return mapExpand[id] ?: false
        }
    }

    fun setExpand(id: Long, value: Boolean) {
        mapExpand[id] = value
    }


    companion object {
        val Saver: Saver<ExpandStateById, *> = listSaver(save = { it.mapExpand.toList() }, restore = {
            ExpandStateById(it)
        })
    }

}


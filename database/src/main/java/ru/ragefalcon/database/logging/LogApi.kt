package ru.ragefalcon.database.logging

import LogLine
import kotlinx.coroutines.flow.StateFlow

interface LogApi {

    enum class Tag (val number: Int, val tagTitle: String, val logText: String, val logTitle: String) {
        DEBUG (6, "DEBUG", "DebagTag", "Debug сообщения"),
        USER_ACTIVITY_LIFECYCLE (5, "USER_ACTIVITY_LIFECYCLE", "User", "User line"),
        ACTION_ACTIVITY_LIFECYCLE (4,"ACTION_ACTIVITY_LIFECYCLE", "Action", "Action"),
        ACTIVITY_LIFECYCLE(1, "ACTIVITY_LIFECYCLE", "Activity", "Activity main"),
        ACTIVITY2_LIFECYCLE(2, "ACTIVITY2_LIFECYCLE", "Activity new", "Activity new"),
        ACTIVITY_TR_LIFECYCLE(3, "ACTIVITY_TR_LIFECYCLE", "Activity tran", "Activity tran");

        companion object{
            fun getTag(tagTitle: String): Tag = entries.find { it.tagTitle == tagTitle } ?: DEBUG
        }
    }

    val mainLogLD: StateFlow<List<LogLine>>

    fun toCheatLog(tag: Tag, lineText: String, comment: String)

    fun setTagFilter(tag: List<LogApi.Tag>)

    fun clearLogByTag(tag: List<Tag>)

}
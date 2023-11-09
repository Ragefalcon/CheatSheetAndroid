package ru.ragefalcon.database.logging

import LogLine
import android.content.Context
import android.util.Log
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import ru.ragefalcon.database.Database
import java.util.*

internal class LogApiImpl(context: Context): LogApi {

    private val driver: SqlDriver = AndroidSqliteDriver(Database.Schema, context, "CheatSheetDB.db")
    private val database = Database(driver)
    private val mainLog = database.mainLogQueries

    private val _mainLogLD = MutableStateFlow<List<LogLine>>(listOf())

    override val mainLogLD: StateFlow<List<LogLine>>
        get() = _mainLogLD


    override fun toCheatLog(tag: LogApi.Tag, lineText: String, comment: String) {
        Log.d("MyTag", tag.tagTitle)
        mainLog.addNewLog(Date().time, tag.tagTitle, lineText, comment)
    }

    private var ctx: CompletableJob? = null

    override fun setTagFilter(tag: List<LogApi.Tag>) {
        Log.d("MyTag", "setTagFilter() = ${tag.map { it.tagTitle }}}")
        ctx?.cancel()
        ctx = Job()
        ctx?.let { job ->
            mainLog.selectByTag(tag.map { it.tagTitle })
                .asFlow()
                .mapToList(Dispatchers.Default)
                .flowOn(Dispatchers.Default)
                .onEach {
                    Log.d("MyTag", "mainLog onEach = ${it.size}")
                    _mainLogLD.value = it.map(::LogLine)
                }
                .launchIn(CoroutineScope(Dispatchers.Main + job))
        }
    }

    override fun clearLogByTag(tag: List<LogApi.Tag>) {
        mainLog.clearLogByTag(tag.map { it.tagTitle } )
    }


}
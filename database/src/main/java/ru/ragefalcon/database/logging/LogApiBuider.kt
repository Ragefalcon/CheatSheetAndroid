package ru.ragefalcon.database.logging

import android.content.Context

object LogApiBuider {
    fun build(context: Context): LogApi = LogApiImpl(context)
}
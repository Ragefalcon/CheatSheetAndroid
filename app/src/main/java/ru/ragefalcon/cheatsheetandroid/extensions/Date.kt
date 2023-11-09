package ru.ragefalcon.cheatsheetandroid.extensions

import java.text.SimpleDateFormat
import java.util.*

fun Date.toStringFormat(format: String): String {
    val dateFormat = SimpleDateFormat(format, Locale.getDefault())
    return dateFormat.format(this)
}
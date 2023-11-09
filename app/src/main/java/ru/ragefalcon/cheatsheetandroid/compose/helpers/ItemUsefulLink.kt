package ru.ragefalcon.cheatsheetandroid.compose.helpers

data class ItemUsefulLink(val title: String, val link: String) {
    constructor(pair: Pair<String, String>) : this(pair.first, pair.second)
}
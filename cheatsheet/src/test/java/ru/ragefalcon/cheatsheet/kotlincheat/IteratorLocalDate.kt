package ru.ragefalcon.cheatsheet.kotlincheat

import java.time.LocalDate


/**
 * Пример реализации итератора для закрытого диапазона.
 * Пример взят из книги "Kotlin in Action", Авторы: Светлана Исакова, Дмитрий Жемеров
 * */
operator fun ClosedRange<LocalDate>.iterator(): Iterator<LocalDate> =
    object : Iterator<LocalDate> {
        var current = start

        override fun hasNext(): Boolean =
            current <= endInclusive

        override fun next(): LocalDate = current.apply {
            current = plusDays(1)
        }

    }
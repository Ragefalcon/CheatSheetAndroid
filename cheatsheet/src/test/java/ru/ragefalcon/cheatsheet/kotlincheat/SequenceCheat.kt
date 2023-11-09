package ru.ragefalcon.cheatsheet.kotlincheat

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.annotations.TestOnly
import org.junit.Test


class CheatSequenceBlock(){

    @Test
    fun test01() {
        /**
         *  В круглых скобках () первый элемент.
         * */
        val seq1 = generateSequence(-1) {
            /**
             * Здесь it предыдущий элемент на базе которого рассчитывается следующий.
             * Здесь могут быть длительные операции, которые потом будут ожидаться при считывании последовательности.
             * */
            println("пауза 1000")
            Thread.sleep(1000)

            /**
             * Если вернуть null последовательность закончится, если последовательность не ограничить она будет бесконечной.
             * Последний элемент будет 5.
             * */
            if (it < 5) it + 1 else null
        }

        val list = listOf(33,44,55)

        val s = sequence<Int> {
            /**
             * Добавление одиночного элемента
             * */
            yield(111)

            println("пауза 2000")
            Thread.sleep(2000)

            /**
             * Можно добавить объект класса реализующий интерфейс Iterable<E> или Iterator<E> напрямую.
             * Также можно добавить другую последовательность.
             * */
            yieldAll(list)
            yieldAll(list.iterator())
            yieldAll(seq1)
        }

        /**
         * В Sequence определен оператор iterator() поэтому ее можно использовать в цикле for.
         * Если последовательно бесконечна, то и цикл for будет бесконечным.
         * */
        for (i in s){
            println(i)
        }

    }
}
package ru.ragefalcon.cheatsheet.kotlincheat

import org.junit.Test

class MultiDeclarationCheat {
    @Test
    fun test01() {

        class Bbb()

        /**
         * Мультидекларации работают на основе соглашений.
         * Для инициализации переменных вызываются функции componentN.
         * Дата классы сами генерируют эти фукнции.
         * */
        data class multiDec(val el1: Int, val str2: String, val el3: Bbb)

        /**
         * Если их нужно реализовать самостоятельно то перегружаются операторы componentN.
         * */
        class multiDecCust(val el1: Int, val str2: String, val el3: Bbb) {
            operator fun component1() = el1
            operator fun component2() = str2
            operator fun component3() = el3
        }

        val listMutli = listOf<multiDec>(multiDec(1, "2", Bbb()))

        /**
         * Мультидекларацию можно и удобно использовать в циклах.
         * */
        println("listMulti")
        for ((el_i, str_i, ell3_i) in listMutli) {
            println("el_i = $el_i")
            println("str_i = $str_i")
            println("ell3_i = $ell3_i")
        }

        val listMutliCust = listOf<multiDecCust>(multiDecCust(3, "4", Bbb()))

        println("listMultiCust")
        for ((el, str, ell3) in listMutliCust) {
            println("el = $el")
            println("str = $str")
            println("ell3 = $ell3")
        }

        /**
         * Можно указывать меньше переменных чем определено функций componentN,
         * переменные будут определяться в порядке component функций.
         * Елси попробовать инициализировать большее количество переменных, например:
         * val (el, str, ell3, str2) = listMutliCust[0]
         * то компилятор покажет ошибку.
         * */
        val (el, str) = listMutliCust[0]
        println("el = $el")
        println("str = $str")

        val listInt = listOf(1,2,3,4,5,6,7)

        /**
         * Коллекции позволяют вернуть таким образом до 5 элементов. Больше component функций по умолчанию не определено.
         * Также нужно учесть, что компилятор не может проверить сколько элементов на данный момент в массиве, и если их
         * будет меньше будет выброшено java.lang.ArrayIndexOutOfBoundsException.
         * */
        val (e1,e2,e3,e4,e5) = listInt
        println("e1 = ${e1}")
        println("e2 = ${e2}")
        println("e3 = ${e3}")
        println("e4 = ${e4}")
        println("e5 = ${e5}")

    }
}
package ru.ragefalcon.cheatsheet.kotlincheat

import org.junit.Test

class FunctionHighOrder {

    val simpleLambda = { x: Int, y: Int ->
        x + y
    }
    val simpleLambda2: (Int, Int) -> Int = simp2@{ x: Int, y: Int ->
        (x + y).let {
            var x = it
            for (i in 1..10) {
                x += i
                if ( x > 15) return@simp2 x
            }
            return@let 2
        }
    }
    var simpleLambda3: ((Int, Int) -> Int)? = { x, y ->
        x + y
    }
    var simpleLambda4: ((
        Int, Int, Int, Int, Int, Int, Int, Int, Int, Int,
        Int, Int, Int, Int, Int, Int, Int, Int, Int, Int,
        Int, Int, Int, Int, Int, Int, Int, Int, Int, Int,
        Int, Int, Int, Int, Int, Int, Int, Int, Int, Int,
        Int, Int, Int, Int, Int, Int, Int, Int, Int, Int,
        Int, Int, Int, Int, Int, Int, Int, Int, Int, Int,
        Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int
    ) -> Int)? = null

    /**
     * Именование аргументов в типе функции как (x: Int, y: Int) -> Int,
     * необязательно и при передаче лямбды их можно именовать по другому,
     * но это может улучшить читабельность кода, а так же в некоторых
     * случаях IDE можно использовать эту информацию для автодополнения и подсказок.
     * */
    fun first(l1: (Int, Int) -> Int, l2: (x: Int, y: Int) -> Int): Int {
        return l2(l1(1, 2), 3)
    }

    @Test
    fun test01() {
        simpleLambda3 = null

        var aa = first(l1 = { x, y -> x + y }) { x, y -> x + y }
        aa = first(l1 = { x, y -> x + y }) { c, d -> c + d }
        aa = first(l1 = { x, y -> x + y }, l2 = { c, d -> c + d })
        aa = first({ _, y -> y }, { _, _ -> 0 })

        val list = listOf(1, 2, 3, 4, 5, 6, 7)

        list.forEach forE@{
            if (it == 5) return@forE
            println(it)
        }

        list.forEach{
            if (it == 2) return@forEach
            println(it)
        }

        list.forEach( fun (num) {
            /**
             *  return здесь вернет из анонимной фукнции, а не из внешней test01.
             *  Здесь как и в функциях выше прерывается не весь цикл, а только конкретная итерация
             *
             *  "return производит выход из ближайшей функции, объявленной с
             *  помощью ключевого слова fun"
             * */
            if (num == 3) return
            println("anonim $num")
        })

        val sdf = fun (aa: Int): Boolean {
            return aa < 3
        }

        list.filter(fun (bb): Boolean {
            return bb < 3
        })

        list.filter filt@{
            if (it > 3) return@filt false
            true
        }

    }
}
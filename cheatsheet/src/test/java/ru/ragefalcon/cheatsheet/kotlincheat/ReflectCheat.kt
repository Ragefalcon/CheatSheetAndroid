package ru.ragefalcon.cheatsheet.kotlincheat

import org.junit.Test

var globalTestProperty = 0
fun foo(x : Int) = println(x)

class ReflectCheat {

    fun nestedFoo(x: Int) = x*2
    var nestedProp = 3

    @Test
    fun test01(){
        val kFunction = ::foo
        println("kFunction.call() = ${kFunction.call(43)}")
        try {
            println("kFunction.call(23,43) = ${kFunction.call(23, 43)}")
        }   catch (e: Exception) {
            println("kFunction.call(23,43) Exception= $e")
//            >>> kFunction.call(23,43) Exception= java.lang.IllegalArgumentException: Callable expects 1 arguments, but 2 were provided.
        }
        println("kFunction.invoke(23) = ${kFunction.invoke(34)}")
        println("kFunction(23) = ${kFunction(23)}")

        val kFunNested = ::nestedFoo
        println("kFunNested.call() = ${kFunNested.call(43)}")
        try {
            println("kFunNested.call(23,43) = ${kFunNested.call(23, 43)}")
        }   catch (e: Exception) {
            println("kFunNested.call(23,43) Exception= $e")
//            >>> kFunction.call(23,43) Exception= java.lang.IllegalArgumentException: Callable expects 1 arguments, but 2 were provided.
        }
        println("kFunNested.invoke(23) = ${kFunNested.invoke(34)}")
        println("kFunNested(23) = ${kFunNested(23)}")

        val kProperty = ::globalTestProperty
        println("kProperty.call() = ${kProperty.call()}")
        kProperty.setter.call(2)
        println("kProperty.call() = ${kProperty.call()}")

        /**
         * Здесь получается ссылка на конкретный
         * экземпляр и ее можно рассматривать как глобальную.
         * */
        val kPropNested = ::nestedProp
        println("kPropNested.call() = ${kPropNested.call()}")
        kPropNested.setter.call(2)
        println("kPropNested.call() = ${kPropNested.call()}")

        /**
         * А здесь ссылка как на функцию класса, поэтому для
         * обращения через рефлексию необходимо передавать
         * экземпляр класса родителя.
         * */
        val kPropNested2 = ReflectCheat::nestedProp
        println("kPropNested2.call(this) = ${kPropNested2.call(this)}")
        println("kPropNested2.setter.call(this,4)")
        kPropNested2.setter.call(this,4)
        println("kPropNested2.call(this) = ${kPropNested2.call(this)}")
        println("kPropNested2.get(this) = ${kPropNested2.get(this)}")

    }
}
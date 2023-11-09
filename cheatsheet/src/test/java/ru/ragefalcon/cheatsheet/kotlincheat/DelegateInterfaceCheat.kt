package ru.ragefalcon.cheatsheet.kotlincheat

import org.junit.Test

class DelegateInterfaceCheat {

    /**
     * Реализация композиции с помощью делегатов
     * */
    class TAdelegateComposition( private val ta: TAdelegate1): TestAction by ta, MutableList<String> by mutableListOf()

    class MapMyDelegete1(): MutableMap<Int, String> by mutableMapOf() {
        fun testValue() {
            put(1,"1")
            put(2,"2")
        }
    }

    class MapMyDelegete2(private val prMap: MutableMap<Int, String>): MutableMap<Int, String> by prMap {
        fun testValue() {
            put(3,"3")
            put(4,"4")
        }
    }

    @Test
    fun test01(){
        val map1 = MapMyDelegete1()
        map1.testValue()

        println("map1 = ${map1.entries}")
        val map2 = MapMyDelegete2(map1)
        map2.testValue()

        println("map2 = ${map2.entries}")
        println("map1 = ${map1.entries}")
        /**
         * map1 = [1=1, 2=2]
         * map2 = [1=1, 2=2, 3=3, 4=4]
         * map1 = [1=1, 2=2, 3=3, 4=4]
         * */
    }

    interface TestAction {
        fun action(): Unit
    }

    class TAdelegate1(): TestAction by object : TestAction {
        override fun action() {
            println("Action fun check")
        }
    }

    fun getTestAction(): TestAction = object : TestAction {
        override fun action() {
            println("Action fun check")
        }
    }

    val testObj = getTestAction()

    /**
     * Без inner нужно чтобы getTestAction и testObj были глобальными или статическими
     * */
    inner class TAdelegate2( ): TestAction by getTestAction()
    inner class TAdelegate3( ): TestAction by testObj

}
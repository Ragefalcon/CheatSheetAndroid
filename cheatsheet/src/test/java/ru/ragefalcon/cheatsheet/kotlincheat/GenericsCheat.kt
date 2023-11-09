package ru.ragefalcon.cheatsheet.kotlincheat

import org.junit.Test

class GenericsCheat {

    /**
     * "В качестве типового аргумента для класса может даже использоваться
     * сам класс."
     * Т.е. тут при указании класса для интерфейса используется сам класс который и будет его реализовывать.
     * */
    class SomeClass : Comparable<SomeClass> {

        /**
         * Возвращает 0 когда объекты равны,
         * когда текущий объект больше чем other - возрващает положительное число,
         * когда меньше - отрицательное.
         *
         * this > other  <->  this.compareTo(other) > 0
         * */
        override fun compareTo(other: SomeClass): Int {
            TODO("Not yet implemented")
        }
    }

    /**
     * "В тех редких случаях, когда требуется определить несколько ограничений
     * для типового параметра, можно использовать несколько иной синтаксис."
     * */
    fun <Т> ensureTrailingPeriod(seq: Т)
            where Т : CharSequence, Т : Appendable {
        if (!seq.endsWith('.')) {
            seq.append('.')
        }
    }

    interface SomeInterface<out T> where T : CharSequence, T : Appendable {
    }

    class SomeClass2<T>() : SomeInterface<T> where T : CharSequence, T : Appendable {
        var tmp: T? = null
    }


    /**
     * "...  типовой параметр без верхней
     * границы на деле имеет верхнюю границу Any?"
     * !!! Т.е. тип переданный в T может быть нуллабельным.
     * Т.е. возможен вариант как SomeClass3<String>, так и SomeClass3<String?>
     * */
    class SomeClass3<T> {
        fun process(value: T) {

            /**
             * Здесь вызывается функция расширения, которая так же может быть вызвана от null значения
             * */
            value.hashCode()
            null.hashCode()

            /**
             * А здесь вызывается функция класса Any, которая НЕ может быть вызвана от null значения
             * */
            value?.hashCode()
        }
    }

    /**
     * "Чтобы гарантировать замену типового параметра только типами, не
     * поддерживающими значения n u ll, необходимо объявить ограничение.
     * Если это единственное требование к типу, то используйте в качестве верх­
     * ней границы тип Any"
     *
     * "Обратите внимание, что такое ограничение можно наложить указанием
     * в верхней границе любого типа, не поддерживающего nu11, не только типа
     * Any."
     * */
    class SomeClass4<T : Any> {
        fun process(value: T) {
            value.hashCode()
        }

        /**
         * Теперь можно управлять нуллабельностью для отдельных методов и свойств
         * используя тип Т со знаком "?".
         * */
        fun processNullable(value: T?) {
            value?.hashCode()
        }
    }

    class SomeClass5<T, K> {

        fun process(value: List<T>, value2: List<K>, value3: T) {
            /**
             * Т.к. обобщенные типы при выполнении стираются, то строка ниже не скомпилируется
             * */
//            if (value is List<String>) 1 else 2

            /**
             * Эта строка скомпилируется, но компилятор будет подсвечивать неконтролируемое приведение.
             * Причем по тестам похоже это подразумевает что listString будет присвоен в любом случае,
             * поскольку value при выполнении является List без указания типа, т.к. типы стираются.
             * Дальнейший println("listString = ${listString}") так же отработает, т.к. toString есть для всех вариантов.
             * А вот дальше при попытке обратиться к элементу и даже просто вызвать forEach,
             * если там окажется не тот тип который ожидался, то вывалится ошибка.
             *
             * "Во время выполнения возможно определить, что значение имеет тип
             * L ist, но нельзя определить, что список хранит строки, объекты класса
             * Person или что-то ещё, - эта информация стерта."
             * */
            val listString: List<Boolean>? = value as? List<Boolean>
            println("listString = ${listString}")
//            listString?.forEach {
//                println(if (it) "true" else "false")
//            }

            val listString2 = value2 as? List<String>
            println("listString2 = ${listString2}")

            /**
             * Данное приведение и проверки будут работать нормально,
             * т.к. тип value3 в отличие от List будет иметь конкретный тип,
             * а не тип с типовым аргументом.
             * */
            if (value3 is Boolean) 1 else 2
            val prop: Boolean? = value3 as? Boolean
            println("prop = ${prop}")

            /**
             * Чтобы убедиться, что значения является List и не важно,
             * что он в себе хранит, можно сделать так.
             * */
            if (value3 is List<*>) {
                println("value3 is List")
            }
            (value3 as? List<*>).let {
                /**
                 * it - List<*>?
                 * вызов get(index) будет возвращать "Any?"
                 * */
            }

            /**
             * Такая проверка будет работать корректно, т.к. тип аргумента задан явно
             * и виден на этапе компиляции.
             * */
            fun printSum(c: Collection<Int>) {
                if (c is List<Int>) {
                    println(c.sum())
                }
            }

            /**
             * Нельзя использовать неовеществляемый типовой параметр в качестве
             * типового аргумента при вызове функции с овеществляемым типовым
             * параметром
             *
             * >>> processReif<T>(value)
             * -> Cannot use 'T' as reified type parameter. Use a class instead.
             * */

        }

        /**
         * В теле обобщенной функции нельзя ссылаться на типовой аргумент,
         * с которым она была вызвана:
         *
         * »> fun <Т> isA(value: Any) = value is T
         * Error: Cannot check for instance of erased type: T
         *
         * Это общее правило. Но есть одно исключение, позволяющее преодолеть
         * данное ограничение: встраиваемые (inline) функции.
         * */
        inline fun <reified V> processReif(value: List<*>) {
            /**
             * Без reified ни одно из этих использований типа V не будет возможно.
             * При этом если функция использует reified, то ее не получится вызвать из Java.
             * */
            value.all { it is V }
            println("value is List<${V::class.java}>= ${value.all { it is V }}")

            /**
             * При этом проверка типов с типовыми аргументами все равно не пройдет
             *
             * >>> value is List<V>
             * -> Cannot check for instance of erased type: List<V>
             * */

        }
    }

    @Test
    fun test01() {
        val sc = SomeClass5<SomeClass, String>()
        sc.process(listOf(SomeClass()), listOf("1", "2"), SomeClass())

        sc.processReif<Boolean>(listOf("1","2"))
        sc.processReif<Boolean>(listOf(true,false))
    }


}
package ru.ragefalcon.cheatsheet.kotlincheat

import org.junit.Test
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

class DelegatesPropertyCheat {


    /**
     * Общая схема для создания объекта делегата реализующая как методы доступа(getValue и setValue),
     * так и логику инициализации (provideDelegate).
     * Если логика инициализации не нужна, то достаточно только реализации ReadWriteObj.
     *
     * Если делегирование происходит константе достаточно метода getValue.
     *
     * ReadWriteObj не обязан быть внутренним, а может быть самостоятельным и написан где угодно.
     * */
    class MostCommonDelegate(private var value: String = "") {

        inner class ReadWriteObj {
            operator fun getValue(thisRef: Any?, prop: KProperty<*>): String {
                println("Read property ${prop.name}. Value = $value")
                return value
            }

            operator fun setValue(thisRef: Any?, prop: KProperty<*>, newValue: String) {
                value = if (newValue != "something") newValue else "other"
                println("Set property ${prop.name}. NewValue = $value")
            }
        }

        operator fun provideDelegate(thisRef: Any?, prop: KProperty<*>): ReadWriteObj {
            println("Property ${prop.name} is created.")
            return ReadWriteObj()
        }
    }

    @Test
    fun test00() {
        /**
         * У объекта справа от by определен оператор provideDelegate и он будет вызван при создании свойства.
         * Помимо добавление логики инициализации и каких либо проверок, здесь удобно получить имя свойства
         * при его создании для автоматической генерации дополнительных ключей по этому полю или
         * создание записей в базе данных с использованием имени свойства.
         * */
        var tmpProvide by MostCommonDelegate("Start tmp1")
//      >>> Property tmpProvide is created.

        /**
         * Здесь справа от by передается дочерний объект у которого определены только getValue и setValue,
         * поэтому provideDelegate родительского при этом вызван не будет.
         * */
        var tmp2 by MostCommonDelegate("Start tmp2").ReadWriteObj()

        println("--------------------")

        println("tmpProvide = ${tmpProvide}")
//      >>> Read property tmpProvide. Value = Start tmp1
//      >>> tmpProvide = Start tmp1
        println("tmp2 = ${tmp2}")
//      >>> Read property tmp2. Value = Start tmp2
//      >>> tmp2 = Start tmp2

        tmpProvide = "something"
//      >>> Set property tmpProvide. NewValue = other
        tmp2 = "tmp3"
//      >>> Set property tmp2. NewValue = tmp3

        println("tmpProvide = ${tmpProvide}")
        println("tmp2 = ${tmp2}")
    }

    /**
     * "Выражение справа от by не обязательно должно создавать новый экземпляр.
     * Это может быть вызов функции, другое свойство или любое выражение,
     * при условии, что значением этого выражения является объект
     * с методами getValue и setValue, принимающими параметры требуемых
     * типов. Как и другие соглашения, getValue и setValue могут быть методами,
     * объявленными в самом объекте, или функциями-расширениями."
     *
     * "Kotlin in Action", Авторы: Светлана Исакова, Дмитрий Жемеров
     * **/

    /**
     * Функции расширения могут быть объявлены как глобально, так и внутри класса где используется.
     *
     * operator fun <K, V> MutableMap<K, V>.getValue(aas: TAdelegate, prop: KProperty<*>):MutableMap<K, V> = this
     *
     *
     *
     * Если объявить функцию расширения внутри внешнего класса,
     * то она не будет видна внутри вложенных классов,
     * но будет видна внутри внутренних (inner).
     * */

    class TAdelegate1() {
        /**
         * Первый параметр в getValue и setValue по сути указывает в каких классах эти функции могут использоваться
         * для делегирования свойств. Можно указать там Any, чтобы использовать в любом классе, а можно указать
         * конкретный класс, ограничив тем самым область применения.
         * */
        private operator fun MutableMap<Int, Int>.getValue(thisRef: Any, prop: KProperty<*>): MutableMap<Int, Int> {
            prop.annotations
            return this
        }

        val myMap by mutableMapOf<Int, Int>()
    }


    class TAdelegate2() {
        var i = 0

        /**
         * getValue объялен здесь, а setValue глобально ниже.
         * */
        private operator fun <K, V> MutableMap<K, V>.getValue(
            owner: TAdelegate2,
            prop: KProperty<*>
        ): MutableMap<K, V> = this

        private operator fun <K, V> MutableMap<K, V>.setValue(
            owner: DelegatesPropertyCheat.TAdelegate2,
            prop: KProperty<*>,
            newValue: MutableMap<K, V>
        ) {
            owner.i++
            this.clear()
            this.putAll(newValue)
        }

        var myMap by mutableMapOf<Int, Int>()
        var globalDelegate2 by mutableListOf<Int>()

        /**
         * Чтобы делегировать свойство другому свойству, используйте квалификатор :: в имени делегата
         * */
        val newName by this::i
        val newName2 by ::i
        val newName3 by ::globalDelegate
    }

    /**
     * Конструкция ниже возможна, потому что для Map, в котором
     * в качестве ключей указан String, определены методы getValue
     * и setValue (через функции расширения).
     * Эти getValue и setValue при запросе к словарю(map) в качестве ключей используют
     * имя свойства из метаданных - prop: KProperty<*>.
     * */
    class MutableUser(val map: MutableMap<String, Any?>) {
        var name: String by map
        var age: Int by map

        val intInMap: Int by mutableMapOf<String, Int>()

        fun stdlibExt() {
            /**
             * Если раскомментировать строку ниже компилятор покажет ошибку,
             * а так же то что есть две реализации для этой функции, одна из которых
             * Как раз предназначена для делегирования.
             *
             * @kotlin.jvm.JvmName("getVar")
             * @kotlin.internal.InlineOnly
             * public inline operator fun <V, V1 : V> MutableMap<in String, out @Exact V>.getValue(thisRef: Any?, property: KProperty<*>): V1 =
             *     @Suppress("UNCHECKED_CAST") (getOrImplicitDefault(property.name) as V1)
             *
             * Зажав Ctrl и кликнув по getValue
             * можно "провалиться" и посмотреть эту реализацию.
             * */
//            map.getValue()
        }
    }

}


/**
 * Для объявления свойства с делегированием на глобальном уровне,
 * нужно чтобы первый параметр в getValue и setValue мог быть равен null.
 * Поэтому можно указать Any?, тогда делегирование можно будет использовать везде.
 * Если указать конкретный класс и нулабельность, например "DelegatesCheat?",
 * то делегирование можно будет использовать либо глобально, либо внутри указанного класса,
 * но не внутри другого.
 * */
private operator fun <V> MutableList<V>.getValue(owner: Any?, prop: KProperty<*>): MutableList<V> = this
private operator fun <V> MutableList<V>.setValue(owner: Any?, prop: KProperty<*>, newValue: MutableList<V>) {
    this.clear()
    this.addAll(newValue)
}

var globalDelegate by mutableListOf<Int>()

/**
 * class C {
 *     var prop: Type by MyDelegate()
 * }
 *
 * // этот код генерируется компилятором:
 * class C {
 *     private val prop$delegate = MyDelegate()
 *     var prop: Type
 *         get() = prop$delegate.getValue(this, this::prop)
 *         set(value: Type) = prop$delegate.setValue(this, this::prop, value)
 * }
 * */


/**
 * В стандартной библиотеки есть уже несколько полезных делегатов.
 *
 * lazy позволяет указать "конструктор" для свойства, который будет вызван
 * при первом обращении к свойству. Вызывается только при первом обращении
 * после возвращает полученное значение.
 * По умолчанию потокобезопасный, можно это изменить с помощью параметра.
 *
 * Delegates.vetoable - задает стартовое значение и условие для изменения,
 * если условие возвращает false, то новое значение не применяется.
 * Delegates.observable - вызывает дополнительную логику после изменения.
 *
 * Оба класса реализуют ObservableProperty<V>.
 * */

val laz: Int by lazy(LazyThreadSafetyMode.NONE) {
    1 + 2
}

var vetDel = Delegates.vetoable(0) { metaProp, oldValue, newValue ->
    newValue > oldValue && metaProp.name != "something_const"
}
var obsDel = Delegates.observable(0) { metaProp, oldValue, newValue ->
    println("${metaProp.name} value: $oldValue - > $newValue ")
}

var bb by vetDel
var something_const by vetDel
/** Не будет изменяться */

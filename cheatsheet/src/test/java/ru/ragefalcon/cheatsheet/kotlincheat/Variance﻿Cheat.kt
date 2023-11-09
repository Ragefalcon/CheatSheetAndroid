package ru.ragefalcon.cheatsheet.kotlincheat

import org.junit.Test
import kotlin.reflect.KClass

/**
 * Здесь будет особенно много примеров и цитат из книги:
 * "Kotlin in Action", Авторы: Светлана Исакова, Дмитрий Жемеров
 *
 * Там где текст взят в кавычки это из этой книги.
 * */
class VarianceCheat {
    /**
     * Тип и класс разные понятия, любой класс может образовывать
     * как минимум два типа, нуллабельный и ненуллабельный.
     * Например: String и String? - это два типа использующие один класс.
     *
     * "Каждый обобщенный класс потенциально способен породить бесконечное
     * количество типов. ... List<Int>, List<String?>, List<List<String>> и так далее"
     * */

    /**
     * "Тип В - это подтип типа А, если значение типа В можно
     * использовать везде, где ожидается значение типа А."
     * */
    open class Animal() {}

    /**
     * Класс Cat является подтипом класса Animal,
     * а класс Animal является супертипом для Cat.
     * */
    class Cat() : Animal() {
    }

    /**
     * Обобщенные классы определяются базовым классом
     * и параметрами типа. Для них понятия подтипа и супертипа
     * определить сложнее.
     *
     * Отношение с родительскими классами соответствует той же
     * логике, так тип "Any?" является супертипом для всех типов.
     * А вот отношение типов с одним базовым классом,
     * но разными параметрами типа определяются отдельно.
     * */

    /**
     * Типовые параметры могут использоваться
     * во входящих и исходящих позициях.
     *
     * К исходящим позициям относится:
     *  - Использование Т для указания типов возвращаемых значений
     *    функциями someFun1 и someFun2, можно говорить,
     *    что эти функции производят значения типа Т и List<T>.
     *  - Использования Т для указания типов свойств
     *    val и/или var (initval1, initval3),
     *    т.к. для них будет генерироваться getters производящие значения типа Т.
     *
     * К входящей позиции относится:
     *  - Использование T для указания типа параметра функции funParam.
     *    Можно сказать, что такая функция потребляет значение типа Т.
     *  - Так же к входящей позиции будет относиться использование T
     *    для указания типа переменной initval3, т.к. для нее будет
     *    сгенерирован setter, т.е. функция потребляющая значение типа Т.
     *
     * Т.е. для свойств var Т будет одновременно и во входящей
     * и в исходящей позиции.
     *
     * Параметры (initval4, vararg initvals) определенные в конструкторе
     * и private поля (initval2) не относятся ни к входящим,
     * ни к исходящим позициям.
     *
     * "Отметьте, что правила позиции охватывают только видимое извне
     * (public, protected и internal) API класса. Параметры приватных
     * методов не находятся ни в исходящей, ни во входящей позициях. Правила
     * вариантности защищают класс от неправильного использования внешними
     * клиентами и не применяются к внутренней реализации класса"
     * */
    class SomeClass<T>(
        val initval1: T,
        private var initval2: T,
        var initval3: T,
        initval4: T,
        vararg initvals: T
    ) {
        private val list = initvals
        fun someFun1(funParam: T): T = initval1
        fun someFun2(): List<T> = list.toList()
    }

    /**
     * ИНВАРИАНТНОСТЬ
     * По умолчанию обобщенные классы инвариантны.
     * Т.е. для базового класса с указанными параметрами типа,
     * не существует подтипов и супертипов с тем же базовым классом.
     *
     * Тип Т может использоваться в любой позиции.
     *
     * Herd0<Animal> не является ни супертипом, ни подтипом для Herd0<Cat>,
     * и Herd0<Cat> не является ни супертипом, ни подтипом для Herd0<Animal>.
     * Значит ни один из них нельзя использовать там где ожидается другой.
     * */
    class Herd0<T : Animal>(private val list: List<T>) {
        val size: Int get() = list.size
        operator fun get(i: Int): T? {
            return list.getOrNull(i)
        }
    }


    /**
     * КОВАРИАНТНОСТЬ
     * Ковариантность класса по параметру типа Т
     * можно указать с помощью ключевого слова "out".
     *
     * "out" накладывает ограничение на использование Т.
     * С ним Т может использоваться только в исходящих позициях.
     * (или лучше сказать, что не может использоваться во входящих,
     * потому что он все еще может использоваться
     * в параметрах конструктора и т.д.)
     *
     * Теперь отношения классов Herd1<T> будут такие же, как у
     * экземпляров самого Т и его наследников и родителей.
     * Т.е. т.к. Cat подтип Animal - Herd1<Cat> будет подтипом Herd1<Animal>,
     * а т.к. Animal супертип для Cat - Herd1<Animal> супертип для Herd1<Cat>.
     * И теперь везде где ожидается Herd1<Animal>,
     * можно также использовать Herd1<Cat>.
     * Т.к. Herd1 только производит значения типа Т, значит,
     * что везде где от Herd1 ожидалось получение Animal,
     * будет получен Cat, который тоже подходит.
     * */
    class Herd1<out T : Animal>(private val list: List<T>) {
        val size: Int get() = list.size
        operator fun get(i: Int): T? {
            return list.getOrNull(i)
        }
    }

    /**
     * КОНТРАВАРИАНТНОСТЬ
     * Контравариантность класса по параметру типа Т
     * можно указать с помощью ключевого слова "in".
     *
     * "in" накладывает ограничение на использование Т.
     * С ним Т может использоваться только во входящих позициях.
     * (или лучше сказать, что не может использоваться в исходящих,
     * потому что он все еще может использоваться
     * в параметрах конструктора и т.д.)
     *
     * Отношения классов Comparator<T> будут обратные отношениям
     * экземпляров самого Т и его наследников и родителей.
     * Т.е. т.к. Int подтип Number - Comparator<Int> будет супертип для Comparator<Number>,
     * а т.к. Number супертип для Int - Comparator<Number> подтипом Comparator<Int>.
     * И теперь везде где ожидается Comparator<Int>,
     * можно также использовать Comparator<Number>.
     *
     * У меня долго не могло это полностью уложиться в голове.
     * Это контринтуитивно  :)
     * Потому что интуитивно воспринимается, что: то, что относится
     * к классу родителю не должно приниматься там где ожидается то,
     * что относится к наследнику.
     *
     * Чтобы полностью понять это, нужно постоянно держать в голове,
     * что Comparator только потребляет значения типа Т,
     * и там где ожидается этот интерфейс значения будут отправлять
     * именно в него, а не наоборот. И там где ожидается Comparator<Int>,
     * в полученный экземпляр будут отправлять значения типа Int.
     * А это значит, что эти значения (Int) вполне подходят для потребления,
     * там где в Comparator<Number> ожидается Number. Именно поэтому
     * Comparator<Number> будет подтипом для Comparator<Int>.
     * */
    interface Comparator<in Т> {
        fun compare(el: Т, е2: Т): Int
    }


    /**
     * Класс или интерфейс может быть ковариантным по одному параметру
     * типа и контравариантным по другому. Классический пример - интерфейс
     * Function.
     * Function1<Animal, Int> -> (Animal) -> Int
     * будет подтипом
     * Function1<Cat, Number> -> (Cat) -> Number
     */
    interface Function1<in P, out R> {
        operator fun invoke(p: P): R
    }

    @Test
    fun test01() {
        var funPodtype: Function1<Animal, Int> = object : Function1<Animal, Int> {
            override fun invoke(p: Animal): Int {
                return p.hashCode()
            }
        }

        val funSupertype: Function1<Cat, Number> = funPodtype

//        Если попытаться сделать так, то компилятор выведет ошибку Type mismatch.
//        funPodtype = funSupertype
    }


    /**
     * "Kotlin позволяет объявить вариантность и для обобщенного класса в
     * целом (вариантность в месте объявления, declaration-site variance), и
     * в месте конкретного использования обобщенного типа (use-site variance)."
     * */

    /**
     * Функция копирования данных с инвариантными типовыми
     * параметрами.
     * */
    fun <T> copyData0(
        source: MutableList<T>,
        destination: MutableList<T>
    ) {
        for (item in source) {
            destination.add(item)
        }
    }

    /**
     * Функция копирования данных с двумя типовыми параметрами.
     * */
    fun <T : R, R> copyData1(
        source: MutableList<T>,
        destination: MutableList<R>
    ) {
        for (item in source) {
            destination.add(item)
        }
    }

    /**
     * Функция копирования данных с выходной проекцией типового
     * параметра.
     * */
    fun <T> copyData2(
        source: MutableList<out T>,
        destination: MutableList<T>
    ) {
        for (item in source) {
            /**
             * Кстати, если указать source: MutableList<in T>,
             * то компилятор это учтет и покажет ошибку на .add(item).
             * */
            destination.add(item)
        }
    }

    /**
     * Функция копирования данных с входящей проекцией типового
     * параметра.
     * */
    fun <T> copyData3(
        source: MutableList<T>,
        destination: MutableList<in T>
    ) {
        for (item in source) {
            destination.add(item)
        }
    }

    /**
     * Проекцию со звездочкой, можно использовать,
     * чтобы сообщить об отсутствии информации об обобщенном аргументе.
     * У объекта с такой проекцией можно будет использовать методы
     * без типовых параметров, а так же методы которые используют
     * параметр типа в исходящих позициях, т.к. как out T.
     *
     * "Синтаксис вариантности со звездочкой можно использовать, когда
     * типовой аргумент неизвестен или неважен."
     * */
    class SomeClassCat<T, R : Cat>(var cat: R, val tmp: T) {
        fun getCatHashInInt(): Int = cat.hashCode()
        fun newCatValue(newCat: R) {}
        fun getTmpValue(): T = tmp
    }

    fun someFun(sc: SomeClassCat<*, *>) {
        /**
         * Так нельзя, потому что sc.setCat<R: Cat> по сути переходит в sc.setCat<Nothing>
         * При проекции * входящие позиции переходят в Nothing.
         * */
//        sc.setCat(Cat())

        /** sc.cat<out Cat>, т.к. указана верхняя граница в виде Cat **/
        val animal: Animal = sc.cat
        val cat: Cat = sc.cat

        val hash = sc.getCatHashInInt()

        /** sc.getTmp<Any?>, если не указана верхняя граница то проекция * переходит в Any? **/
        val tmp: Any? = sc.getTmpValue()
    }

    /**
     * Ниже хороший пример из книги как это можно использовать.
     * В нем есть неконтролируемое приведение (предупреждение
     * о нем устраняется соответствующей аннотацией),
     * но с учетом того, что весь механизм инкапсулирован в отдельный
     * класс, то его использование будет безопасным.
     * */

    interface FieldValidator<in T> {
        fun validate(input: T): Boolean
    }

    object DefaultStringValidator : FieldValidator<String> {
        override fun validate(input: String) = input.isNotEmpty()
    }

    object DefaultlntValidator : FieldValidator<Int> {
        override fun validate(input: Int) = input >= 0
    }

    object Validators {
        private val validators =
            mutableMapOf<KClass<*>, FieldValidator<*>>()

        fun <T : Any> registerValidator(
            kClass: KClass<T>, fieldValidator: FieldValidator<T>
        ) {
            validators[kClass] = fieldValidator
        }

        @Suppress("UNCHECKEDCAST")
        operator fun <T : Any> get(kClass: KClass<T>): FieldValidator<T> =
            validators[kClass] as? FieldValidator<T>
                ?: throw IllegalArgumentException(
                    "No validator for ${kClass.simpleName}"
                )

        /** Мое дополнение */
        operator fun <T : Any> set(kClass: KClass<T>, fieldValidator: FieldValidator<T>) {
            validators[kClass] = fieldValidator
        }

        inline fun <reified T : Any> registerValidator(
            fieldValidator: FieldValidator<T>
        ) {
            registerValidator(T::class, fieldValidator)
        }

        inline fun <reified T : Any> get(): FieldValidator<T> = get(T::class)
    }

    fun test02(){
        Validators.registerValidator(DefaultStringValidator)
        Validators.registerValidator(Int::class, DefaultlntValidator)
        Validators.get<Int>()
        Validators[String::class] = DefaultStringValidator
        Validators[String::class]

        /**
         * На эти строчки компилятор выдаст ошибку Type mismatch.
         * */
//        Validators[String::class] = DefaultlntValidator
//        Validators.registerValidator(Int::class, DefaultStringValidator)
    }
}
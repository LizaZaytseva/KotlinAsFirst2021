@file:Suppress("UNUSED_PARAMETER")

package lesson12.task1

import kotlinx.html.A
import ru.spbstu.kotlin.typeclass.kind

/**
 * Класс "хеш-таблица с открытой адресацией"
 *
 * Общая сложность задания -- сложная, общая ценность в баллах -- 20.
 * Объект класса хранит данные типа T в виде хеш-таблицы.
 * Хеш-таблица не может содержать равные по equals элементы.
 * Подробности по организации см. статью википедии "Хеш-таблица", раздел "Открытая адресация".
 * Методы: добавление элемента, проверка вхождения элемента, сравнение двух таблиц на равенство.
 * В этом задании не разрешается использовать библиотечные классы HashSet, HashMap и им подобные,
 * а также любые функции, создающие множества (mutableSetOf и пр.).
 *
 * В конструктор хеш-таблицы передаётся её вместимость (максимальное количество элементов)
 */
class OpenHashSet<T>(val capacity: Int) {

    private var added = 0

    /**
     * Массив для хранения элементов хеш-таблицы
     */
    internal val elements = Array<Any?>(capacity) { null }

    /**
     * Число элементов в хеш-таблице
     */
    val size: Int get() = added

    /**
     * Признак пустоты
     */
    fun isEmpty(): Boolean = (size == 0)

    /**
     * Добавление элемента.
     * Вернуть true, если элемент был успешно добавлен,
     * или false, если такой элемент уже был в таблице, или превышена вместимость таблицы.
     */
    fun add(element: T): Boolean {
        if (size < capacity && !contains(element)) {
            elements[size] = element
            added += 1
            return true
        }
        return false
    }

    /**
     * Проверка, входит ли заданный элемент в хеш-таблицу
     */
    operator fun contains(element: T): Boolean = elements.contains(element)

    /**
     * Таблицы равны, если в них одинаковое количество элементов,
     * и любой элемент из второй таблицы входит также и в первую
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as OpenHashSet<Any?>
        if (other.size != size) {
            return false
        }
        for (element in elements) {
            if (element != null && !other.contains(element)) {
                return false
            }
        }
        return true
    }

    override fun hashCode(): Int {
        var result = 13
        for (element in elements) {
            if (element != null) {
                result = (result * 31) + element.hashCode()
            }
        }
        return result
    }
}
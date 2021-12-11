@file:Suppress("UNUSED_PARAMETER")

package lesson12.task1

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
    internal val elements: Array<T?> = Array<Any?>(capacity) { null } as Array<T?>
    // "Cannot use 'T' as reified type parameter. Use a class instead." ???

    /**
     * Число элементов в хеш-таблице
     */
    val size: Int get() = added

    /**
     * Признак пустоты
     */
    fun isEmpty(): Boolean = size == 0

    /**
     * Добавление элемента.
     * Вернуть true, если элемент был успешно добавлен,
     * или false, если такой элемент уже был в таблице, или превышена вместимость таблицы.
     */

    private fun generateKey(element: T): Int {
        var key = element.hashCode() % capacity
        if (key < 0) {
            key += capacity
        }
        return key
    }

    // Линейное пробирование
    fun add(element: T): Boolean {
        if (size == capacity || contains(element)) {
            return false
        }
        var key = generateKey(element)
        while (elements[key] != null) {
            key = (key + 1) % capacity
        }
        elements[key] = element
        added += 1
        return true
    }


    // Немного самодеятельности
    fun delete(element: T): Boolean {
        if (!contains(element)) return false
        val hashCode = element.hashCode()
        var key = generateKey(element)
        while (elements[key] != element) {
            key = (key + 1) % capacity
        }
        elements[key] = null
        while (elements[(key + 1) % capacity].hashCode() == hashCode) {
            elements[key] = elements[(key + 1) % capacity]
            elements[(key + 1) % capacity] = null
            key = (key + 1) % capacity
        }
        added--
        return true
    }

    /**
     * Проверка, входит ли заданный элемент в хеш-таблицу
     */
    operator fun contains(element: T): Boolean {
        var key = generateKey(element)
        if (elements[key] == element) return true

        val start = key
        key = (key + 1) % capacity

        while (elements[key] != element && elements[key] != null && key != start) {
            key = (key + 1) % capacity
        }

        if (elements[key] == element) return true

        return false
    }

    /**
     * Таблицы равны, если в них одинаковое количество элементов,
     * и любой элемент из второй таблицы входит также и в первую
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as OpenHashSet<T>
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

        val elementsForHash = Array<Any?>(added) { null } as Array<T>
        var i = 0

        for (element in elements) {
            if (element != null) {
                elementsForHash[i] = element
                i++
            }
        }

        elementsForHash.sort()

        for (element in elementsForHash) {
            result = (result * 31) + element.hashCode()
        }

        return result
    }
}
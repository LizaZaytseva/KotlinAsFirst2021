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
private const val PROBING_CONSTANT = 1

class OpenHashSet<T>(val capacity: Int) {

    private var added = 0

    /**
     * Массив для хранения элементов хеш-таблицы
     */
    internal val elements = Array<Any?>(capacity) { null }
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
    // Линейное пробирование
    private fun findKey(element: T): Int {
        var key = element.hashCode() % capacity
        if (key < 0) {
            key += capacity
        }
        if (elements[key] == null || elements[key] == element) return key
        val start = key
        key = getNextKey(key)
        while (elements[key] != null && elements[key] != element && key != start) {
            key = getNextKey(key)
        }
        return key
    }

    private fun getNextKey(key: Int): Int = (key + PROBING_CONSTANT) % capacity

    fun add(element: T): Boolean {
        if (size == capacity) return false
        val key = findKey(element)
        if (elements[key] != null) return false
        elements[key] = element
        added += 1
        return true
    }

    // Немного самодеятельности
    // P. S. "Ленивый алгоритм" с маркировкой мест удаления - для неудачников

    fun delete(element: T): Boolean {
        var deletionKey = findKey(element)

        if (elements[deletionKey] == null) return false

        var currentKey = getNextKey(deletionKey)

        while (elements[currentKey] != null && currentKey != deletionKey) {
            var firstKey = elements[currentKey].hashCode() % capacity
            if (firstKey < 0) {
                firstKey += capacity
            }

            if ((currentKey < deletionKey && (firstKey in (currentKey + 1)..deletionKey)) ||
                (currentKey > deletionKey && (firstKey <= deletionKey || firstKey > currentKey))
            ) {
                elements[deletionKey] = elements[currentKey]
                deletionKey = currentKey
            }

            currentKey = getNextKey(currentKey)
        }
        elements[deletionKey] = null

        added--
        return true
    }

    /**
     * Проверка, входит ли заданный элемент в хеш-таблицу
     */
    operator fun contains(element: T): Boolean {
        val key = findKey(element)
        return (elements[key] == element)
    }

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
        var result = 0
        for (element in elements) {
            if (element != null) {
                result += element.hashCode()
            }
        }
        return result
    }
}
@file:Suppress("UNUSED_PARAMETER")

package lesson8.task1

import kotlin.math.abs
import kotlin.math.min

/**
 * Точка (гекс) на шестиугольной сетке.
 * Координаты заданы как в примере (первая цифра - y, вторая цифра - x)
 *
 *       60  61  62  63  64  65
 *     50  51  52  53  54  55  56
 *   40  41  42  43  44  45  46  47
 * 30  31  32  33  34  35  36  37  38
 *   21  22  23  24  25  26  27  28
 *     12  13  14  15  16  17  18
 *       03  04  05  06  07  08
 *
 * В примерах к задачам используются те же обозначения точек,
 * к примеру, 16 соответствует HexPoint(x = 6, y = 1), а 41 -- HexPoint(x = 1, y = 4).
 *
 * В задачах, работающих с шестиугольниками на сетке, считать, что они имеют
 * _плоскую_ ориентацию:
 *  __
 * /  \
 * \__/
 *
 * со сторонами, параллельными координатным осям сетки.
 *
 * Более подробно про шестиугольные системы координат можно почитать по следующей ссылке:
 *   https://www.redblobgames.com/grids/hexagons/
 */
data class HexPoint(val x: Int, val y: Int) {
    /**
     * Средняя (3 балла)
     *
     * Найти целочисленное расстояние между двумя гексами сетки.
     * Расстояние вычисляется как число единичных отрезков в пути между двумя гексами.
     * Например, путь межу гексами 16 и 41 (см. выше) может проходить через 25, 34, 43 и 42 и имеет длину 5.
     */
    fun distance(other: HexPoint): Int {
        var result = maxOf(abs(this.x - other.x), abs(this.y - other.y))
        if ((x - other.x) * (y - other.y) > 0) {
            result += minOf(abs(this.x - other.x), abs(this.y - other.y))
        }
        return result
    }

    override fun toString(): String = "$y.$x"
}


/**
 * Правильный шестиугольник на гексагональной сетке.
 * Как окружность на плоскости, задаётся центральным гексом и радиусом.
 * Например, шестиугольник с центром в 33 и радиусом 1 состоит из гексов 42, 43, 34, 24, 23, 32.
 */
data class Hexagon(val center: HexPoint, val radius: Int) {

    /**
     * Средняя (3 балла)
     *
     * Рассчитать расстояние между двумя шестиугольниками.
     * Оно равно расстоянию между ближайшими точками этих шестиугольников,
     * или 0, если шестиугольники имеют общую точку.
     *
     * Например, расстояние между шестиугольником A с центром в 31 и радиусом 1
     * и другим шестиугольником B с центром в 26 и радиуоом 2 равно 2
     * (расстояние между точками 32 и 24)
     */
    fun distance(other: Hexagon): Int {
        if (center.distance(other.center) <= radius + other.radius) {
            return 0
        }
        return center.distance(other.center) - radius - other.radius
    }

    /**
     * Тривиальная (1 балл)
     *
     * Вернуть true, если заданная точка находится внутри или на границе шестиугольника
     */
    fun contains(point: HexPoint): Boolean = center.distance(point) <= radius

    fun getPerimeterPoints(): Set<HexPoint> {
        if (radius == 0) return mutableSetOf(center)
        val result = mutableSetOf<HexPoint>()
        var segment = HexSegment(center.move(Direction.UP_RIGHT, radius), center.move(Direction.UP_LEFT, radius))
        result.addAll(segment.getPoints())
        for (i in 0 until 5) {
            segment = HexSegment(segment.end, segment.end.move(segment.direction().next(), radius))
            result.addAll(segment.getPoints())
        }
        return result
    }
}
/**
 * Прямолинейный отрезок между двумя гексами
 */
class HexSegment(val begin: HexPoint, val end: HexPoint) {
    /**
     * Простая (2 балла)
     *
     * Определить "правильность" отрезка.
     * "Правильным" считается только отрезок, проходящий параллельно одной из трёх осей шестиугольника.
     * Такими являются, например, отрезок 30-34 (горизонталь), 13-63 (прямая диагональ) или 51-24 (косая диагональ).
     * А, например, 13-26 не является "правильным" отрезком.
     */
    fun isValid(): Boolean {
        if (begin == end) return false
        if (begin.x != end.x && begin.y != end.y) {
            return (begin.x - end.x) * (begin.y - end.y) < 0 && abs(begin.x - end.x) == abs(begin.y - end.y)
        }
        return true
    }

    /**
     * Средняя (3 балла)
     *
     * Вернуть направление отрезка (см. описание класса Direction ниже).
     * Для "правильного" отрезка выбирается одно из первых шести направлений,
     * для "неправильного" -- INCORRECT.
     */
    fun direction(): Direction {
        if (!this.isValid()) {
            return Direction.INCORRECT
        }
        if (begin.x == end.x) {
            return when {
                begin.y < end.y -> Direction.UP_RIGHT
                else -> Direction.DOWN_LEFT
            }
        }
        if (begin.y == end.y) {
            return when {
                begin.x < end.x -> Direction.RIGHT
                else -> Direction.LEFT
            }
        }
        if (begin.x > end.x && begin.y < end.y) return Direction.UP_LEFT
        return Direction.DOWN_RIGHT
    }

    fun length(): Int = begin.distance(end)
    fun getPoints(): Set<HexPoint> {
        val result = mutableSetOf(begin, end)
        var currentPoint = begin.move(direction(), 1)
        for (i in 1 until length()) {
            result.add(currentPoint)
            currentPoint = currentPoint.move(direction(), 1)
        }
        return result
    }

    fun isParallel(other: HexSegment): Boolean = this.direction().isParallel(other.direction())
    fun isParallel(direction: Direction): Boolean = this.direction().isParallel(direction)

    override fun equals(other: Any?) =
        other is HexSegment && (begin == other.begin && end == other.end || end == other.begin && begin == other.end)

    override fun hashCode() =
        begin.hashCode() + end.hashCode()
}

/**
 * Направление отрезка на гексагональной сетке.
 * Если отрезок "правильный", то он проходит вдоль одной из трёх осей шестугольника.
 * Если нет, его направление считается INCORRECT
 */
enum class Direction {
    RIGHT,      // слева направо, например 30 -> 34
    UP_RIGHT,   // вверх-вправо, например 32 -> 62
    UP_LEFT,    // вверх-влево, например 25 -> 61
    LEFT,       // справа налево, например 34 -> 30
    DOWN_LEFT,  // вниз-влево, например 62 -> 32
    DOWN_RIGHT, // вниз-вправо, например 61 -> 25
    INCORRECT;  // отрезок имеет изгиб, например 30 -> 55 (изгиб в точке 35)

    /**
     * Простая (2 балла)
     *
     * Вернуть направление, противоположное данному.
     * Для INCORRECT вернуть INCORRECT
     */
    fun opposite(): Direction {
        return when (this) {
            RIGHT -> LEFT
            LEFT -> RIGHT
            UP_RIGHT -> DOWN_LEFT
            DOWN_LEFT -> UP_RIGHT
            DOWN_RIGHT -> UP_LEFT
            UP_LEFT -> DOWN_RIGHT
            else -> INCORRECT
        }
    }

    /**
     * Средняя (3 балла)
     *
     * Вернуть направление, повёрнутое относительно
     * заданного на 60 градусов против часовой стрелки.
     *
     * Например, для RIGHT это UP_RIGHT, для UP_LEFT это LEFT, для LEFT это DOWN_LEFT.
     * Для направления INCORRECT бросить исключение IllegalArgumentException.
     * При решении этой задачи попробуйте обойтись без перечисления всех семи вариантов.
     */
    fun next(): Direction {
        if (this == INCORRECT) throw IllegalArgumentException("Incorrect direction")
        return when (this) {
            RIGHT -> UP_RIGHT
            UP_RIGHT -> UP_LEFT
            UP_LEFT -> LEFT
            else -> this.opposite().next().opposite()
        }
    }

    /**
     * Простая (2 балла)
     *
     * Вернуть true, если данное направление совпадает с other или противоположно ему.
     * INCORRECT не параллельно никакому направлению, в том числе другому INCORRECT.
     */
    fun isParallel(other: Direction): Boolean {
        if (this in setOf(RIGHT, LEFT) && other in setOf(RIGHT, LEFT)) return true
        if (this in setOf(UP_RIGHT, DOWN_LEFT) && other in setOf(UP_RIGHT, DOWN_LEFT)) return true
        if (this in setOf(DOWN_RIGHT, UP_LEFT) && other in setOf(DOWN_RIGHT, UP_LEFT)) return true
        return false
    }
}

/**
 * Средняя (3 балла)
 *
 * Сдвинуть точку в направлении direction на расстояние distance.
 * Бросить IllegalArgumentException(), если задано направление INCORRECT.
 * Для расстояния 0 и направления не INCORRECT вернуть ту же точку.
 * Для отрицательного расстояния сдвинуть точку в противоположном направлении на -distance.
 *
 * Примеры:
 * 30, direction = RIGHT, distance = 3 --> 33
 * 35, direction = UP_LEFT, distance = 2 --> 53
 * 45, direction = DOWN_LEFT, distance = 4 --> 05
 */
fun HexPoint.move(direction: Direction, distance: Int): HexPoint {
    if (direction == Direction.INCORRECT) throw IllegalArgumentException("Incorrect direction")
    return when (direction) {
        Direction.RIGHT -> HexPoint(this.x + distance, this.y)
        Direction.UP_RIGHT -> HexPoint(this.x, this.y + distance)
        Direction.UP_LEFT -> HexPoint(this.x - distance, this.y + distance)
        else -> this.move(direction.opposite(), -distance)
    }
}

/**
 * Сложная (5 баллов)
 *
 * Найти кратчайший путь между двумя заданными гексами, представленный в виде списка всех гексов,
 * которые входят в этот путь.
 * Начальный и конечный гекс также входят в данный список.
 * Если кратчайших путей существует несколько, вернуть любой из них.
 *
 * Пример (для координатной сетки из примера в начале файла):
 *   pathBetweenHexes(HexPoint(y = 2, x = 2), HexPoint(y = 5, x = 3)) ->
 *     listOf(
 *       HexPoint(y = 2, x = 2),
 *       HexPoint(y = 2, x = 3),
 *       HexPoint(y = 3, x = 3),
 *       HexPoint(y = 4, x = 3),
 *       HexPoint(y = 5, x = 3)
 *     )
 */
fun pathBetweenHexes(from: HexPoint, to: HexPoint): List<HexPoint> = TODO()

/**
 * Очень сложная (20 баллов)
 *
 * Дано три точки (гекса). Построить правильный шестиугольник, проходящий через них
 * (все три точки должны лежать НА ГРАНИЦЕ, а не ВНУТРИ, шестиугольника).
 * Все стороны шестиугольника должны являться "правильными" отрезками.
 * Вернуть null, если такой шестиугольник построить невозможно.
 * Если шестиугольников существует более одного, выбрать имеющий минимальный радиус.
 *
 * Пример: через точки 13, 32 и 44 проходит правильный шестиугольник с центром в 24 и радиусом 2.
 * Для точек 13, 32 и 45 такого шестиугольника не существует.
 * Для точек 32, 33 и 35 следует вернуть шестиугольник радиусом 3 (с центром в 62 или 05).
 *
 * Если все три точки совпадают, вернуть шестиугольник нулевого радиуса с центром в данной точке.
 */

fun hexagonByThreeConsPoints(ab: HexSegment, bc: HexSegment, ac: HexSegment): Hexagon {
    val maxSegment = if (ab.length() > bc.length()) {
        if (ab.length() > ac.length()) {
            ab
        } else {
            ac
        }
    } else {
        if (bc.length() > ac.length()) {
            bc
        } else {
            ac
        }
    }
    val radius = maxSegment.length()
    return Hexagon(maxSegment.end.move(maxSegment.direction().next().next(), radius), radius)
}


fun hexagonByThreePoints(a: HexPoint, b: HexPoint, c: HexPoint): Hexagon? {
    if (a == b && b == c) {
        return Hexagon(a, 0)
    }
    var numberOfSegments = 0
    val abSegment = HexSegment(a, b)
    val bcSegment = HexSegment(b, c)
    val acSegment = HexSegment(a, c)
    if (abSegment.isValid()) numberOfSegments++
    if (bcSegment.isValid()) numberOfSegments++
    if (acSegment.isValid()) numberOfSegments++
    if (numberOfSegments == 3) {
        if (abSegment.isParallel(bcSegment) && acSegment.isParallel(bcSegment)) {
            return hexagonByThreeConsPoints(abSegment, bcSegment, acSegment)
        }
    }

    return hexagonByThreeNonConsPoints(a, b, c)
}

fun checkIntersect(intersect: Set<HexPoint>, i: Int): Boolean {
    val intersectList = intersect.toList()
    val distance = intersectList[0].distance(intersectList[1])
    if (distance > i + 150) {
        return true
    }
    return false
}
/*
fun hexagonByThreeNonConsPoints(a: HexPoint, b: HexPoint, c: HexPoint): Hexagon? {
    val maxDistance = maxOf(a.distance(b), b.distance(c), a.distance(c))
    var i = maxDistance / 2
    while (true) {
        val aHexagon = Hexagon(a, i)
        val bHexagon = Hexagon(b, i)
        val cHexagon = Hexagon(c, i)

        val aPoints = aHexagon.getPerimeterPoints()
        val bPoints = bHexagon.getPerimeterPoints()
        val cPoints = cHexagon.getPerimeterPoints()

        val ab = aPoints.intersect(bPoints)
        val bc = bPoints.intersect(cPoints)
        val ca = cPoints.intersect(aPoints)

        var sum = 0
        var checkSum = 0

        if (ab.size > 1) {
            if (checkIntersect(ab, i)) {
                checkSum++
            }
            sum++
        }
        if (bc.size > 1) {
            if (checkIntersect(bc, i)) {
                checkSum++
            }
            sum++
        }
        if (ca.size > 1) {
            if (checkIntersect(ca, i)) {
                checkSum++
            }
            sum++
        }

        if (sum == 3) {
            if (checkSum == 3) {
                return null
            }
            val intersection = aPoints.intersect(bPoints).intersect(cPoints)
            if (intersection.isNotEmpty()) {
                return Hexagon(intersection.toList()[0], i)
            }
        }

        i++
    }
}

 */

fun hexagonByThreeNonConsPoints(a: HexPoint, b: HexPoint, c: HexPoint): Hexagon? {
    val maxDistance = maxOf(a.distance(b), b.distance(c), a.distance(c))
    for (i in maxDistance / 2 until maxDistance * 6) {
        val aHexagon = Hexagon(a, i)
        val bHexagon = Hexagon(b, i)
        val cHexagon = Hexagon(c, i)

        val aPoints = aHexagon.getPerimeterPoints()
        val bPoints = bHexagon.getPerimeterPoints()
        val cPoints = cHexagon.getPerimeterPoints()

        val ab = aPoints.intersect(bPoints)
        val bc = bPoints.intersect(cPoints)
        val ca = cPoints.intersect(aPoints)

        if (ab.size > 1 && bc.size > 1 && ca.size > 1) {
            val intersection = ab.intersect(bc).intersect(ca)
            if (intersection.isNotEmpty()) {
                return Hexagon(intersection.toList()[0], i)
            }

            val aDistance = bc.toList()[0].distance(a)
            val bDistance = ca.toList()[0].distance(b)
            val cDistance = ab.toList()[0].distance(c)


            if (aDistance < i && bDistance < i && cDistance < i) {
                return null
            }
        }
    }

    return null
}



/**
 * Очень сложная (20 баллов)
 *
 * Дано множество точек (гексов). Найти правильный шестиугольник минимального радиуса,
 * содержащий все эти точки (безразлично, внутри или на границе).
 * Если множество пустое, бросить IllegalArgumentException.
 * Если множество содержит один гекс, вернуть шестиугольник нулевого радиуса с центром в данной точке.
 *
 * Пример: 13, 32, 45, 18 -- шестиугольник радиусом 3 (с центром, например, в 15)
 */
fun minContainingHexagon(vararg points: HexPoint): Hexagon = TODO()




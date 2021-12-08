@file:Suppress("UNUSED_PARAMETER")

package lesson11.task1

import kotlin.math.pow

/**
 * Класс "полином с вещественными коэффициентами".
 *
 * Общая сложность задания -- средняя, общая ценность в баллах -- 16.
 * Объект класса -- полином от одной переменной (x) вида 7x^4+3x^3-6x^2+x-8.
 * Количество слагаемых неограничено.
 *
 * Полиномы можно складывать -- (x^2+3x+2) + (x^3-2x^2-x+4) = x^3-x^2+2x+6,
 * вычитать -- (x^3-2x^2-x+4) - (x^2+3x+2) = x^3-3x^2-4x+2,
 * умножать -- (x^2+3x+2) * (x^3-2x^2-x+4) = x^5+x^4-5x^3-3x^2+10x+8,
 * делить с остатком -- (x^3-2x^2-x+4) / (x^2+3x+2) = x-5, остаток 12x+16
 * вычислять значение при заданном x: при x=5 (x^2+3x+2) = 42.
 *
 * В конструктор полинома передаются его коэффициенты, начиная со старшего.
 * Нули в середине и в конце пропускаться не должны, например: x^3+2x+1 --> Polynom(1.0, 2.0, 0.0, 1.0)
 * Старшие коэффициенты, равные нулю, игнорировать, например Polynom(0.0, 0.0, 5.0, 3.0) соответствует 5x+3
 */
class Polynom(vararg coeffs: Double) {
    private val coeffs: DoubleArray

    init {
        var zeroes = 0
        while (zeroes < coeffs.size && coeffs[zeroes] == 0.0) {
            zeroes++
        }
        if (zeroes == coeffs.size) {
            this.coeffs = DoubleArray(1) { 0.0 }
        } else {
            val result = DoubleArray(coeffs.size - zeroes)
            for (i in zeroes until coeffs.size) {
                result[i - zeroes] = coeffs[i]
            }
            this.coeffs = result
        }

    }

    /**
     * Геттер: вернуть значение коэффициента при x^i
     */
    fun coeff(i: Int): Double {
        if (i > degree()) {
            return 0.0
        }
        return coeffs[coeffs.size - (1 + i)]
    }

    /**
     * Расчёт значения при заданном x
     */
    fun getValue(x: Double): Double {
        var sum = 0.0
        for (i in coeffs.indices) {
            sum += x.pow(i) * coeff(i)
        }
        return sum
    }

    /**
     * Степень (максимальная степень x при ненулевом слагаемом, например 2 для x^2+x+1).
     *
     * Степень полинома с нулевыми коэффициентами считать равной 0.
     * Слагаемые с нулевыми коэффициентами игнорировать, т.е.
     * степень 0x^2+0x+2 также равна 0.
     */
    fun degree(): Int = coeffs.size - 1

    /**
     * Сложение
     */

    private fun plusMinus(other: Polynom, type: Int): Polynom {
        val newDegree = maxOf(this.degree(), other.degree())
        val newCoeffs = DoubleArray(newDegree + 1)

        for (i in 0..newDegree) {
            newCoeffs[i] = this.coeff(i) + type * other.coeff(i)
        }

        return Polynom(*newCoeffs.reversed().toDoubleArray())
    }

    operator fun plus(other: Polynom): Polynom = plusMinus(other, 1)

    /**
     * Смена знака (при всех слагаемых)
     */
    operator fun unaryMinus(): Polynom {
        val newCoeffs = DoubleArray(this.degree() + 1)
        for (i in newCoeffs.indices) {
            newCoeffs[i] = -coeffs[i]
        }
        return Polynom(*newCoeffs)
    }

    /**
     * Вычитание
     */
    operator fun minus(other: Polynom): Polynom = plusMinus(other, -1)

    /**
     * Умножение
     */
    operator fun times(other: Polynom): Polynom {
        val newDegree = this.degree() + other.degree()
        val newCoeffs = DoubleArray(newDegree + 1)
        for (i in 0..this.degree()) {
            for (j in 0..other.degree()) {
                newCoeffs[i + j] += this.coeff(i) * other.coeff(j)
            }
        }
        return Polynom(*newCoeffs.reversed().toDoubleArray())
    }

    /**
     * Деление
     *
     * Про операции деления и взятия остатка см. статью Википедии
     * "Деление многочленов столбиком". Основные свойства:
     *
     * Если A / B = C и A % B = D, то A = B * C + D и степень D меньше степени B
     */

    private fun division(other: Polynom): Pair<Polynom, Polynom> {
        val dividendCoeffs = coeffs.clone()
        val otherDegree = other.degree()
        val thisDegree = this.degree()
        if (other.degree() > thisDegree) {
            return Pair(Polynom(0.0), this)
        }
        val result = mutableListOf<Double>()
        for (position in 0..(thisDegree - otherDegree)) {
            val currentFraction = dividendCoeffs[position] / other.coeff(otherDegree)
            result.add(currentFraction)

            val currentSubtrahend = DoubleArray(otherDegree + 1)
            for (i in currentSubtrahend.indices) {
                currentSubtrahend[i] = other.coeffs[i] * currentFraction
            }
            for (i in 0..otherDegree) {
                dividendCoeffs[i + position] -= currentSubtrahend[i]
            }

        }
        return Pair(Polynom(*result.toDoubleArray()), Polynom(*dividendCoeffs))
    }

    operator fun div(other: Polynom): Polynom = division(other).first

    /**
     * Взятие остатка
     */
    operator fun rem(other: Polynom): Polynom = division(other).second

    /**
     * Сравнение на равенство
     */

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Polynom) return false
        if (this.degree() != other.degree()) return false
        for (i in this.coeffs.indices) {
            if (this.coeff(i) != other.coeff(i)) {
                return false
            }
        }
        return true
    }

    /**
     * Получение хеш-кода
     */
    override fun hashCode(): Int {
        var result = 13
        for (coeff in coeffs) {
            result = (result * 31) + coeff.hashCode()
        }
        return result
    }
}

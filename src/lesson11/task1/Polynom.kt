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


    private val coeffs = coeffs
    /**
     * Геттер: вернуть значение коэффициента при x^i
     */
    fun coeff(i: Int): Double = coeffs[coeffs.size - (1 + i)]

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
    fun degree(): Int {
        for (i in coeffs.indices) {
            if (coeffs[i] != 0.0) {
                return coeffs.size - (i + 1)
            }
        }
        return 0
    }

    /**
     * Сложение
     */
    operator fun plus(other: Polynom): Polynom {
        val newDegree = maxOf(this.degree(), other.degree())
        val thisDifference = newDegree - this.degree()
        val otherDifference = newDegree - other.degree()
        val newCoeffs = DoubleArray(newDegree + 1)
        for (i in 0..newDegree) {
            var currentCoeff = 0.0
            if (i - thisDifference >= 0) {
                currentCoeff += this.coeff(this.degree() - (i - thisDifference))
            }
            if (i - otherDifference >= 0) {
                currentCoeff += other.coeff(other.degree() - (i - otherDifference))
            }
            newCoeffs[i] = currentCoeff
        }
        return Polynom(*newCoeffs)
    }

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
    operator fun minus(other: Polynom): Polynom {
        val newDegree = maxOf(this.degree(), other.degree())
        val thisDifference = newDegree - this.degree()
        val otherDifference = newDegree - other.degree()
        val newCoeffs = DoubleArray(newDegree + 1)
        for (i in 0..newDegree) {
            var currentCoeff = 0.0
            if (i - thisDifference >= 0) {
                currentCoeff += this.coeff(this.degree() - (i - thisDifference))
            }
            if (i - otherDifference >= 0) {
                currentCoeff -= other.coeff(other.degree() - (i - otherDifference))
            }
            newCoeffs[i] = currentCoeff
        }
        return Polynom(*newCoeffs)
    }

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
    operator fun div(other: Polynom): Polynom = TODO()

    /**
     * Взятие остатка
     */
    operator fun rem(other: Polynom): Polynom = TODO()

    /**
     * Сравнение на равенство
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Polynom) return false
        if (this.degree() != other.degree()) return false
        for (i in coeffs.indices) {
            if (this.coeff(i) != other.coeff(i)) {
                return false
            }
        }
        return true
    }

    /**
     * Получение хеш-кода
     */
    override fun hashCode(): Int = TODO()
}

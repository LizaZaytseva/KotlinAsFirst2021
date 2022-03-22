package lesson5.task1

import junit.framework.Assert.assertEquals
import lesson5.task1.findSumOfTwo
import org.junit.Test

class SoftwareTesting5 {

    @Test
    fun testFindSumOfTwoNeg() {
        assertEquals(Pair(-1, -1), findSumOfTwo(emptyList(), 2))
        assertEquals(Pair(-1, -1), findSumOfTwo(listOf(1), 3))
        assertEquals(Pair(-1, -1), findSumOfTwo(listOf(1, 1), 5))
        assertEquals(Pair(-1, -1), findSumOfTwo(listOf(1, ), 5))
    }

    @Test
    fun testFindSumOfTwoPos(){
        assertEquals(Pair(0, 1), findSumOfTwo(listOf(1, 1), 2))
        assertEquals(Pair(2, 3), findSumOfTwo(listOf(1, 2, 3, 4, 7, 8), 7))
        assertEquals(Pair(0, 1), findSumOfTwo(listOf(4, 6, 4, 6), 10))
    }
}
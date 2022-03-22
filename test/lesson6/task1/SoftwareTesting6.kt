package lesson6.task1;

import junit.framework.Assert.assertEquals
import lesson6.task1.plusMinus
import org.junit.Test;
import org.junit.jupiter.api.Assertions

class SoftwareTesting6 {
    @Test
    fun testPlusMinusPos() {
        assertEquals(1, plusMinus("1"))
        assertEquals(0, plusMinus("0 + 0"))
        assertEquals(1, plusMinus("0 + 1"))
        assertEquals(0, plusMinus("0 - 0"))
        assertEquals(-1, plusMinus("0 - 1"))
        assertEquals(30, plusMinus("10 + 20"))
        assertEquals(6, plusMinus("1 + 2 + 3"))
        assertEquals(-4, plusMinus("1 - 2 - 3"))
    }

    @Test
    fun testPlusMinusNeg() {
        Assertions.assertThrows(IllegalArgumentException::class.java) { plusMinus("- +") }
        Assertions.assertThrows(IllegalArgumentException::class.java) { plusMinus("- 1 + 1") }
        Assertions.assertThrows(IllegalArgumentException::class.java) { plusMinus("1 - 1 +") }
        Assertions.assertThrows(IllegalArgumentException::class.java) { plusMinus("1 - - 1") }
        Assertions.assertThrows(IllegalArgumentException::class.java) { plusMinus("1 +") }
        Assertions.assertThrows(IllegalArgumentException::class.java) { plusMinus("1-") }
        Assertions.assertThrows(IllegalArgumentException::class.java) { plusMinus("- 1") }
        Assertions.assertThrows(IllegalArgumentException::class.java) { plusMinus("+1") }
        Assertions.assertThrows(IllegalArgumentException::class.java) { plusMinus("") }
        Assertions.assertThrows(IllegalArgumentException::class.java) { plusMinus("1 1") }
        Assertions.assertThrows(IllegalArgumentException::class.java) { plusMinus("1+1") }
        Assertions.assertThrows(IllegalArgumentException::class.java) { plusMinus("1-1") }
        Assertions.assertThrows(IllegalArgumentException::class.java) { plusMinus(" 1") }
        Assertions.assertThrows(IllegalArgumentException::class.java) { plusMinus("1 ") }
    }
}
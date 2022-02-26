package lesson12.task1

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag

class OpenHashSetTest {

    @Test
    @Tag("6")
    fun size() {
        val set = OpenHashSet<Int>(32)
        assertEquals(32, set.capacity)
        assertEquals(0, set.size)
        set.add(42)
        set.add(42)
        assertEquals(1, set.size)
        set.add(21)
        assertEquals(2, set.size)
    }

    @Test
    @Tag("4")
    fun isEmpty() {
        val set = OpenHashSet<Int>(16)
        assertTrue(set.isEmpty())
        set.add(0)
        assertFalse(set.isEmpty())
    }

    @Test
    @Tag("10")
    fun add() {
        val set = OpenHashSet<String>(4)
        assertTrue(set.add("alpha"))
        assertTrue(set.add("beta"))
        assertFalse(set.add("alpha"))
        assertTrue(set.add("gamma"))
        assertTrue(set.add("omega"))
        for (word in listOf("alpha", "beta", "gamma", "omega")) {
            assertTrue(word in set.elements)
        }
        assertFalse(set.add("???"))
    }

    @Test
    fun delete() {
        val set1 = OpenHashSet<String>(4)
        assertTrue(set1.add("alpha"))
        assertTrue(set1.add("beta"))
        assertFalse(set1.delete("gamma"))
        assertTrue(set1.add("gamma"))
        assertTrue(set1.delete("alpha"))

        val set2 = OpenHashSet<String>(3)
        assertTrue(set2.add("beta"))
        assertTrue(set2.add("gamma"))

        assertEquals(set1, set2)
        set1.add("omicron")

        assertNotEquals(set1, set2)

        val set3 = OpenHashSet<Int>(4)
        assertTrue(set3.add(3))
        assertTrue(set3.add(7))
        assertTrue(set3.add(-1))

        assertTrue(set3.delete(7))
        assertFalse(set3.delete(7))

        val set4 = OpenHashSet<Int>(5)

        assertTrue(set4.add(-1))
        assertTrue(set4.add(3))
        assertTrue(set4.delete(3))
        assertTrue(set4.add(3))
        assertTrue(set4.add(4))
        assertTrue(set3.add(4))

        assertEquals(set3, set4)

        val set5 = OpenHashSet<Int>(4)
        assertTrue(set5.add(0))
        assertTrue(set5.add(1))
        assertTrue(set5.add(2))
        assertTrue(set5.add(4))

        assertTrue(set5.delete(1))

        assertTrue(set5.contains(0))
        assertFalse(set5.contains(1))
        assertTrue(set5.contains(2))
        assertTrue(set5.contains(4))

        val set6 = OpenHashSet<Int>(5)

        assertTrue(set6.add(0))
        assertTrue(set6.add(2))
        assertTrue(set6.add(4))

        assertEquals(set5, set6)


    }


    @Test
    @Tag("10")
    fun contains() {
        val set = OpenHashSet<Int>(8)
        set.add(1)
        set.add(3)
        set.add(6)
        set.add(11)
        set.add(14)
        assertTrue(3 in set)
        assertTrue(11 in set)
        assertTrue(14 in set)
        assertFalse(4 in set)
    }

    @Test
    @Tag("8")
    fun testEquals() {
        val set1 = OpenHashSet<Int>(8)
        set1.add(1)
        set1.add(3)
        set1.add(6)
        val set2 = OpenHashSet<Int>(4)
        set2.add(1)
        set2.add(3)
        set2.add(6)
        assertTrue(set1 == set2)
        set1.add(10)
        assertFalse(set1 == set2)
        val set3 = OpenHashSet<Int>(8)
        val set4 = OpenHashSet<Int>(4)
        assertTrue(set3 == set4)
    }

    @Test
    @Tag("6")
    fun testHashCode() {
        val set1 = OpenHashSet<Int>(8)
        set1.add(1)
        set1.add(3)
        set1.add(6)
        val set2 = OpenHashSet<Int>(4)
        set2.add(1)
        set2.add(3)
        set2.add(6)
        assertTrue(set1.hashCode() == set2.hashCode())

        val set3 = OpenHashSet<Char>(8)
        set3.add('a')
        set3.add('b')
        set3.add('c')
        val set4 = OpenHashSet<Char>(4)
        set4.add('a')
        set4.add('b')
        set4.add('c')
        assertTrue(set3.hashCode() == set4.hashCode())
    }
}
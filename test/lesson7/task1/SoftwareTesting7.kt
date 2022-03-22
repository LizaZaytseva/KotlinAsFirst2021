package lesson7.task1

import org.junit.jupiter.api.Assertions.assertEquals
import lesson7.task1.printDivisionProcess
import org.junit.jupiter.api.Test
import java.io.File

class SoftwareTesting7 {

    private fun assertFileContent(name: String, expectedContent: String) {
        val file = File(name)
        val content = file.readLines().joinToString("\n")
        assertEquals(expectedContent, content)
    }

    fun test(lhv: Int, rhv: Int, res: String) {
        printDivisionProcess(lhv, rhv, "temp.txt")
        assertFileContent("temp.txt", res.trimIndent())
        File("temp.txt").delete()
    }

    @Test
    fun testPrintDivisionProcess() {
        test(
            100,
            25,
            """
              100 | 25
             -100   4
             ----
                0
             """
        )
        test(
            1111,
            25,
            """
              1111 | 25
             -100    44
             ----
               111
              -100
              ----
                11
             """
        )
        test(Int.MAX_VALUE, 2, """
             2147483647 | 2
            -2            1073741823
            --
             01
             -0
             --
              14
             -14
             ---
               07
               -6
               --
                14
               -14
               ---
                 08
                 -8
                 --
                  03
                  -2
                  --
                   16
                  -16
                  ---
                    04
                    -4
                    --
                     07
                     -6
                     --
                      1
             """)
    }
}
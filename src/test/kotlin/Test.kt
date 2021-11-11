import org.junit.jupiter.api.Test
import java.io.*
import java.util.*
import kotlin.test.assertEquals

class Test {
    var testNumber = 3
    fun testInputCorrector() {
        val autocorrector = Autocorrector()
        val scanner = Scanner(File("MODUL2/TESTS/Q/$testNumber.txt"))
        val writer = File("myOutput$testNumber.txt").outputStream()
        val amount = scanner.nextInt()
        for (i in 1..amount) {
            autocorrector.addWordToDictionary(scanner.next())
        }
        while (scanner.hasNext()) {
            val word = scanner.next()
            writer.write(word.toByteArray())
            writer.write(" -".toByteArray())
            try {
                val matched = autocorrector.check(word)
                if (matched.isEmpty()) {
                    writer.write("?\n".toByteArray())
                    continue
                }
                writer.write("> ".toByteArray())
                writer.write(matched[0].toByteArray())
                for (i in 1 until matched.size) {
                    writer.write(", ".toByteArray())
                    writer.write(matched[i].toByteArray())
                }
                writer.write("\n".toByteArray())
            } catch (exception: Exception) {
                println(exception.message)
                writer.write(" ok\n".toByteArray())
            }
        }
    }
    fun testInputHeap() {
        val heap = Heap()
        val scanner = Scanner(File("MODUL2/tests3/input$testNumber.txt"))
        val writer = File("myOutput$testNumber.txt").outputStream()
        while (scanner.hasNext()) {
            try {
                when (scanner.next()) {
                    "add" -> {
                        val key = scanner.nextLong()
                        if (key == 4L) {
                            3
                        }
                        val value = scanner.next()
                        heap.add(key, value)
                    }
                    "set" -> {
                        val key = scanner.nextLong()
                        val newValue = scanner.next()
                        heap.set(key, newValue)
                    }
                    "delete" -> {
                        val key = scanner.nextLong()
                        heap.delete(key)
                    }
                    "search" -> {
                        val key = scanner.nextLong()
                        val pair = heap.search(key)
                        if (pair == null) {
                            writer.write("0\n".toByteArray())
                        } else {
                            writer.write("1 ${pair.first} ${pair.second}\n".toByteArray())
                        }
                    }
                    "min" -> {
                        val triple = heap.min()
                        writer.write("${triple.first} ${triple.second} ${triple.third}\n".toByteArray())
                    }
                    "max" -> {
                        val triple = heap.max()
                        writer.write("${triple.first} ${triple.second} ${triple.third}\n".toByteArray())
                    }
                    "extract" -> {
                        val pair = heap.extract()
                        writer.write("${pair.first} ${pair.second}\n".toByteArray())
                    }
                    "print" -> {
                        heap.print(writer)
                    }
                    else -> {
                        writer.write("error".toByteArray())
                    }
                }
            } catch(exception: Exception) {
                writer.write(exception.message?.toByteArray())
                writer.write("\n".toByteArray())
            }
        }
    }
    fun testInputSplayTree() {
        val splayTree = SplayTree()
        val scanner = Scanner(File("MODUL2/tests2/input$testNumber.txt"))
        val writer = File("myOutput$testNumber.txt").outputStream()
        while (scanner.hasNext()) {
            try {
                // reads command
                when (scanner.next()) {
                    "add" -> {
                        val key = scanner.nextLong()
                        val value = scanner.next()
                        splayTree.add(key, value)
                    } "set" -> {
                        val key = scanner.nextLong()
                        val newValue = scanner.next()
                        splayTree.set(key, newValue)
                    } "delete" -> {
                        val key = scanner.nextLong()
                        splayTree.delete(key)
                    } "search" -> {
                        val key = scanner.nextLong()
                        val value: String? = splayTree.search(key)
                        if (value == null) {
                            writer.write("0\n".toByteArray())
                        } else {
                            writer.write("1 $value\n".toByteArray())
                        }
                    } "min" -> {
                        val pair = splayTree.min()
                        writer.write("${pair.first} ${pair.second}\n".toByteArray())
                    } "max" -> {
                        val pair = splayTree.max()
                        writer.write("${pair.first} ${pair.second}\n".toByteArray())
                    }"print" -> {
                        splayTree.print(writer)
                    } else -> {
                        throw NoSuchMethodException("error")
                    }
                }
            } catch (exception: Exception) {
                writer.write(exception.message?.toByteArray())
                writer.write("\n".toByteArray())
            }
        }
    }
    @Test
    fun testOutput() {
        for (i in 1..10) {
            testNumber = i
            println("test #$i")
            testInputCorrector()
            val sc1 = Scanner(File("myOutput$testNumber.txt"))
            val sc2 = Scanner(File("MODUL2/TESTS/A/$testNumber.txt"))
            var numberOfLine = 1
            while (sc2.hasNext()) {
                val myOutput = sc1.nextLine()
                val trueOutput = sc2.nextLine()
                //println("Line: $numberOfLine")
                assertEquals(trueOutput, myOutput)
                numberOfLine++
            }
        }
    }
}
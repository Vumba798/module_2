import java.io.BufferedWriter
import java.io.File
import java.io.OutputStream
import java.io.OutputStreamWriter

fun test() {
    var writer = BufferedWriter(OutputStreamWriter(System.out))
    var begin = System.currentTimeMillis()
    for (i in 0..100000) {
        writer.write("foo\n")
    }
    writer.flush()
    var end = System.currentTimeMillis()
    val bwTime = end - begin

    val osw = OutputStreamWriter(System.out)
    begin = System.currentTimeMillis()
    for (i in 0..100000) {
        osw.write("foo\n")
    }
    end = System.currentTimeMillis()
    val oswTime = end - begin

    begin = System.currentTimeMillis()
    for (i in 0..100000) {
        System.out.write("foo\n".toByteArray())
    }
    end = System.currentTimeMillis()
    val soTime = end - begin

    val fileBW = BufferedWriter(File("tmp").writer())
    begin = System.currentTimeMillis()
    for (i in 0..100000) {
        fileBW.write("foo\n")
    }
    end = System.currentTimeMillis()
    val fileBWTime = end - begin

    val fileOS = File("tmp").outputStream()
    begin = System.currentTimeMillis()
    for (i in 0..100000) {
        fileOS.write("foo\n".toByteArray())
    }

    end = System.currentTimeMillis()
    val fileOSTime = end - begin

    begin = System.currentTimeMillis()
    end = System.currentTimeMillis()
    val printTime = end - begin

    print("BufferedWriter: ")
    println(bwTime)
    print("OutputStreamWriter: ")
    println(oswTime)
    print("System.out: ")
    println(soTime)
    print("FileBufferedWriter: ")
    println(fileBWTime)
    print("FileOutputStream: ")
    println(fileOSTime)
}

fun print_with_output(out: OutputStream): OutputStream {
    val writer = BufferedWriter(OutputStreamWriter(out))
    for (i in 0..10000) {
        writer.write("joja\n")
    }
    return out
}

fun tmp(i : Int) {
    i.apply {
        this.inc()
    }
    println(i)
}

fun main() {
    for (i in 0 until 3) {
        println(i)
    }
}


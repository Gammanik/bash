package com.bash

import com.bash.commands.Grep
import junit.framework.TestCase.assertEquals
import main.java.com.bash.util.Environment
import org.junit.Test
import java.io.File

class GrepCommandTest {
    private val sep = System.lineSeparator()
    
    @Test
    fun testGrep() {
        val args = listOf("pattern", "src/test/resources/in3.txt")
        val out = Grep.buildArgs(args, "", Environment()).run().stdOut
        val expected = File("src/test/resources/results/in3grep.txt").readText()
        assertEquals(expected, out)
    }

    @Test
    fun testGrepWithArgs() {
        val args = listOf("pattern", "src/test/resources/in3.txt", "-A", "1", "-i")
        val cmd = Grep.buildArgs(args, "", Environment())
        val out = cmd.run().stdOut

        assertEquals(cmd.linesToInclude, 1)
        assertEquals(cmd.caseInsensitivity, true)
        assertEquals(cmd.isWordSearch, false)

        val expected = File("src/test/resources/results/in3grepA1_i.txt").readText()
        assertEquals(expected, out)
    }

    @Test
    fun testGrepA2() {
        val args = listOf("pattern", "src/test/resources/in3.txt", "-A", "2")
        val out = Grep.buildArgs(args, "", Environment()).run().stdOut

        val expected = File("src/test/resources/results/in3grepA2.txt").readText()
        assertEquals(expected, out)
    }

    @Test
    fun testGrepWI() {
        val args = listOf("pattern", "src/test/resources/in3.txt", "-i", "-w")
        val out = Grep.buildArgs(args, "", Environment()).run().stdOut

        val expected = File("src/test/resources/results/in3grep_w_i.txt").readText()
        assertEquals(expected, out)
    }

    @Test
    fun testFromPipe() {
        val pipeInput = "ab${sep}acb${sep}aa${sep}acb${sep}aab"
        val out = Grep.buildArgs(listOf("ab"), pipeInput, Environment()).run().stdOut

        val expected = "ab${sep}aab${sep}"
        assertEquals(expected, out)
    }

    @Test
    fun testNonExistentFile() {
        val filename = "nonExistentFilename.txt"
        val err = Grep.buildArgs(listOf("no", filename), "", Environment()).run().stdErr
        assertEquals(err, "grep: $filename: No such file or directory")
    }

    @Test
    fun testErrorFlagA() {
        val out = Grep.buildArgs(listOf("ab", "-A"), "abc", Environment()).run()

        assertEquals("", out.stdOut)
        assertEquals("Expected a value after parameter -A", out.stdErr)
    }

    @Test
    fun testNegativeValueA() {
        val out = Grep.buildArgs(listOf("ab", "-A", "-1"), "ab", Environment()).run()

        assertEquals("", out.stdOut)
        assertEquals("-A can't have negative value", out.stdErr)
    }

    @Test
    fun testZeroValueA() {
        val out = Grep.buildArgs(listOf("ab", "-A", "0"), "ab${sep}cd", Environment()).run()

        assertEquals("ab${sep}", out.stdOut)
        assertEquals("", out.stdErr)
    }
}

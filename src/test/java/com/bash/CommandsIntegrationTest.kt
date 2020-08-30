package com.bash

import junit.framework.TestCase.assertEquals
import org.junit.Test
import com.bash.util.Settings
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream


class CommandsIntegrationTest {
    
    private val sep = System.lineSeparator() 
    
    @Test
    fun testEcho() {
        val input = "echo hello world"
        checkCommand(input, "hello world")
    }

    @Test
    fun testSimpleEnv() {
        val input = "x=1${sep}echo \$x"
        checkCommand(input, "${Settings.PREFIX}1")
    }

    @Test
    fun testEnvWithQuotes() {
        val input = "x=1${sep}echo \"'\$x'\""
        checkCommand(input, "${Settings.PREFIX}'1'")
    }

    @Test
    fun testWc() {
        val input = "wc src/test/resources/in1.txt"
        checkCommand(input, "\t\t3\t\t4\t\t24 src/test/resources/in1.txt")
    }

    @Test
    fun testWcWithEnv() {
        val input = "FILE=src/test/resources/in1.txt${sep}" +
                "wc \$FILE"
        checkCommand(input, "${Settings.PREFIX}\t\t3\t\t4\t\t24 src/test/resources/in1.txt")
    }

    @Test
    fun testWcSplitTabs() {
        val input = "echo 123 | wc | wc"
        val expectedOut = "\t\t1\t\t3\t\t10"
        checkCommand(input, expectedOut)
    }

    @Test
    fun testCat() {
        val input = "cat src/test/resources/in1.txt"
        val out = File("src/test/resources/in1.txt").readText()
        checkCommand(input, out)
    }

    @Test
    fun testPiped() {
        val input = "echo 1 2 3 | cat"
        checkCommand(input, "1 2 3")
    }

    @Test
    fun testPipedDoubleQuotes() {
        val input = "echo \"lol\" | cat | wc"
        checkCommand(input, "\t\t1\t\t1\t\t4")
    }

    @Test
    fun testPipedDoubleQuotesWords() {
        val input = "echo \"lol 1 2 3\" | cat | wc"
        checkCommand(input, "\t\t1\t\t4\t\t10")
    }

    @Test
    fun testEnvSubstitution() {
        val input = "a=one${sep}" +
                    "b=two${sep}" +
                    "echo \$a\$b"
        checkCommand(input,"${Settings.PREFIX}${Settings.PREFIX}onetwo")
    }

    @Test
    fun testExitFromEnv() {
        val input = "a=ex${sep}" +
                    "b=it${sep}" +
                    "\$a\$b${sep}" +
                    "echo should not print"

        val expectedBashOut = "${Settings.PREFIX}${Settings.PREFIX}${Settings.PREFIX}"
        System.setIn(ByteArrayInputStream(input.toByteArray()))
        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))

        val sh = Bash()
        sh.start()
        assertEquals(expectedBashOut, output.toString())
    }

    @Test
    fun testExitInPipe() {
        val input = "echo a | echo b | exit | echo c"
        checkCommand(input, "c")
    }

    @Test
    fun testErrorOutput() {
        val input = "exit 1 2 | exit 1 2 3 | echo a"
        val expectedOut = "-bash: exit: too many arguments${sep}" +
                "-bash: exit: too many arguments${sep}" +
                "a"
        checkCommand(input, expectedOut)
    }

    @Test
    fun testEmptyPipe() {
        val input = "echo lol |$sep" + "cat"
        val expectedOut = ">lol"
        checkCommand(input, expectedOut)
    }

    @Test
    fun testEmptyPipeClickEnter() {
        val input = "echo lol |$sep$sep$sep$sep cat"
        val expectedOut = ">>>>lol"
        checkCommand(input, expectedOut)
    }

    @Test
    fun testEmptyPipeError() {
        val input = "echo lol |$sep exit 1 2 3"
        val expectedOut = ">-bash: exit: too many arguments$sep"
        checkCommand(input, expectedOut)
    }

    @Test
    fun testEmptyPipePiped() {
        val input = "echo lol |$sep cat | echo 123 | cat"
        val expectedOut = ">123"
        checkCommand(input, expectedOut)
    }

    private fun checkCommand(input: String, expectedOut: String) {
        val command = "$input${sep} exit" // add exit command
        val expectedBashOut =  Settings.PREFIX + "$expectedOut${sep}" + Settings.PREFIX

        System.setIn(ByteArrayInputStream(command.toByteArray()))
        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))

        val sh = Bash()
        sh.start()

        assertEquals(expectedBashOut, ignoreColorChange(output.toString()))
    }

    private fun ignoreColorChange(line: String): String {
        return line.replace(Settings.OUTPUT_COLOR, "", false)
                .replace(Settings.ERR_COLOR, "", false)
                .replace(Settings.ANSI_RESET, "", false)
    }
}

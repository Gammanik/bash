package com.bash

import com.bash.commands.External
import com.bash.commands.Wc
import com.bash.util.Settings
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test
import java.io.File

class CommandsTest {

    @Test
    fun testWcFromPipe() {
        val out = Wc(emptyList(), "count me").run()
        assertEquals("\t\t1\t\t2\t\t9", out.sdtOut)
    }

    @Test
    fun testWcFromPipeSpaces() {
        val out = Wc(emptyList(), "count      me").run()
        assertEquals("\t\t1\t\t2\t\t14", out.sdtOut)
    }

    @Test
    fun testWcFromPipeSpaces2() {
        val out = Wc(emptyList(), "count      me  on 1").run()
        assertEquals("\t\t1\t\t4\t\t20", out.sdtOut)
    }

    @Test
    fun testMultilineWcFromPipe() {
        val content = "count me\n" + "and my second line please"
        val out = Wc(emptyList(), content).run()
        assertEquals("\t\t2\t\t7\t\t35", out.sdtOut)
    }

    @Test
    fun testWcFromFile() {
        val filename = "src/test/resources/in1.txt"
        val out = Wc(listOf(filename), "").run()
        assertEquals("\t\t3\t\t4\t\t24 $filename", out.sdtOut)
    }

    @Test
    fun testWcWithTabs() {
        val content = "\t\t1\t\t1\t\t4"
        val out = Wc(emptyList(), content).run()
        assertEquals("\t\t1\t\t3\t\t10", out.sdtOut)
    }

    @Test
    fun testExternalEcho() {
        val out = External("echo", listOf("lol"), "").run()
        assertEquals("lol", out.sdtOut)
    }

    @Test
    fun testExternalError() {
        val out = External("kkkk", emptyList(), "").run()
        assertEquals("", out.sdtOut)

        if (Settings.IS_WINDOWS) {
            assertTrue(out.stdErr.isNotBlank())
        } else {
            assertEquals("/bin/bash: kkkk: command not found", out.stdErr)
        }
    }

    @Test
    fun testExternalErrorOut() {
        val out = External("git kkkk", emptyList(), "").run()
        assertEquals("", out.sdtOut)
        assertEquals("git: 'kkkk' is not a git command. See 'git --help'.", out.stdErr)
    }

    @Test
    fun testExternalAsEchoTwoArgs() {
        val out = External("echo", listOf("lol", "lol2"), "").run()
        assertEquals("lol lol2", out.sdtOut)
    }

    @Test
    fun testOtherAsCatFromFile() {
        val filename = "src/test/resources/in2.txt"
        val out = External("cat", listOf(filename), "").run()
        File(filename).readText()
        assertEquals(File(filename).readText(), out.sdtOut)
    }

    @Test
    fun testOtherAsCatFromPipe() {
        val out = External("cat", emptyList(), "test from pipe").run()
        assertEquals("test from pipe", out.sdtOut)
    }
}

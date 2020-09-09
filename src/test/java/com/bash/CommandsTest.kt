package com.bash

import com.bash.commands.External
import com.bash.commands.Wc
import com.bash.util.Settings
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import main.java.com.bash.commands.Cd
import main.java.com.bash.commands.Ls
import main.java.com.bash.util.Environment
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
        val out = External("echo", listOf("lol"), "", Environment()).run()
        assertEquals("lol", out.sdtOut)
    }

    @Test
    fun testExternalError() {
        val out = External("kkkk", emptyList(), "", Environment()).run()
        assertEquals("", out.sdtOut)

        if (Settings.IS_WINDOWS) {
            assertTrue(out.stdErr.isNotBlank())
        } else {
            assertEquals("/bin/bash: kkkk: command not found", out.stdErr)
        }
    }

    @Test
    fun testExternalErrorOut() {
        val out = External("git kkkk", emptyList(), "", Environment()).run()
        assertEquals("", out.sdtOut)
        assertEquals("git: 'kkkk' is not a git command. See 'git --help'.", out.stdErr)
    }

    @Test
    fun testExternalAsEchoTwoArgs() {
        val out = External("echo", listOf("lol", "lol2"), "", Environment()).run()
        assertEquals("lol lol2", out.sdtOut)
    }

    @Test
    fun testOtherAsCatFromFile() {
        val filename = "src/test/resources/in2.txt"
        val out = External("cat", listOf(filename), "", Environment()).run()
        File(filename).readText()
        assertEquals(File(filename).readText(), out.sdtOut)
    }

    @Test
    fun testOtherAsCatFromPipe() {
        val out = External("cat", emptyList(), "test from pipe", Environment()).run()
        assertEquals("test from pipe", out.sdtOut)
    }

    @Test
    fun testCd() {
        val env = Environment()
        val dirname = "src"
        val out = Cd(listOf(dirname), env).run()
        assertEquals("", out.stdErr)
    }

    @Test
    fun testCdBadArg() {
        val env = Environment()
        val dirname = "not_existed_dir"
        val out = Cd(listOf(dirname), env).run()
        assertEquals("", out.sdtOut)
        assertEquals("-bash: cd: no such file or directory: not_existed_dir", out.stdErr)
    }

    @Test
    fun testCdGoHome() {
        val env = Environment()
        val dirname = "~"
        val out = Cd(listOf(dirname), env).run()
        assertEquals(System.getProperty("user.home"), env.getDirectory())
        assertEquals("", out.sdtOut)
        assertEquals("", out.stdErr)
    }

    @Test
    fun testCdGoRoot() {
        val env = Environment()
        val dirname = "/"
        val out = Cd(listOf(dirname), env).run()
        assertEquals("/", env.getDirectory())
        assertEquals("", out.sdtOut)
        assertEquals("", out.stdErr)
    }

    @Test
    fun testCdNotADirectory() {
        val env = Environment()
        val oldDir = env.getDirectory()
        val dirname = "README.md"
        val out = Cd(listOf(dirname), env).run()
        assertEquals(oldDir, env.getDirectory())
        assertEquals("", out.sdtOut)
        assertEquals("-bash: cd: not a directory: README.md", out.stdErr)
    }

    @Test
    fun testLs() {
        val env = Environment()
        Cd(listOf("src/"), env).run()
        val out = Ls(listOf(), env).run()

        val sep = System.lineSeparator()
        print(out.sdtOut)
        assertEquals(
                "test${sep}main",
                out.sdtOut
        )
    }

    @Test
    fun testLsWithArgument() {
        val env = Environment()
        val out = Ls(listOf("src/"), env).run()

        val sep = System.lineSeparator()
        print(out.sdtOut)
        assertEquals(
                "test${sep}main",
                out.sdtOut
        )
    }

    @Test
    fun testLsBadArgument() {
        val env = Environment()
        val out = Ls(listOf("bad_directory"), env).run()

        assertEquals("", out.sdtOut)
        assertEquals("ls: bad_directory: No such file or directory", out.stdErr)
    }
}

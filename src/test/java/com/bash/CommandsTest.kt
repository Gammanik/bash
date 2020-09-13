package com.bash

import com.bash.commands.*
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
        val out = Wc(emptyList(), "count me", Environment()).run()
        assertEquals("\t\t1\t\t2\t\t9", out.stdOut)
    }

    @Test
    fun testWcFromPipeSpaces() {
        val out = Wc(emptyList(), "count      me", Environment()).run()
        assertEquals("\t\t1\t\t2\t\t14", out.stdOut)
    }

    @Test
    fun testWcFromPipeSpaces2() {
        val out = Wc(emptyList(), "count      me  on 1", Environment()).run()
        assertEquals("\t\t1\t\t4\t\t20", out.stdOut)
    }

    @Test
    fun testMultilineWcFromPipe() {
        val content = "count me\n" + "and my second line please"
        val out = Wc(emptyList(), content, Environment()).run()
        assertEquals("\t\t2\t\t7\t\t35", out.stdOut)
    }

    @Test
    fun testWcFromFile() {
        val filename = "src/test/resources/in1.txt"
        val out = Wc(listOf(filename), "", Environment()).run()
        assertEquals("\t\t3\t\t4\t\t24 $filename", out.stdOut)
    }

    @Test
    fun testWcWithTabs() {
        val content = "\t\t1\t\t1\t\t4"
        val out = Wc(emptyList(), content, Environment()).run()
        assertEquals("\t\t1\t\t3\t\t10", out.stdOut)
    }

    @Test
    fun testExternalEcho() {
        val out = External("echo", listOf("lol"), "", Environment()).run()
        assertEquals("lol", out.stdOut)
    }

    @Test
    fun testExternalError() {
        val out = External("kkkk", emptyList(), "", Environment()).run()
        assertEquals("", out.stdOut)

        if (Settings.IS_WINDOWS) {
            assertTrue(out.stdErr.isNotBlank())
        } else {
            assertEquals("/bin/bash: kkkk: command not found", out.stdErr)
        }
    }

    @Test
    fun testExternalErrorOut() {
        val out = External("git kkkk", emptyList(), "", Environment()).run()
        assertEquals("", out.stdOut)
        assertEquals("git: 'kkkk' is not a git command. See 'git --help'.", out.stdErr)
    }

    @Test
    fun testExternalAsEchoTwoArgs() {
        val out = External("echo", listOf("lol", "lol2"), "", Environment()).run()
        assertEquals("lol lol2", out.stdOut)
    }

    @Test
    fun testOtherAsCatFromFile() {
        if ( !Settings.IS_WINDOWS) {
            val filename = "src/test/resources/in2.txt"
            val out = External("cat", listOf(filename), "", Environment()).run()
            File(filename).readText()
            assertEquals(File(filename).readText(), out.stdOut)
        }
    }

    @Test
    fun testOtherAsCatFromPipe() {
        if ( !Settings.IS_WINDOWS) {
            val out = External("cat", emptyList(), "test from pipe", Environment()).run()
            assertEquals("test from pipe", out.stdOut)
        }
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
        assertEquals("", out.stdOut)
        assertEquals("-bash: cd: no such file or directory: not_existed_dir", out.stdErr)
    }

    @Test
    fun testCdGoHome() {
        val env = Environment()
        val dirname = "~"
        val out = Cd(listOf(dirname), env).run()
        assertEquals(System.getProperty("user.home"), env.getDirectory())
        assertEquals("", out.stdOut)
        assertEquals("", out.stdErr)
    }

    @Test
    fun testCdGoRoot() {
        val env = Environment()
        val dirname = "/"
        val out = Cd(listOf(dirname), env).run()
        assertEquals("/", env.getDirectory())
        assertEquals("", out.stdOut)
        assertEquals("", out.stdErr)
    }

    @Test
    fun testCdNotADirectory() {
        val env = Environment()
        val oldDir = env.getDirectory()
        val dirname = "README.md"
        val out = Cd(listOf(dirname), env).run()
        assertEquals(oldDir, env.getDirectory())
        assertEquals("", out.stdOut)
        assertEquals("-bash: cd: not a directory: README.md", out.stdErr)
    }

    @Test
    fun testLs() {
        val env = Environment()
        Cd(listOf("src/"), env).run()
        val out = Ls(listOf(), env).run()

        assertTrue(
                out.stdOut.contains("main")
                        && out.stdOut.contains("test")
        )
    }

    @Test
    fun testLsWithArgument() {
        val env = Environment()
        val out = Ls(listOf("src/"), env).run()

        assertTrue(
                out.stdOut.contains("main")
                        && out.stdOut.contains("test")
        )
    }

    @Test
    fun testLsBadArgument() {
        val env = Environment()
        val out = Ls(listOf("bad_directory"), env).run()

        assertEquals("", out.stdOut)
        assertEquals("ls: bad_directory: No such file or directory", out.stdErr)
    }

    @Test
    fun testPwdAfterCd() {
        val env = Environment()
        val sep = File.separator
        val oldDir = env.getDirectory()
        val dirname = "gradle${sep}wrapper"
        val out = Cd(listOf(dirname), env).run()
        assertEquals(oldDir + "${sep}gradle${sep}wrapper", env.getDirectory())
        assertEquals("", out.stdOut)

        val pwdOut = Pwd(env).run().stdOut

        assertEquals(oldDir + "${sep}gradle${sep}wrapper", pwdOut)
    }

    @Test
    fun testWcAfterCd() {
        val env = Environment()
        val sep = File.separator
        val dirname = "gradle${sep}wrapper"
        val filename = "gradle-wrapper.properties"
        Cd(listOf(dirname), env).run()

        val pwdOut = Wc(listOf(filename), "", env).run().stdOut

        assertEquals("\t\t6\t\t37\t\t201 gradle-wrapper.properties", pwdOut)
    }

    @Test
    fun testCatAfterCd() {
        val env = Environment()
        val sep = File.separator
        val dirname = "gradle${sep}wrapper"
        val filename = "gradle-wrapper.properties"
        Cd(listOf(dirname), env).run()

        val correctAns = File("$dirname/$filename").readText()

        val pwdOut = Cat(listOf(filename), "", env).run().stdOut

        assertEquals(correctAns, pwdOut)
    }

    @Test
    fun testGrepAfterCd() {
        val env = Environment()
        val sep = File.separator
        val dirname = "gradle${sep}wrapper"
        val filename = "gradle-wrapper.properties"
        Cd(listOf(dirname), env).run()

        val correctAns = "distributionUrl=https\\://services.gradle.org/distributions/gradle-6.6-bin.zip" +
                System.lineSeparator()

        val pwdOut = Grep.buildArgs(listOf("Url", filename), "", env).run().stdOut

        assertEquals(correctAns, pwdOut)
    }

    @Test
    fun testCdWithPoints() {
        val env = Environment()
        val sep = File.separator
        val dirname = "src${sep}..${sep}src${sep}..${sep}gradle${sep}.."
        val out = Cd(listOf(dirname), env).run()

        assertEquals("", out.stdErr)
        assertEquals(File("").absolutePath, env.getDirectory())
    }


}

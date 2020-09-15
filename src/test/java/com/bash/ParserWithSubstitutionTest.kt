package com.bash

import com.bash.commands.Echo
import com.bash.commands.Exit
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import com.bash.parser.Parser
import com.bash.util.Substitutor

class ParserWithSubstitutionTest {
    private lateinit var parser: Parser

    @Before
    fun update() {
        parser = Parser(Substitutor())
    }

    @Test
    fun testEcho() {
        parser.addToEnv("a", "111")
        val input = "echo \$a"
        val res = parser.parse(input, "")
        TestCase.assertEquals(Echo(listOf("111")), res)
    }

    @Test
    fun testEchoTwoSubstitutions() {
        parser.addToEnv("a", "123")
        parser.addToEnv("b", "456")
        val input = "echo \$a\$b"
        val res = parser.parse(input, "")
        TestCase.assertEquals(Echo(listOf("123456")), res)
    }

    @Test
    fun testTwoWords() {
        parser.addToEnv("a", "123")
        parser.addToEnv("b", "456")
        val input = "echo \$a \$b"
        val res = parser.parse(input, "")
        TestCase.assertEquals(Echo(listOf("123", "456")), res)
    }

    @Test
    fun testTwoWords2() {
        parser.addToEnv("a", "123321")
        parser.addToEnv("b", "45678910")
        val input = "echo \$a \$b"
        val res = parser.parse(input, "")
        TestCase.assertEquals(Echo(listOf("123321", "45678910")), res)
    }

    @Test
    fun testSingleQuotes() {
        parser.addToEnv("a", "12")
        parser.addToEnv("b", "34")
        val input = "echo '\$a \$b'"
        val res = parser.parse(input, "")
        TestCase.assertEquals(Echo(listOf("\$a \$b")), res)
    }

    @Test
    fun testDoubleQuotes() {
        parser.addToEnv("a", "12")
        parser.addToEnv("b", "34")
        val input = "echo \"\$a \$b\""
        val res = parser.parse(input, "")
        TestCase.assertEquals(Echo(listOf("12 34")), res)
    }

    @Test
    fun testExitFromEnv() {
        parser.addToEnv("a", "ex")
        parser.addToEnv("b", "it")
        val input = "\$a\$b"
        val res = parser.parse(input, "")
        TestCase.assertEquals(Exit(emptyList(), ""), res)
    }
}

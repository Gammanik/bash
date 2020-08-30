package com.bash

import com.bash.commands.Echo
import com.bash.commands.Grep
import junit.framework.TestCase.assertEquals
import org.junit.Test
import com.bash.parser.Parser
import com.bash.util.Substitutor

class ParserTest {
    private val parser = Parser(Substitutor())

    @Test
    fun testEcho() {
        val input = "echo one"
        val res = parser.parse(input, "")
        assertEquals(Echo(listOf("one")), res)
    }

    @Test
    fun testEchoTwoWords() {
        val input = "echo one two"
        val res = parser.parse(input, "")
        assertEquals(Echo(listOf("one", "two")), res)
    }

    @Test
    fun testEchoTwoWordsSpaced() {
        val input = "echo  one   two  "
        val res = parser.parse(input, "")
        assertEquals(Echo(listOf("one", "two")), res)
    }

    @Test
    fun testSingleQuote() {
        val input = "echo 'one two'"
        val res = parser.parse(input, "")
        assertEquals(Echo(listOf("one two")), res)
    }

    @Test
    fun testSingleQuoteWithWords() {
        val input = "echo zero 'one two'  three"
        val res = parser.parse(input, "")
        assertEquals(Echo(listOf("zero", "one two", "three")), res)
    }

    @Test
    fun testDoubleQuote() {
        val input = "echo \"one two\""
        val res = parser.parse(input, "")
        assertEquals(Echo(listOf("one two")), res)
    }

    @Test
    fun testDoubleQuoteWords() {
        val input = "echo \"one two\" three"
        val res = parser.parse(input, "")
        assertEquals(Echo(listOf("one two", "three")), res)
    }

    @Test
    fun testGrepAllFlags() {
        val input = "grep -A 2 -i -w"
        val res = parser.parse(input, "")

        if (res is Grep) {
            assertEquals(2, res.linesToInclude )
            assertEquals(true, res.isWordSearch)
            assertEquals(true, res.caseInsensitivity)
        }
    }

}

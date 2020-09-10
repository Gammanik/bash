package com.bash

import junit.framework.TestCase.assertEquals
import org.junit.Test
import com.bash.util.Substitutor

class SubstitutorTest {
    private val subst = Substitutor()

    @Test
    fun oneWordTest() {
        val substituted = subst.substitute(mapOf(Pair("a", "b")), "\$a")
        assertEquals("b", substituted)
    }

    @Test
    fun twoWordsTest() {
        val env = mapOf("a1" to "a2", "b1" to "b2" )
        val substituted = subst.substitute(env, "\$a1\$b1")
        assertEquals("a2b2", substituted)
    }

    @Test
    fun twoSeparatedWordsTest() {
        val env = mapOf("a1" to "a2", "b1" to "b2")
        val substituted = subst.substitute(env, "\$a1 \$b1")
        assertEquals("a2 b2", substituted)
    }
}

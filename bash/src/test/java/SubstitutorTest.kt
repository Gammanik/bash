import junit.framework.TestCase.assertEquals
import org.junit.Test
import util.Substitutor

class SubstitutorTest {

    @Test
    fun oneWordTest() {
        val substituted = Substitutor.substitute(mapOf(Pair("a", "b")), "\$a")
        assertEquals("b", substituted)
    }

    @Test
    fun twoWordsTest() {
        val env = mapOf("a1" to "a2", "b1" to "b2")
        val substituted = Substitutor.substitute(env, "\$a1\$b1")
        assertEquals("a2b2", substituted)
    }

    @Test
    fun twoSeparatedWordsTest() {
        val env = mapOf("a1" to "a2", "b1" to "b2")
        val substituted = Substitutor.substitute(env, "\$a1 \$b1")
        assertEquals("a2 b2", substituted)
    }
}
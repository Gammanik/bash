import commands.Wc
import junit.framework.TestCase.assertEquals
import org.junit.Test

class CommandTest {

    @Test
    fun testWcFromPipe() {
        val out = Wc(emptyList(), "count me").run()
        assertEquals("\t\t1\t\t2\t\t9", out)
    }

    @Test
    fun testWcFromPipeSpaces() {
        val out = Wc(emptyList(), "count      me").run()
        assertEquals("\t\t1\t\t2\t\t9", out)
    }

    @Test
    fun testWcFromPipeSpaces2() {
        val out = Wc(emptyList(), "count      me  on 1").run()
        assertEquals("\t\t1\t\t4\t\t14", out)
    }

    @Test
    fun testMultilineWcFromPipe() {
        val content = "count me\n" + "and my second line please"
        val out = Wc(emptyList(), content).run()
        assertEquals("\t\t2\t\t7\t\t35", out)
    }

    @Test
    fun testWcFromFile() {
        val filename = "src/test/resources/in1.txt"
        val out = Wc(listOf(filename), "").run()
        assertEquals("\t\t2\t\t4\t\t23 $filename", out)
    }
}
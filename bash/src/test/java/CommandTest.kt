import commands.Echo
import junit.framework.Assert.assertEquals
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream


class CommandTest {

    @Test
    fun testEcho() {
        val st = "echo \"Hello world\""

        val st2 = "hello world"
        val cmd = Echo(st2)
        assertEquals(st2, cmd.run())
    }

    @Test
    fun testWc() {
        val input = "wc in1.txt\n" +
                "exit"
        val output = ByteArrayOutputStream()
        System.setIn(ByteArrayInputStream(input.toByteArray()))
        System.setOut(PrintStream(output))

        val sh = Bash()
        sh.start()
        assertEquals("wcc in1.txt\n", output)
    }

    @Test
    fun testPiped() { }
}

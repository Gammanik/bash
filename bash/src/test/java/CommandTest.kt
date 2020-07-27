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
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))

        val sh = Bash()
        sh.start()
        assertEquals("wc in1.txt\n", output.toString())
    }

    @Test
    fun testPiped() {
        val input = "echo lol | cat | wc"
        val input2 = "echo \"lol\" | cat | wc"

        val output = ByteArrayOutputStream()
        assertEquals("1       2       8", output)
    }
}

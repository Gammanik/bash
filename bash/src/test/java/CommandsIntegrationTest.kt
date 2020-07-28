import junit.framework.TestCase.assertEquals
import org.junit.Test
import util.Settings
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream


class CommandsIntegrationTest {
    @Test
    fun testEcho() {
        val input = "echo Hello world" // todo: add "echo \"Hello world\"" support
        checkCommand(input, "Hello world")
    }

    @Test
    fun testWc() {
        val input = "wc in1.txt\n"
        checkCommand(input, "")
    }

    @Test
    fun testCat() {
        val input = "cat src/test/resources/in1.txt \n" +
                "exit"
        val out = "sh>" + File("src/test/resources/in1.txt").readText() +
                "\n" +
                "sh>"
        checkCommand(input, out)
    }

    @Test
    fun testPiped() {
        val input = "echo 1 2 3 | cat\n"
        checkCommand(input, "")

        val input2 = "echo \"lol\" | cat | wc"
        val output = ByteArrayOutputStream()
        assertEquals("1       2       8", output)
    }

//    private fun checkCommand(input: String, expectedOut: String) {
//        val command = "$input\n exit" // add exit command
//        val expectedBashOut = "${Settings.PREFIX}$expectedOut\n" + Settings.PREFIX
//
//
//        System.setIn(ByteArrayInputStream(command.toByteArray()))
//        val output = ByteArrayOutputStream()
//        System.setOut(PrintStream(output))
//
//        val sh = Bash()
//        sh.start()
//
//        assertEquals(expectedBashOut, output)
//    }

    private fun checkCommand(input: String, expectedOut: String) {
        System.setIn(ByteArrayInputStream(input.toByteArray()))
        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))

        Bash().start()
        assertEquals(expectedOut, output)
    }
}

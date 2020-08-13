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
        val input = "echo hello world"
        checkCommand(input, "hello world")
    }

    @Test
    fun testWc() {
        val input = "wc src/test/resources/in1.txt"
        checkCommand(input, "\t\t2\t\t4\t\t23 src/test/resources/in1.txt")
    }

    @Test
    fun testWcWithEnv() {
        val input = "FILE=src/test/resources/in1.txt\n" +
                "wc \$FILE"
        checkCommand(input, "${Settings.PREFIX}\t\t2\t\t4\t\t23 src/test/resources/in1.txt")
    }

    @Test
    fun testCat() {
        val input = "cat src/test/resources/in1.txt"
        val out = File("src/test/resources/in1.txt").readText()
        checkCommand(input, out)
    }

    @Test
    fun testPiped() {
        val input = "echo 1 2 3 | cat"
        checkCommand(input, "1 2 3")
    }

    @Test
    fun testPipedDoubleQuotes() {
        val input = "echo \"lol\" | cat | wc"
        checkCommand(input, "\t\t1\t\t1\t\t4")
    }

    @Test
    fun testPipedDoubleQuotesWords() {
        val input = "echo \"lol 1 2 3\" | cat | wc"
        checkCommand(input, "\t\t1\t\t4\t\t10")
    }

    @Test
    fun testEnvSubstitution() {
        val input = "a=one\n" +
                    "b=two\n" +
                    "echo \$a\$b"
        checkCommand(input,"${Settings.PREFIX}${Settings.PREFIX}onetwo")
    }

    @Test
    fun testExitFromEnv() {
        val input = "a=ex\n" +
                    "b=it\n" +
                    "\$a\$b\n" +
                    "echo should not print"

        val expectedBashOut = "${Settings.PREFIX}${Settings.PREFIX}${Settings.PREFIX}"
        System.setIn(ByteArrayInputStream(input.toByteArray()))
        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))

        val sh = Bash()
        sh.start()
        assertEquals(expectedBashOut, output.toString())
    }

    @Test
    fun testGrepFromPipe() {
        val input = "cat src/test/resources/in3.txt | grep pattern"
        val expectedOut = File("src/test/resources/results/in3grep.txt").readText()
        checkCommand(input, expectedOut)
    }

    private fun checkCommand(input: String, expectedOut: String) {
        val command = "$input\n exit" // add exit command
        val expectedBashOut =  Settings.PREFIX + "$expectedOut\n" + Settings.PREFIX

        System.setIn(ByteArrayInputStream(command.toByteArray()))
        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))

        val sh = Bash()
        sh.start()

        assertEquals(expectedBashOut, ignoreColorChange(output.toString()))
    }

    private fun ignoreColorChange(line: String): String {
        return line.replace(Settings.OUTPUT_COLOR, "", false)
                .replace(Settings.ANSI_RESET, "", false)
    }
}

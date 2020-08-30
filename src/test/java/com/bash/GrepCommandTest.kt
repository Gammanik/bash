import com.bash.commands.Grep
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.io.File

class GrepCommandTest {
    @Test
    fun testGrep() {
        val args = listOf("pattern", "src/test/resources/in3.txt")
        val out = Grep.buildArgs(args, "").run().sdtOut
        val expected = File("src/test/resources/results/in3grep.txt").readText()
        assertEquals(expected, out)
    }

    @Test
    fun testGrepWithArgs() {
        val args = listOf("pattern", "src/test/resources/in3.txt", "-A", "1", "-i")
        val cmd = Grep.buildArgs(args, "")
        val out = cmd.run().sdtOut

        assertEquals(cmd.linesToInclude, 1)
        assertEquals(cmd.caseInsensitivity, true)
        assertEquals(cmd.isWordSearch, false)

        val expected = File("src/test/resources/results/in3grepA1_i.txt").readText()
        assertEquals(expected, out)
    }

    @Test
    fun testGrepA2() {
        val args = listOf("pattern", "src/test/resources/in3.txt", "-A", "2")
        val out = Grep.buildArgs(args, "").run().sdtOut

        val expected = File("src/test/resources/results/in3grepA2.txt").readText()
        assertEquals(expected, out)
    }

    @Test
    fun testGrepWI() {
        val args = listOf("pattern", "src/test/resources/in3.txt", "-i", "-w")
        val out = Grep.buildArgs(args, "").run().sdtOut

        val expected = File("src/test/resources/results/in3grep_w_i.txt").readText()
        assertEquals(expected, out)
    }

    @Test
    fun testFromPipe() {
        val pipeInput = "ab\nacb\naa\nacb\naab"
        val out = Grep.buildArgs(listOf("ab"), pipeInput).run().sdtOut

        val expected = "ab\naab\n"
        assertEquals(expected, out)
    }

    @Test
    fun testNonExistentFile() {
        val filename = "nonExistentFilename.txt"
        val err = Grep.buildArgs(listOf("no", filename), "").run().stdErr
        assertEquals(err, "grep: $filename: No such file or directory")
    }

}

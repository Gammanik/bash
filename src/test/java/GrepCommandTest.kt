import commands.Grep
import junit.framework.TestCase
import org.junit.Test

class GrepCommandTest {
    @Test
    fun testGrepBasic() {
        val args = listOf("plugin", "build.gradle")
        val out = Grep.buildArgs(args, "").run()
        // todo: check output
    }

    @Test
    fun testGrep() {
        val args = listOf("pattern", "src/test/resources/in3.txt")
        val out = Grep.buildArgs(args, "").run()

    }

    @Test
    fun testGrepWithArgs() {
        val args = listOf("pattern", "src/test/resources/in3.txt", "-A", "2", "-i")
        val cmd = Grep.buildArgs(args, "")
        val out = cmd.run()

        TestCase.assertEquals(cmd.lines, 2)
        TestCase.assertEquals(cmd.caseInsensitivity, true)
        TestCase.assertEquals(cmd.isWordSearch, false)
        // todo: check output
    }
}
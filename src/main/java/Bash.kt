import commands.Exit
import parser.Parser
import util.Settings
import util.Settings.ANSI_RESET
import util.Settings.OUTPUT_COLOR
import util.Substitutor

class Bash {

    fun start() {
        val parser = Parser(Substitutor())

        while (true) {
            print(Settings.PREFIX)
            val line = readLine() ?: continue
            if ((line == "") or runIsAssignment(line, parser)) { continue }

            val commands = line.split("|")
            var lastRes = ""

            for (cmd in commands) {
                val command = parser.parse(cmd.trim(), lastRes)
                lastRes = parser.parse(cmd.trim(), lastRes).run()

                if (command is Exit && commands.size == 1)
                    return
            }

            println(OUTPUT_COLOR + lastRes + ANSI_RESET)
        }
    }

    private fun runIsAssignment(line: String, parser: Parser): Boolean {
        if (line.split("|").size > 1) {
            return false
        }

        val arr = line.split("=")
        if (arr.size == 2) {
            val left = arr.first()
            val right = arr.last()
            parser.addToEnv(left.trimStart(), right.trimEnd())
            return true
        }

        return false
    }
}

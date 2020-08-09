import parser.Parser
import util.Settings
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
                lastRes = parser.parse(cmd.trim(), lastRes).run()
            }

//            println(ANSI_GREEN + lastRes + ANSI_RESET) // todo: make Printer (with toString sh>)
            println(lastRes)
        }
    }

    private fun runIsAssignment(line: String, parser: Parser): Boolean {
        if (line.split("|").isNotEmpty()) {
            return false
        }

        val arr = line.split("=")
        if (arr.size == 2) {
            val left = arr.first()
            val right = arr.last()
            parser.addToEnv(left, right)
            return true
        }

        return false
    }
}

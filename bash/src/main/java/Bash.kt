import parser.Parser
import util.Settings

class Bash {
    private val env = emptyMap<String, String>()

    fun start() {
        val parser = Parser()

        while (true) {
            print(Settings.PREFIX)
            val line = readLine() ?: continue
            if (line.trim().equals("exit")) { break }
            if (line.trim().equals("")) { continue }

            val commands = line.split(" | ")
            var lastRes = ""

            for (cmd in commands) {
                lastRes = parser.parse(cmd, lastRes).run()
            }
//            println(ANSI_GREEN + lastRes + ANSI_RESET) // todo: make Printer (with toString sh>)
            println(lastRes)
        }
    }
}

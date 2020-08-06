import parser.Parser
import util.Settings
import util.Substitutor

class Bash {
    private val env = mutableMapOf<String, String>()

    fun start() {
        val parser = Parser()

        while (true) {
            print(Settings.PREFIX)
            val line = Substitutor.substitute(env, readLine()) ?: continue
            if (line == "exit") { break }
            if ((line == "") or runIsAssignment(line)) { continue }

            val commands = line.split(" | ")
            var lastRes = ""

            for (cmd in commands) {
                lastRes = parser.parse(cmd, lastRes).run()
            }

//            println(ANSI_GREEN + lastRes + ANSI_RESET) // todo: make Printer (with toString sh>)
            println(lastRes)
        }
    }

    private fun runIsAssignment(line: String): Boolean {
        if (line.split(" | ").isNotEmpty()) {
            return false
        }

        val arr = line.split("=")
        if (arr.size == 2) {
            val left = arr.first()
            val right = arr.last()
//            if (left) // todo: check if left & right is word

            env[left] = right
            return true
        }

        return false
    }

    private fun isAssignableVariable(v: String): Boolean {
//        if (v.startsWith(""))
        return true
    }
}

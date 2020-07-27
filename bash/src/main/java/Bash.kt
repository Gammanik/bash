import parser.Parser

class Bash {
    private val env = emptyMap<String, String>()

    fun start() {
        val parser = Parser()

        while (true) {
            print("sh>")
            val line = readLine() ?: continue
            if (line.trim().equals("exit")) { break }

            val commands = line.split(" | ")
            var lastRes = ""

            for (cmd in commands) {
                lastRes = parser.parse(cmd, lastRes).run()
            }
            println(lastRes)
        }
    }
}

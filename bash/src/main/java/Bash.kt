class Bash {
    private val env = emptyMap<String, String>()


    fun start() {
        while (true) {
            val line = readLine()

            if (line?.trim().equals("exit")) {
                break
            }

            println("$line")

        }
    }
}

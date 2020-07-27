package commands

class Cat(private val args: List<String>, private val lastRes: String): Command() {
    override fun run(): String {
        if (args.isEmpty() && !lastRes.isEmpty())
            return lastRes

        val filename = args.first()
        // todo: open file and out
        return ""
    }
}

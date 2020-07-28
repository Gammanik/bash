package commands

class Wc(private val args: List<String>, private val lastRes: String = ""): Command() {

    override fun run(): String {
        var content = ""

        if (args.isEmpty() && lastRes.isNotEmpty()) {
            content = lastRes
        } else {
            val filename = args.first()
        }

        return ""
    }
}

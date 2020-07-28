package commands

class Echo(private val args: List<String>): Command() {

    override fun run(): String {
        return args.joinToString(separator = " ")
    }
}

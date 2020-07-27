package commands

class Echo(private val args: List<String>): Command() {

    override fun run(): String {
//        println("jts: ${args.joinToString { " " }}")
        return args.toString()
    }
}

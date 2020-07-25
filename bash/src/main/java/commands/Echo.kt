package commands

class Echo(private val args: String) {

    fun run(): String {
        return args
    }
}

package commands

/** prints the current working directory **/
class Pwd: Command() {
    override fun run(): String {
        return System.getProperty("user.dir")
    }
}

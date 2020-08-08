package commands

class Pwd: Command() {
    override fun run(): String {
        return System.getProperty("user.dir")
    }
}

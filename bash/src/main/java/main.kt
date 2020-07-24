import com.beust.jcommander.JCommander


fun main() {
    println("hi")

    val args = Args()
    val argv = arrayOf("-bf", "2", "targets", "someTarget")
    JCommander.newBuilder()
            .addObject(args)
            .build()
            .parse(*argv)

    println("${args.buildFile} , ${args.targets}")
}
package commands

import java.io.File

class Wc(private val args: List<String>, private val lastRes: String = ""): Command() {

    override fun run(): String {
        var content = ""
        val isFromPipe = args.isEmpty() && lastRes.isNotEmpty()
        var filename = ""

        content = if (isFromPipe) {
            lastRes
        } else {
            filename = args.first()
            File(filename).readText()
        }

//        val words = content.split(" ").size
//        val lines = content.split("\n").size
//        val bytes = content.byt // todo: find how to count byte size

        var lines = 0 ; var words = 0; var bytes = 0

        content.lines().forEach { line ->
            line.split(" ").forEach {
                if (it.isNotEmpty()) {
                    words += 1
                    bytes += it.length + 1// todo: count sz?
                }
            }

            if (line.isNotEmpty()) lines += 1
        }

        return if (isFromPipe)
            "\t\t$lines\t\t$words\t\t$bytes"
        else
            "\t\t$lines\t\t$words\t\t$bytes $filename"
    }
}

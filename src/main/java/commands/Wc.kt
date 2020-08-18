package commands

import util.CmdRes
import java.io.File

class Wc(private val args: List<String>, private val lastRes: String = ""): Command() {

    override fun run(): CmdRes {
        // todo: add multiple args

        if (args.isEmpty() && lastRes.isBlank())
            return CmdRes("", "wc: not enough arguments")

        var content = ""
        val isFromPipe = args.isEmpty() && lastRes.isNotEmpty()
        var filename = ""

        content = if (isFromPipe) {
            lastRes
        } else {
            filename = args.first()
            File(filename).readText()
        }

        var lines = 0 ; var words = 0; var bytes = 0

        content.lines().forEach { line ->
            line.split(" ").forEach {
                if (it.isNotEmpty()) {
                    words += 1
                    bytes += it.length + 1
                }
            }

            if (line.isNotEmpty()) lines += 1
        }

        return if (isFromPipe)
            CmdRes("\t\t$lines\t\t$words\t\t$bytes", "")
        else
            CmdRes("\t\t$lines\t\t$words\t\t$bytes $filename", "")
    }
}

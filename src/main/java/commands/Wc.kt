package commands

import util.CmdRes
import java.io.File

class Wc(private val args: List<String>, private val lastRes: String = ""): Command() {

    override fun run(): CmdRes {
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

        val lines = content.split("\\n|\\r|\\n\\r".toRegex()).size
        val words = content.split("\\b".toRegex()).filter { it.isNotBlank() }.size
        val bytes = content.length + 1

        return if (isFromPipe)
            CmdRes("\t\t$lines\t\t$words\t\t$bytes", "")
        else
            CmdRes("\t\t$lines\t\t$words\t\t$bytes $filename", "")
    }

//    private fun
}

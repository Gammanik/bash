package com.bash.commands

import com.bash.util.CmdRes
import main.java.com.bash.util.Environment
import java.io.File

/**  wc utility displays the number of lines, words, and bytes contained in each input file, or standard
 * input (if no file is specified)
 * format: 		lines		words		bytes       filename?**/
class Wc(private val args: List<String>, private val lastRes: String = "",
         private val environment: Environment): Command() {

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
            File(environment.getDirectory() + "/" + filename).readText()
        }

        val lines = content.split("\\n|\\r|\\n\\r".toRegex()).size
        val words = content.split("\\b".toRegex()).filter { it.isNotBlank() }.size
        val bytes = content.length + 1

        return if (isFromPipe)
            CmdRes("\t\t$lines\t\t$words\t\t$bytes", "")
        else
            CmdRes("\t\t$lines\t\t$words\t\t$bytes $filename", "")
    }
}

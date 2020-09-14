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
        val filename = if (args.isEmpty()) "" else args.first()

        println(environment.getDirectory() + File.separatorChar + filename)

        if (isFromPipe) {
            content = lastRes
        } else if (File(filename).exists()) {
            content =  File(filename).readText()
        } else if (File(environment.getDirectory() + File.separatorChar + filename).exists()) {
            content =  File(environment.getDirectory() + File.separatorChar + filename).readText()
        } else {
            return CmdRes("", "wc: ${args.first()}: open: No such file or directory")
        }

        val lines = content.split(System.lineSeparator()).size
        val words = content.split("\\b".toRegex()).filter { it.isNotBlank() }.size
        val bytes = content.length + 1

        return if (isFromPipe)
            CmdRes("\t\t$lines\t\t$words\t\t$bytes", "")
        else
            CmdRes("\t\t$lines\t\t$words\t\t$bytes $filename", "")
    }
}

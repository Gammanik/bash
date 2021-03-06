package com.bash.commands

import com.bash.util.CmdRes
import java.io.File
import java.lang.Exception

/**
 * if not piped then first argument should be a filename
 * prints the text
 */
class Cat(private val args: List<String>, private val lastRes: String): Command() {
    override fun run(): CmdRes {
        if (args.isEmpty() && lastRes.isNotEmpty())
            return CmdRes(lastRes, "")

        val filename = args.first()

        val res: String
        try {
            res = File(filename).readText()
        } catch (e: Exception) {
            return CmdRes("", e.toString())
        }

        return CmdRes(res, "")
    }
}

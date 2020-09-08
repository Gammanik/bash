package main.java.com.bash.commands

import com.bash.commands.Command
import com.bash.util.CmdRes
import main.java.com.bash.util.Environment
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*


class Ls(private val args: List<String>, private val env: Environment) : Command() {
    override fun run(): CmdRes {
        if (args.size > 1) {
            val errorMsg = "-bash: ls: too many arguments"
            return CmdRes("", errorMsg)
        } else if (args.isEmpty()) {
            val dir = env.getDirectory()
            val dirFile = File(dir)

            val res = StringJoiner(System.lineSeparator())

            dirFile.list()!!.forEach {
                x -> res.add(x)
            }

            return CmdRes(res.toString(), "")
        } else {
            val dir = args[0]
            val dirFile = File(dir)

            if ( !dirFile.exists()) {
                val errorMsg = "ls: " + dirFile.name + ": No such file or directory"
                return CmdRes("", errorMsg)
            }
            if ( !dirFile.isDirectory) {
                return CmdRes(dirFile.name, "")
            }

            val res = StringJoiner(System.lineSeparator())

            dirFile.list()!!.forEach {
                x -> res.add(x)
            }

            return CmdRes(res.toString(), "")
        }
    }
}
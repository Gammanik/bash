package main.java.com.bash.commands

import com.bash.commands.Command
import com.bash.util.CmdRes
import main.java.com.bash.util.Environment
import java.io.File

class Cd(private val args: List<String>, private val env: Environment) : Command() {
    override fun run(): CmdRes {
        if (args.size > 1) {
            val errorMsg = "-bash: cd: too many arguments"
            return CmdRes("", errorMsg)
        } else if (args.isEmpty() || args[0] == "~") {
            env.changeDirectory(System.getProperty("user.home"))
            return CmdRes("", "")
        } else {
            val path = args[0]
            val file = File(path)
            if ( !file.exists()) {
                val errorMsg = String.format("-bash: cd: no such file or directory: %s", path)
                return CmdRes("", errorMsg)
            }
            if ( !file.isDirectory) {
                val errorMsg = String.format("-bash: cd: not a directory: %s", path)
                return CmdRes("", errorMsg)
            }
            env.changeDirectory(file.absolutePath)
            return CmdRes("", "")
        }
    }
}
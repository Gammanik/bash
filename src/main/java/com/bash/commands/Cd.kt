package main.java.com.bash.commands

import com.bash.commands.Command
import com.bash.util.CmdRes
import com.bash.util.Settings
import main.java.com.bash.util.Environment
import java.io.File
import java.lang.Exception
import java.util.*

/** class for changing directory command **/
class Cd(private val args: List<String>, private val env: Environment) : Command() {
    override fun run(): CmdRes {
        if (args.size > 1) {
            val errorMsg = "-bash: cd: too many arguments"
            return CmdRes("", errorMsg)
        } else if (args.isEmpty()) {
            env.changeDirectory(System.getProperty("user.home"))
            return CmdRes("", "")
        } else {
            val arg = args[0]
            val file: File

            file = if (arg.isNotEmpty() && arg[0] == '/') {
                val path = StringJoiner(File.separator, File.separator, File.separator)

                absolutePathApplier(arg).forEach { x ->
                    path.add(x)
                }

                File(path.toString())
            } else {
                val pathSequence = cdHelper(env.getDirectory(), arg)
                val path = StringJoiner(File.separator, File.separator, File.separator)

                pathSequence.forEach { x ->
                    path.add(x)
                }

                File(path.toString())
            }

            if ( !file.exists()) {
                val errorMsg = "-bash: cd: no such file or directory: $arg"
                return CmdRes("", errorMsg)
            }

            if ( !file.isDirectory) {
                val errorMsg = "-bash: cd: not a directory: $arg"
                return CmdRes("", errorMsg)
            }

            env.changeDirectory(file.absolutePath)
            return CmdRes("", "")
        }
    }

    private fun cdParser(string: String): MutableList<String> {
        return string.split(File.separatorChar)
                .filter { x -> x != "" }
                .toMutableList()
    }

    private fun cdHelper(path: String, needToGo: String): MutableList<String> {
        val res = mutableListOf<String>()
        val pathList = cdParser(path)
        val needToGoList = cdParser(needToGo)

        if (needToGoList.size >= 1 && needToGoList[0] == "~") {
            res.addAll(cdParser(System.getProperty("user.home")))
            needToGoList.removeAt(0)
            res.addAll(needToGoList)
            return res
        }

        res.addAll(pathList)

        return applyWayToPath(res, needToGoList)
    }

    private fun absolutePathApplier(needToGo: String): MutableList<String> {
        val res = mutableListOf<String>()
        val needToGoList = cdParser(needToGo)

        return applyWayToPath(res, needToGoList)
    }

    private fun applyWayToPath(startPath: MutableList<String>, needToGoList: MutableList<String>):
            MutableList<String> {
        val minElements = if (Settings.IS_WINDOWS) 1 else 0
        for (go in needToGoList) {
            if (go == "..") {
                if (startPath.size > minElements) startPath.removeLast()
            } else if (go != "..") {
                startPath.add(go)
            } else {
                throw Exception()
            }
        }

        return startPath
    }
}
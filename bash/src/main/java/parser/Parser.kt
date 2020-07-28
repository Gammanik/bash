package parser

import commands.Cat
import commands.Command
import commands.Echo
import commands.Wc

class Parser {
    fun parse(cmd: String, lastRes: String): Command {
        val cmdWithArgs = cmd.split(" ")
        val commandName = cmdWithArgs.first()
        val args = cmdWithArgs.subList(1, cmdWithArgs.size)

        return when (commandName) {
            "echo"  -> Echo(args)
            "cat"   -> Cat(args, lastRes)
            "wc"    -> Wc(args, lastRes)
            else -> Echo(emptyList())
        }
    }
}

package parser

import commands.Cat
import commands.Command
import commands.Echo

class Parser {
    fun parse(cmd: String, lastRes: String): Command {
        val cmdWithArgs = cmd.split(" ")
        val commandName = cmdWithArgs.first()
        val args = cmdWithArgs.subList(1, cmdWithArgs.size)

        if (commandName.equals("echo")) {
            return Echo(args)
        }

        if (commandName.equals("cat")) {
            return Cat(args, lastRes)
        }

        return Echo(emptyList())
    }
}

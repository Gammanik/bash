package parser

import commands.Cat
import commands.Command
import commands.Echo
import commands.Wc

class Parser {
    fun addToEnv(r: String, l: String) { }

    fun parse(cmd: String, lastRes: String): Command {
        val (commandName, args) = getCommandWithArgs(cmd.trim())

        return when (commandName) {
            "echo"  -> Echo(args)
            "cat"   -> Cat(args, lastRes)
            "wc"    -> Wc(args, lastRes)
            else -> Echo(emptyList()) // todo: call real sh
        }
    }

    private fun getCommandWithArgs(cmd: String): Pair<String, List<String>> {
        val command = StringBuilder()
        var i = 0 // todo: make shared var??
        while (i < cmd.length && cmd[i] != ' ') {
            command.append(cmd[i])
            i += 1
        }

        val args = mutableListOf<String>()
        while (i < cmd.length) {
            while (i < cmd.length && cmd[i] == ' ')
                i += 1

            when (cmd[i]) {
                '\'' ->  {
                    i += 1 // skip the ' char
                    val arg = parseSingleQuote(i, cmd)
                    args.add(arg)
                    i += arg.length
                    i += 1 // skip the ' char
                }
                '"'  -> {
                    i += 1 // skip the " char
                    val arg = parseDoubleQuote(i, cmd)
                    args.add(arg)
                    i += arg.length
                    i += 1 // skip the " char
                }
                else -> {
                    val arg = parseWord(i, cmd)
                    args.add(parseWord(i, cmd))
                    i += arg.length
                }
            }
        }


        return Pair(command.toString(), args)
    }

    private fun parseSingleQuote(i: Int, cmd: String): String {
        val res = StringBuilder()

        var j = i
        while (j < cmd.length && cmd[j] != '\''){
            res.append(cmd[j])
            j += 1
        }

        return res.toString()
    }

    private fun parseDoubleQuote(i: Int, cmd: String): String {
        val res = StringBuilder()

        var j = i
        while (j < cmd.length && cmd[j] != '"'){
            res.append(cmd[j])
            j += 1
        }

        return substitute(res.toString())
    }

    private fun parseWord(i: Int, cmd: String): String {
        val res = StringBuilder()
        var j = i
        while (j < cmd.length && cmd[j] != '\'' && cmd[j] != '"' && cmd[j] != ' '){
            res.append(cmd[j])
            j += 1
        }

        return substitute(res.toString())
    }

    private fun substitute(arg: String): String {
        return arg
    }
}

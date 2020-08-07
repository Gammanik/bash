package parser

import commands.*
import util.Substitutor

class Parser(private val substitutor: Substitutor) {
    private val env = mutableMapOf<String, String>()

    fun addToEnv(r: String, l: String) {
        env[r] = l
    }

    fun parse(cmd: String, lastRes: String): Command {
        val (commandName, args) = getCommandWithArgs(cmd.trim())

        return when (commandName) {
            "echo"  -> Echo(args)
            "cat"   -> Cat(args, lastRes)
            "wc"    -> Wc(args, lastRes)
            "exit"  -> Exit(lastRes)
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
                    i += arg.length + 1 // skip the ' char also
                }
                '"'  -> {
                    i += 1 // skip the " char
                    val arg = parseDoubleQuote(i, cmd)
                    args.add(arg)
                    i += arg.length + 1 // skip the " char also
                }
                else -> {
                    val arg = parseWord(i, cmd)
                    args.add(parseWord(i, cmd))
                    i += arg.length
                }
            }
        }

        val commandString = substitutor.substitute(env, command.toString())
        return Pair(commandString, args)
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

        return substitutor.substitute(env, res.toString())
    }

    private fun parseWord(i: Int, cmd: String): String {
        val res = StringBuilder()
        var j = i
        while (j < cmd.length && cmd[j] != '\'' && cmd[j] != '"' && cmd[j] != ' '){
            res.append(cmd[j])
            j += 1
        }

        return substitutor.substitute(env, res.toString())
    }

}

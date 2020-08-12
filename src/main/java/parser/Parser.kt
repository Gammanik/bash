package parser

import com.beust.jcommander.JCommander
import commands.*
import util.Substitutor


/** parse command and it's args
 * make substitutions using {@link util.Substitutor.class}
 * **/
class Parser(private val substitutor: Substitutor) {
    private val env = mutableMapOf<String, String>()

    fun addToEnv(r: String, l: String) {
        env[r] = l
    }

    fun parse(cmd: String, lastRes: String): Command {
        val (commandName, args) = getCommandWithArgs(cmd.trim())
        println("args $args")

        val argv = arrayOf("-A", "2", "-i", "-w")

        return when (commandName) {
            "echo"  -> Echo(args)
            "cat"   -> Cat(args, lastRes)
            "wc"    -> Wc(args, lastRes)
            "pwd"   -> Pwd()
            "grep"  -> { Grep.buildArgs(args, lastRes) }
            "exit"  -> Exit(lastRes)
            else -> External(commandName, args, lastRes)
        }
    }

    private fun getCommandWithArgs(cmd: String): Pair<String, List<String>> {
        val command = StringBuilder()
        var i = 0 // make as a shared var?
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

    // to parse arguments inside the double quotes: ""
    private fun parseSingleQuote(i: Int, cmd: String): String {
        val res = StringBuilder()

        var j = i
        while (j < cmd.length && cmd[j] != '\''){
            res.append(cmd[j])
            j += 1
        }

        return res.toString()
    }

    // to parse arguments inside the single quotes: ''
    // does not make substitution
    private fun parseDoubleQuote(i: Int, cmd: String): String {
        val res = StringBuilder()

        var j = i
        while (j < cmd.length && cmd[j] != '"'){
            res.append(cmd[j])
            j += 1
        }

        return substitutor.substitute(env, res.toString())
    }

    // parsing a word and makes substitution
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

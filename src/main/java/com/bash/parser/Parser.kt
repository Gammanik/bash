package com.bash.parser

import com.bash.commands.*
import com.bash.util.Substitutor
import main.java.com.bash.commands.Cd
import main.java.com.bash.commands.Ls
import main.java.com.bash.util.Environment


/** parse command and it's args
 * make substitutions using [@link com.bash.util.Substitutor]
 * **/
class Parser(private val substitutor: Substitutor, private val environment: Environment) {
    private val env = mutableMapOf<String, String>()

    /** add variable to the environment **/
    fun addToEnv(r: String, l: String) {
        env[r] = l
    }

    /** parse given command
     * returns [com.bash.commands.Command] subclass **/
    fun parse(cmd: String, lastRes: String): Command {
        val (commandName, args) = getCommandWithArgs(cmd.trim())

        return when (commandName) {
            "echo"  -> Echo(args)
            "cat"   -> Cat(args, lastRes, environment)
            "wc"    -> Wc(args, lastRes, environment)
            "pwd"   -> Pwd(environment)
            "grep"  -> Grep.buildArgs(args, lastRes, environment)
            "exit"  -> Exit(args, lastRes)
            "cd"    -> Cd(args, environment)
            "ls"    -> Ls(args, environment)
            else -> External(commandName, args, lastRes, environment)
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
                    val arg = parseQuote(i, cmd, '\'')
                    args.add(arg)
                    i += arg.length + 1 // skip the ' char also
                }
                '"'  -> {
                    i += 1 // skip the " char
                    val arg = parseQuote(i, cmd, '"')
                    args.add(substitutor.substitute(env, arg))
                    i += arg.length + 1 // skip the " char also
                }
                else -> {
                    val arg = parseWord(i, cmd)
                    args.add(substitutor.substitute(env, arg))
                    i += arg.length
                }
            }
        }

        val commandString = substitutor.substitute(env, command.toString())
        return Pair(commandString, args)
    }

    /** parse arguments inside quotes (single and double) */
    private fun parseQuote(i: Int, cmd: String, quote: Char): String {
        val res = StringBuilder()

        var j = i
        while (j < cmd.length && cmd[j] != quote){
            res.append(cmd[j])
            j += 1
        }

        return res.toString()
    }

    /** parsing a word */
    private fun parseWord(i: Int, cmd: String): String {
        val res = StringBuilder()
        var j = i
        while (j < cmd.length && cmd[j] != '\'' && cmd[j] != '"' && cmd[j] != ' '){
            res.append(cmd[j])
            j += 1
        }

        return res.toString()
    }

}

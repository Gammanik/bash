package com.bash

import com.bash.commands.Exit
import com.bash.parser.Parser
import com.bash.util.CmdRes
import com.bash.util.Settings
import com.bash.util.Settings.ANSI_RESET
import com.bash.util.Settings.OUTPUT_COLOR
import com.bash.util.Substitutor

/** main class representing the CLI **/
class Bash {

    /** starts the REPL loop **/
    fun start() {
        val parser = Parser(Substitutor())

        while (true) {
            print(Settings.PREFIX)
            val line = readLine() ?: continue
            if ((line == "") or runIsAssignment(line, parser)) { continue }

            val commands = line.split("|")
            var lastRes = CmdRes("", "")

            for (cmd in commands) {
                val command = parser.parse(cmd.trim(), lastRes.sdtOut)
                lastRes = command.run()

                if (lastRes.stdErr.isNotBlank()) // error output
                    println(lastRes.stdErr)

                if (command is Exit && commands.size == 1)
                    return
            }

            println(OUTPUT_COLOR + lastRes + ANSI_RESET)
        }
    }

    private fun runIsAssignment(line: String, parser: Parser): Boolean {
        if (line.split("|").size > 1) {
            return false
        }

        val arr = line.split("=")
        if (arr.size == 2) {
            val left = arr.first()
            val right = arr.last()
            parser.addToEnv(left.trimStart(), right.trimEnd())
            return true
        }

        return false
    }
}

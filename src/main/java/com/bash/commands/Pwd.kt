package com.bash.commands

import com.bash.util.CmdRes
import main.java.com.bash.util.Environment

/** prints the current working directory **/
class Pwd(private val env: Environment): Command() {
    override fun run(): CmdRes {
        return CmdRes(env.getDirectory(), "")
    }
}

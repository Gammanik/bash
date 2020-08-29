package com.bash.commands

import com.bash.util.CmdRes

abstract class Command {
    abstract fun run(): CmdRes
}

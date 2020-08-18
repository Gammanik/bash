package commands

import util.CmdRes

abstract class Command {
    abstract fun run(): CmdRes
}

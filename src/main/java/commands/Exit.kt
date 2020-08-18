package commands

import util.CmdRes

/**
 * command terminates the bash
 * handled in the outer loop
 */
class Exit(private val args: List<String>, private val lastRes: String): Command() {
    override fun run(): CmdRes {
        if (args.isNotEmpty()) {
            val errorMsg = "-bash: exit: too many arguments"
            return CmdRes("", errorMsg)
        }

        return CmdRes("", "")
    }

    override fun equals(other: Any?): Boolean {
        if (other is Exit) {
            return lastRes == other.lastRes
        }
        return false
    }
}

package com.bash.commands

import com.bash.util.CmdRes

/** write arguments to the standard output **/
class Echo(private val args: List<String>): Command() {

    override fun run(): CmdRes {
        return CmdRes(args.joinToString(separator = " "), "")
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Echo)
            return false

        if (args.size != other.args.size)
            return false

        for (i in args.indices) {
            if (args[i] != other.args[i])
                return false
        }

        return true
    }

    override fun toString(): String {
        return args.toString()
    }
}

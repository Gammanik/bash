package com.bash.util

/** represents the command result
 * sdtOut - command output
 * stdErr - command error **/
data class CmdRes(val stdOut: String, val stdErr: String) {
    override fun toString(): String {
        return stdOut
    }
}

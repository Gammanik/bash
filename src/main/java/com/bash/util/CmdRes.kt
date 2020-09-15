package com.bash.util

/** represents the command result
 * sdtOut - command output
 * stdErr - command error **/
data class CmdRes(val sdtOut: String, val stdErr: String) {
    override fun toString(): String {
        return sdtOut
    }
}

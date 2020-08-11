package commands

import kotlin.system.exitProcess

class Exit(private val lastRes: String): Command() {
    override fun run(): String {
        if (lastRes.isEmpty()) {
//            exitProcess(0)
            return ""
        } else {
            return ""
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other is Exit) {
            return lastRes == other.lastRes
        }
        return false
    }
}
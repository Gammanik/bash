package commands

/**
 * command terminates the bash
 * handled in the outer loop
 */
class Exit(private val lastRes: String): Command() {
    override fun run(): String {
        return ""
    }

    override fun equals(other: Any?): Boolean {
        if (other is Exit) {
            return lastRes == other.lastRes
        }
        return false
    }
}

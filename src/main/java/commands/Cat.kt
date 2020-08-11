package commands

import java.io.File

/**
 * if not piped then first argument should be a filename
 * prints the text
 */
class Cat(private val args: List<String>, private val lastRes: String): Command() {
    override fun run(): String {
        if (args.isEmpty() && lastRes.isNotEmpty())
            return lastRes

        val filename = args.first()
        return File(filename).readText()
    }
}

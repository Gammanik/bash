package commands

import java.io.File
import java.lang.StringBuilder

class Cat(private val args: List<String>, private val lastRes: String): Command() {
    override fun run(): String {
        if (args.isEmpty() && lastRes.isNotEmpty())
            return lastRes

        val filename = args.first()
        val stringBuilder = StringBuilder()
        File(filename).forEachLine { stringBuilder.appendln(it) }
        return stringBuilder.toString()
    }
}

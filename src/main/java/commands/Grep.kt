package commands

import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameter
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.io.StringReader
import java.lang.StringBuilder
import java.util.regex.Pattern

/** searches any given input files, selecting lines that match the pattern
 * create only by calling [@link Grep.buildArgs] in case of parsing flags
 *
 * flags:
 * -A num [linesToInclude]: print num lines of trailing context after each match
 * -w search the full world match
 * -i search case insensitive (case sensitive by default)
 * **/
class Grep
    private constructor(private val lastRes: String)
    : Command() {

    // stores expr to search and filename (optional)
    @Parameter var otherArgs = mutableListOf<String>()
        private set

    @Parameter(names = ["-A"]) var linesToInclude: Int = 0
        private set

    @Parameter(names = ["-w"]) var isWordSearch = false
        private set

    @Parameter(names = ["-i"]) var caseInsensitivity = false
        private set


    companion object {
        fun buildArgs(args: List<String>, lastRes: String): Grep {
            val instance = Grep(lastRes)
            JCommander.newBuilder()
                    .addObject(instance)
                    .build()
                    .parse(*args.toTypedArray())

            return instance
        }
    }

    override fun run(): String {
        val exprToFind = if (isWordSearch) "\\b${otherArgs.first()}\\b" else otherArgs.first()
        val p = if (caseInsensitivity) Pattern.compile(exprToFind, Pattern.CASE_INSENSITIVE)
                else Pattern.compile(exprToFind)

        var br: BufferedReader? = null
        try {
            val reader = if (otherArgs.size == 2) FileReader(otherArgs[1])
                else StringReader(lastRes)

            br = BufferedReader(reader);
        } catch (e: IOException) {
            throw Exception("grep: ${otherArgs[1]}: No such file or directory")
        }

        return getMatched(p, br)
    }

    private fun getMatched(p: Pattern, reader: BufferedReader): String {
        val matcher = p.matcher("")

        val res = StringBuilder()
        // count how many lines after match left to include
        var linesAfterMatchCnt = 0

        reader.lineSequence().forEach {
            matcher.reset(it)
            if (matcher.find()) {
                res.appendln(it)
                linesAfterMatchCnt = linesToInclude
            } else if (linesAfterMatchCnt > 0) {
                linesAfterMatchCnt -= 1
                res.appendln(it)
            }
        }
        reader.close()
        return res.toString()
    }
}

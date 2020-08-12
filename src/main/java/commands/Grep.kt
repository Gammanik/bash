package commands

import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameter
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.lang.StringBuilder
import java.util.regex.Pattern

/** searches any given input files, selecting lines that match the pattern
 * create only by calling [@link Grep.buildArgs] in case of parsing flags
 * **/
class Grep
    private constructor(private val lastRes: String)
    : Command() {

    // stores expr to search and filename (optional)
    @Parameter var otherArgs = mutableListOf<String>()
        private set

    @Parameter(names = ["-A"]) var lines: Int? = null
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
        val exprToFind = otherArgs.first()
        val file = otherArgs[1] // todo: could be from pipe

        val p = Pattern.compile(exprToFind)
        val matcher = p.matcher("")

        var br: BufferedReader? = null
        try {
            br = BufferedReader(FileReader(file));
        } catch (e: IOException) {
            // todo: handle
        }

        val res = StringBuilder()
        br?.lineSequence()?.forEach {
            matcher.reset(it)
            if (matcher.find()) {
                res.appendln(it)
            }
        }
        br?.close()

        return res.toString()
    }


    override fun toString(): String {
        return "$isWordSearch $caseInsensitivity $lines"
    }
}
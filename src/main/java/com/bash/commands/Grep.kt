package com.bash.commands

import com.bash.util.CmdRes
import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameter
import com.beust.jcommander.ParameterException
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
private constructor(private val lastRes: String, private val errorMessage: String?): Command() {

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
            var instance = Grep(lastRes, null)

            try {
                JCommander.newBuilder()
                        .addObject(instance)
                        .build()
                        .parse(*args.toTypedArray())
            } catch (e: ParameterException) {
                val errorMessage = "Expected a value after parameter -A"
                instance = Grep(lastRes, errorMessage)
            }

            return instance
        }
    }

    override fun run(): CmdRes {
        if (errorMessage != null) return CmdRes("", errorMessage)
        if (linesToInclude < 0) return CmdRes("", "-A can't have negative value")

        if (otherArgs.isEmpty())
            return CmdRes("", "usage: grep [-A num] [-w] [-i]")

        val expressionToFind = if (isWordSearch) "\\b${otherArgs.first()}\\b" else otherArgs.first()
        val pattern = if (caseInsensitivity)
            Pattern.compile(expressionToFind, Pattern.CASE_INSENSITIVE)
            else Pattern.compile(expressionToFind)

        return try {
            val reader = if (otherArgs.size == 2)
                FileReader(otherArgs[1])
                else StringReader(lastRes)

            CmdRes(getMatched(pattern, BufferedReader(reader)), "")
        } catch (e: IOException) {
            CmdRes("", "grep: ${otherArgs[1]}: No such file or directory")
        }

    }

    private fun getMatched(p: Pattern, reader: BufferedReader): String {
        val matcher = p.matcher("")

        val res = StringBuilder()
        // count how many lines after match left to include
        var linesAfterMatchLeft = 0

        reader.lineSequence().forEach {
            matcher.reset(it)
            if (matcher.find()) {
                res.appendln(it)
                linesAfterMatchLeft = linesToInclude
            } else if (linesAfterMatchLeft > 0) {
                linesAfterMatchLeft -= 1
                res.appendln(it)
            }
        }
        reader.close()
        return res.toString()
    }
}

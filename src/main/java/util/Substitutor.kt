package util

import java.lang.StringBuilder

/**
 * class for substituting variables like $a
 * from the environment
 */

class Substitutor {
    fun substitute(env: Map<String, String>, line: String): String {
        val res = StringBuilder()

        var i = 0
        while (i < line.length) {
            val ch = line[i]

            if (ch == '$' && i < line.length + 1) {
                val varName = getVarName(line.substring(i + 1))
                res.append(env[varName])
                i += varName.length
            } else {
                res.append(ch)
            }
            i += 1
        }

        return res.toString()
    }


    private fun getVarName(subLine: String): String {
        var i = 0
        val varName = StringBuffer()
        while ((i < subLine.length) && subLine[i] != ' ' && subLine[i] != '$' && subLine[i] != '\'') {
            varName.append(subLine[i])
            i += 1
        }

        return varName.toString()
    }

}

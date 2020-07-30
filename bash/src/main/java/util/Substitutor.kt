package util

import java.lang.StringBuilder

class Substitutor {
    companion object {
        //todo: google bash quotes handling
        fun substitute(env: Map<String, String>, line: String?): String? {
            if (line == null)
                return null

            val res = StringBuilder()

            var i = 0
            while (i < line.length) {
                val ch = line[i]

                if (ch.equals('$')) {
                    // todo: separate fun
                    var j = i + 1
                    val varName = StringBuffer()
                    while ((j < line.length) && !line[j].equals(' ') && !line[j].equals('$')) {
                        varName.append(line[j])
                        j += 1
                    }

                    i += j - 1
                    res.append(env[varName.toString()])
                } else {
                    res.append(ch)
                }
                i += 1
            }

            return res.toString()
        }

        private fun substituteVar(startIndex: Int, line: String, env: Map<String, String>): String {
            TODO("Not yet implemented")
        }
    }


}
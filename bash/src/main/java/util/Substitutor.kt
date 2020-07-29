package util

import java.lang.StringBuilder

class Substitutor {
    companion object {
        //todo: google bash quotes handling
        fun substitute(env: Map<String, String>, line: String?): String? {
            if (line == null)
                return null

            val res = StringBuilder()
            line.split(" ").forEach {// todo: fix
                    if (it.trim().startsWith("$")) {
                        res.append(env.get(it.trim().substring(1)))
                    } else {
                        res.append(it)
                    }
        }

            return res.toString()
        }
    }
}
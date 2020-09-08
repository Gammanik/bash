package com.bash.commands

import com.bash.util.CmdRes
import com.bash.util.Settings
import main.java.com.bash.util.Environment
import java.io.*
import java.lang.Exception


/** class for handling not yet supported bash com.bash.commands
 * redirecting the commands to real bash and returns it's output
 * all the environment variables are already substituted
 * **/
class External(
        private val cmdString: String,
        private val args: List<String>,
        private val lastRes: String,
        private val env: Environment) : Command() {

    override fun run(): CmdRes {
        val cmd = "$cmdString ${args.joinToString(separator = " ")}"

        val process: Process
        try {
            process = if (Settings.IS_WINDOWS)
                ProcessBuilder("CMD", "/C", cmd).start()
            else ProcessBuilder("/bin/bash", "-c", cmd).start()
        } catch (e: Exception) {
            return CmdRes("", "can't start process for command: $cmdString")
        }

        try { // redirecting output from pipe in case it's piped command
            val input = BufferedWriter(OutputStreamWriter(process.outputStream))
            input.write(lastRes)
            input.flush()
            input.close()
        } catch (e: IOException) { }


        return CmdRes(readStream(process.inputStream), readStream(process.errorStream))
    }

    private fun readStream(st: InputStream): String {
        val reader = BufferedReader(InputStreamReader(st))
        val outRes = mutableListOf<String>()

        var tmpOut: String? // reading output of a command
        while (reader.readLine().also { tmpOut = it } != null) {
            outRes.add(tmpOut!!)
        }

        return outRes.joinToString(separator = System.lineSeparator())
    }
}

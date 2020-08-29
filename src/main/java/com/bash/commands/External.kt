package com.bash.commands

import com.bash.util.CmdRes
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.lang.Exception


/** class for handling not yet supported bash com.bash.commands
 * redirecting the com.bash.commands to real bash and returns it's output
 * all the environment variables are already substituted
 * **/
class External(
        private val cmdString: String,
        private val args: List<String>,
        private val lastRes: String) : Command() {

    override fun run(): CmdRes {
        val osName = System.getProperty("os.name").toLowerCase()
        val cmd = "$cmdString ${args.joinToString(separator = " ")}"

        val process: Process
        try {
            process = if (osName.contains("win"))
                ProcessBuilder("CMD", "/C", cmd).start()
            else ProcessBuilder("/bin/bash", "-c", cmd).start()
        } catch (e: Exception) {
            return CmdRes("", "can't start process for command: $cmdString")
        }

        // redirecting output from pipe in case it's piped command
        val input = BufferedWriter(OutputStreamWriter(process.outputStream))
        input.write(lastRes)
        input.flush()
        input.close()

        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val res = mutableListOf<String>()

        var tmpOut: String? // reading output of a command
        while (reader.readLine().also { tmpOut = it } != null) {
            res.add(tmpOut!!)
        }

        return CmdRes(res.joinToString(separator = "\n"), "")
    }
}
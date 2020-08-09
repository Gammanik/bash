package commands

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter


/** class for handling not yet supported bash commands
 * redirecting the commands to real bash and returns it's output
 * all the environment variables are already substituted
 * **/
class External(
        private val cmdString: String,
        private val args: List<String>,
        private val lastRes: String) : Command() {

    override fun run(): String {
        val pb = ProcessBuilder()

        pb.command("bash", "-c", "$cmdString ${args.joinToString(separator = " ")}")
        val process: Process = pb.start()

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

        return res.joinToString(separator = "\n", postfix = "\n")
    }
}
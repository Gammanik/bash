package commands

import java.io.BufferedReader
import java.io.InputStreamReader


/** class for handling not yet supported bash commands
 * redirecting the commands to real bash and returns it's output
 * **/
class Other(
        private val cmdString: String,
        private val args: List<String>,
        private val lastRes: String) : Command() {

    override fun run(): String {
        val pb = ProcessBuilder()
        // todo: pb.directory()
//        val commandArgs = if ()
        pb.command("bash", "-c", "echo lol")

        val process: Process = pb.start()
        val reader = BufferedReader(InputStreamReader(process.inputStream))

        val res = StringBuilder()

        var tmpOut: String?
        while (reader.readLine().also { tmpOut = it } != null) {
            res.append("$tmpOut ")
        }

        return res.toString()
    }
}
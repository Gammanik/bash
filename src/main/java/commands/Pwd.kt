package commands

import util.CmdRes

/** prints the current working directory **/
class Pwd: Command() {
    override fun run(): CmdRes {
        return CmdRes(System.getProperty("user.dir"), "")
    }
}

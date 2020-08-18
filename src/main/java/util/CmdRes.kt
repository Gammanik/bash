package util


data class CmdRes(val sdtOut: String, val stdErr: String) {
    override fun toString(): String {
        return sdtOut
    }
}
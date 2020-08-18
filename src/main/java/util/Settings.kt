package util

import commands.Pwd

object Settings {
    val PREFIX = Pwd().run().sdtOut + ">$"
    const val OUTPUT_COLOR = "\u001B[32m" // bash result color
    const val ANSI_RESET = "\u001B[0m"    // reset the color back
}

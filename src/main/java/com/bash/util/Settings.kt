package com.bash.util

import com.bash.commands.Pwd

/** CLI settings **/
object Settings {
    val IS_WINDOWS = System.getProperty("os.name").toLowerCase().contains("win")
    val PREFIX = Pwd().run().sdtOut + ">$"
    const val OUTPUT_COLOR = "\u001B[32m" // bash result color
    const val ERR_COLOR = "\u001B[31m"  // err stream color
    const val ANSI_RESET = "\u001B[0m"    // reset the color back
}

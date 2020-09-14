package main.java.com.bash.util

import java.io.File

/** class for CLI environment **/
class Environment {
    private var currentDirectory = File("").absolutePath

    /** function to change env directory **/
    fun changeDirectory(newDirectory: String) {
        currentDirectory = newDirectory
    }

    /** getter for directory **/
    fun getDirectory(): String {
        return currentDirectory
    }
}
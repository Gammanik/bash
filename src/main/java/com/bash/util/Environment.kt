package main.java.com.bash.util

import java.io.File

class Environment {
    private var currentDirectory = File("").absolutePath

    fun changeDirectory(newDirectory: String) {
        currentDirectory = newDirectory
    }

    fun getDirectory(): String {
        return currentDirectory
    }
}
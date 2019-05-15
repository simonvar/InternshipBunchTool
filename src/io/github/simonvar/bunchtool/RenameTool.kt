package io.github.simonvar.bunchtool

import java.io.File
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        exitWithMessage(
            """
                Usage: rename <path1 path2 ...>

                Rename all files with .kt/.java to .kt.2019/.java.2019 in folder (and sub-folders)
            """.trimIndent()
        )
    }

    args.forEach(::proceed)

    println("Done!")
}

private fun proceed(path: String) {
    val folder = File(path)

    if (folder.isFile) {
        println("$path is not folder!")
        return
    }

    folder.walkTopDown().forEach { file ->
        if (isDesiredFile(file)) {
            rename(file)
        } else if (file.isDirectory) {
            proceed(folder.path + file.path)
        }
    }
}

private fun isDesiredFile(file: File) = file.isFile && (file.extension == "kt" || file.extension == "java")

private fun rename(file: File) {
    val newFile = File(file.path + ".2019")
    file.renameTo(newFile)
    printResult(file.path, newFile.path)
}

private fun printResult(fromName: String, toName: String) {
    println("$fromName ------------> $toName")
}

private fun exitWithMessage(message: String) {
    println(message)
    exitProcess(0)
}
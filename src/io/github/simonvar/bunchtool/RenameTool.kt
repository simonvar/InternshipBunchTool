package io.github.simonvar.bunchtool

import java.io.File
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        exitWithMessage(
            """
                Rename all files with .kt/.java to .kt.2019/.java.2019 in folder (and sub-folders)
                Usage: rename <path1 path2 ...>
            """.trimIndent()
        )
    }

    args.forEach(::proceed)
}

private fun proceed(path: String) {
    val folder = File(path)

    if (folder.isFile) {
        println("\"$path\" is file!")
        return
    }

    folder.walkTopDown()
        .onFail { file, e ->
            println("Can not read files in \"$file\"")
        }.forEach { file ->
            if (file != folder) {
                if (isDesiredFile(file)) {
                    printSuccess(file)
                } else if (file.isDirectory) {
                    proceed(file.path)
                }
            }
        }
}

private fun isDesiredFile(file: File) = file.isFile && (file.extension == "kt" || file.extension == "java")


private fun printSuccess(file: File) {
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
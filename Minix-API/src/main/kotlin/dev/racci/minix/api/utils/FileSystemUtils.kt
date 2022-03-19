package dev.racci.minix.api.utils

import java.io.File
import java.nio.file.Path

object FileSystemUtils : UtilObject by UtilObject {

    fun size(
        file: File,
        maxDepth: Int = Int.MAX_VALUE
    ): Long {
        var size = 0L
        file.walkTopDown().onEnter {
            size += it.length()
            true
        }.onFail { f, e ->
            minix.log.debug(e) { "Could not enter file [${f.path}]." }
        }.maxDepth(maxDepth) // .sumOf { it.length() }
        return size
    }
}

fun Path.size(): Long = FileSystemUtils.size(this.toFile())

fun File.size(): Long = FileSystemUtils.size(this)

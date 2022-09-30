package dev.racci.minix.api.utils

import java.io.File
import java.io.IOException
import java.nio.file.Path

object FileSystemUtils : UtilObject {

    /**
     * Calculates the size of a file / directory.
     * If the file / directory does not exist, 0 is returned.
     * This can hang if there are too many nested directories, so it is recommended to use a WithTimeout wrapper around this method.
     *
     * @param file The file / directory to calculate the size of.
     * @param maxDepth The maximum depth of nested directories to calculate the size of.
     * @return The size of the file / directory.
     */
    fun size(
        file: File,
        maxDepth: Int = Int.MAX_VALUE
    ): Long {
        var size = 0L

        if (file.isFile) {
            return file.length()
        }

        if (!file.isDirectory) return 0

        val folders = mutableListOf<File>()
        var depth = 0
        val itr = file.listFiles()?.iterator() ?: return 0

        while (folders.isNotEmpty() || itr.hasNext()) {
            try {
                val next = folders.getOrNull(0)?.also {
                    folders.removeAt(0)
                } ?: run { depth = 0; itr.next() }

                when {
                    depth >= maxDepth -> { /* Skip this folder */ }
                    next.isDirectory -> {
                        depth++
                        folders.addAll(next.listFiles() ?: continue)
                    }
                    else -> size += next.length()
                }
            } catch (e: IOException) {
                logger.debug(e) { "Could not enter file" }
            }
        }
        return size
    }
}

/** @see [FileSystemUtils.size] */
fun Path.size(): Long = FileSystemUtils.size(this.toFile())

/** @see [FileSystemUtils.size] */
fun File.size(): Long = FileSystemUtils.size(this)

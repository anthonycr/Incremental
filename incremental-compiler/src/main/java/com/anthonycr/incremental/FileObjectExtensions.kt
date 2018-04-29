package com.anthonycr.incremental

import com.google.common.base.Charsets
import com.google.common.io.CharStreams
import java.io.*
import javax.tools.FileObject

/**
 * Read a [FileObject] line by line into a [List] of [String].
 */
fun FileObject.readFileAsStrings(): List<String> = try {
    BufferedReader(InputStreamReader(openInputStream(), Charsets.UTF_8)).use {
         CharStreams.readLines(it)
    }
} catch (exception: IOException) {
    emptyList()
}


/**
 * Write a [List] of [String] line by line into a [FileObject].
 */
fun FileObject.writeListToResource(list: List<String>) {
    BufferedWriter(OutputStreamWriter(openOutputStream(), Charsets.UTF_8)).use { writer ->
        list.forEach {
            writer.write(it)
            writer.newLine()
        }
        writer.flush()
    }
}
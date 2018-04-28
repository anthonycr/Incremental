package com.anthonycr.incremental

import com.google.common.base.Charsets
import com.google.common.io.CharStreams
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import javax.tools.FileObject

/**
 * Created by anthonycr on 4/27/18.
 */


fun FileObject.readFileAsStrings(): List<String> =
        BufferedReader(InputStreamReader(openInputStream(), Charsets.UTF_8)).use {
            return CharStreams.readLines(it)
        }

fun FileObject.writeListToResource(list: List<String>) {
    BufferedWriter(OutputStreamWriter(openOutputStream(), Charsets.UTF_8)).use { writer ->
        list.forEach {
            writer.write(it)
            writer.newLine()
        }
        writer.flush()
    }
}
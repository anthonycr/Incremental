package com.anthonycr.incremental

import java.io.IOException
import javax.annotation.processing.Filer
import javax.tools.StandardLocation


/**
 * Created by anthonycr on 4/26/18.
 */
class GradleResourcesSource(private val filer: Filer) {

    fun resourceFileAsList(): Set<GradleResourcesEntry> = try {
        filer.getResource(StandardLocation.CLASS_OUTPUT, "", RESOURCE_FILE_PATH)
                .readFileAsStrings()
                .map { line ->
                    val splitLine = line.split(',')
                    when {
                        splitLine.size != 2 ->
                            GradleResourcesEntry.Other(line)
                        splitLine[1] == IncrementalType.ISOLATING.value ->
                            GradleResourcesEntry.IncrementalProcessor.Isolating(splitLine[0])
                        splitLine[1] == IncrementalType.AGGREGATING.value ->
                            GradleResourcesEntry.IncrementalProcessor.Isolating(splitLine[0])
                        else ->
                            GradleResourcesEntry.Other(line)
                    }
                }
                .toSet()
    } catch (ioException: IOException) {
        emptySet()
    }

}

const val RESOURCE_FILE_PATH = "META-INF/gradle/incremental.annotation.processors"
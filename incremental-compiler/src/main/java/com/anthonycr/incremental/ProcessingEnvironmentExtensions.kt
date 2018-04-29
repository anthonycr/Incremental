package com.anthonycr.incremental

import javax.annotation.processing.ProcessingEnvironment
import javax.tools.FileObject
import javax.tools.StandardLocation

/**
 * Returns the environment [Boolean] option with the provided [name] or the [default] if the
 * consumer did not provided a default value.
 */
fun ProcessingEnvironment.option(name: String, default: Boolean): Boolean {
    return options[name]?.toBoolean() ?: default
}

/**
 * Creates the [FileObject] for the incremental resource file in the gradle folder.
 */
fun ProcessingEnvironment.createIncrementalGradleResource(): FileObject {
    return filer.createResource(StandardLocation.CLASS_OUTPUT, "", RESOURCE_FILE_PATH)
}

/**
 * Gets the existing [FileObject] for the incremental resource file in the gradle folder.
 */
fun ProcessingEnvironment.getIncrementalGradleResource(): FileObject {
    return filer.getResource(StandardLocation.CLASS_OUTPUT, "", RESOURCE_FILE_PATH)
}

private const val RESOURCE_FILE_PATH = "META-INF/gradle/incremental.annotation.processors"
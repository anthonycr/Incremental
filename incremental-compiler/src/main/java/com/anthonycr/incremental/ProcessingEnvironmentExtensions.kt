package com.anthonycr.incremental

import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.Processor
import javax.lang.model.element.Element
import javax.lang.model.type.TypeMirror
import javax.tools.FileObject
import javax.tools.StandardLocation
import kotlin.reflect.KClass

/**
 * Returns the environment [Boolean] option with the provided [name] or the [default] if the
 * consumer did not provided a default value.
 */
fun ProcessingEnvironment.option(name: String, default: Boolean): Boolean {
    return options[name]?.toBoolean() ?: default
}

/**
 * Returns the [TypeMirror] that represents the provided [KClass].
 */
fun ProcessingEnvironment.classToTypeMirror(clazz: KClass<*>): TypeMirror {
    return elementUtils.getTypeElement(clazz.java.name).asType()
}

/**
 * Returns true if the [Element] implements the [Processor] interface, false otherwise.
 */
fun ProcessingEnvironment.doesImplementProcessor(element: Element): Boolean {
    return typeUtils.isAssignable(element.asType(), classToTypeMirror(Processor::class))
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

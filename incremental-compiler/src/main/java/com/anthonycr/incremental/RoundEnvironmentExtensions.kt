package com.anthonycr.incremental

import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element

/**
 * Returns the [Set] of elements annotated with [T].
 */
inline fun <reified T : Annotation> RoundEnvironment.elementsAnnotatedWith(): Set<Element> {
    return getElementsAnnotatedWith(T::class.java)
}
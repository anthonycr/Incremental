package com.anthonycr.incremental

import javax.annotation.processing.Messager
import javax.lang.model.element.Element
import javax.tools.Diagnostic

/**
 * A logger that communicates to the compiler.
 */
class Logger(private val messager: Messager, private val debug: Boolean) {

    /**
     * Report an info level [message].
     */
    fun info(message: String) {
        if (debug) {
            messager.printMessage(Diagnostic.Kind.NOTE, message)
        }
    }

    /**
     * Report an error level [message] on the specified [element].
     */
    fun error(element: Element, message: String) {
        messager.printMessage(Diagnostic.Kind.ERROR, message, element)
    }

}

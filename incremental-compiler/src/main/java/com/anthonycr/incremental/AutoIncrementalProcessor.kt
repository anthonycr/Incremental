package com.anthonycr.incremental

import com.google.auto.service.AutoService
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedOptions
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

/**
 * The annotation processor that writes processors annotated with [AutoIsolating] and
 * [AutoAggregating] to resources.
 */
@AutoService(Processor::class)
@SupportedOptions(OPTIONS_DEBUG)
class AutoIncrementalProcessor : AbstractProcessor() {

    private var processedElements: Set<GradleResourcesEntry> = setOf()

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latest()

    override fun getSupportedAnnotationTypes(): Set<String> = setOf(
            AutoIsolating::class.java.name,
            AutoAggregating::class.java.name
    )

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        val debug = processingEnv.option(OPTIONS_DEBUG, false)
        val logger = Logger(processingEnv.messager, debug)

        if (!roundEnv.processingOver()) {
            val processedAggregatingElements = roundEnv
                    .getElementsAnnotatedWith(AutoAggregating::class.java)
                    .filterIsInstanceOr<Element, TypeElement> {
                        logger.error(it, "Annotation is only applicable to classes.")
                    }
                    .map { GradleResourcesEntry.IncrementalProcessor.Aggregating(it.qualifiedName.toString()) }
                    .onEach { logger.info("Resource entry: ${it.stringValue}") }

            val processedIsolatingElements = roundEnv
                    .getElementsAnnotatedWith(AutoIsolating::class.java)
                    .filterIsInstanceOr<Element, TypeElement> {
                        logger.error(it, "Annotation is only applicable to classes.")
                    }
                    .map { GradleResourcesEntry.IncrementalProcessor.Isolating(it.qualifiedName.toString()) }
                    .onEach { logger.info("Resource entry: ${it.stringValue}") }

            processedElements = processedElements
                    .union(processedAggregatingElements)
                    .union(processedIsolatingElements)
        } else {
            val combinedList = processingEnv
                    .getIncrementalGradleResource()
                    .readFileAsStrings()
                    .asGradleResourceEntries()
                    .union(processedElements)
                    .map { it.stringValue }

            logger.info("Writing to resources")

            processingEnv
                    .createIncrementalGradleResource()
                    .writeListToResource(combinedList)
        }

        return true
    }

}

private const val OPTIONS_DEBUG = "incremental.debug"
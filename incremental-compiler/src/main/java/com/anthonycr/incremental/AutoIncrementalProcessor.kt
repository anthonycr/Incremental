package com.anthonycr.incremental

import com.google.auto.service.AutoService
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedOptions
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.StandardLocation

/**
 * Created by anthonycr on 4/26/18.
 */
@AutoService(Processor::class)
@SupportedOptions(OPTIONS_DEBUG)
class AutoIncrementalProcessor : AbstractProcessor() {

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latest()

    override fun getSupportedAnnotationTypes(): Set<String> = setOf(
            AutoIsolating::class.java.name,
            AutoAggregating::class.java.name
    )

    private var processedElements: Set<GradleResourcesEntry> = setOf()

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        val debug = processingEnv.options[OPTIONS_DEBUG]?.toBoolean() ?: false
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
            val combinedList = GradleResourcesSource(processingEnv.filer)
                    .resourceFileAsList()
                    .union(processedElements)
                    .map { it.stringValue }

            logger.info("Writing to resources")

            processingEnv.filer.createResource(StandardLocation.CLASS_OUTPUT, "", RESOURCE_FILE_PATH)
                    .writeListToResource(combinedList)
        }

        return true
    }

}

private const val OPTIONS_DEBUG = "incremental.debug"
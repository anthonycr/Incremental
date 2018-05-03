package com.anthonycr.incremental

import com.google.auto.service.AutoService
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedOptions
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror

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

    override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {
        val debug = processingEnv.option(OPTIONS_DEBUG, false)
        val logger = Logger(processingEnv.messager, debug)

        val processorInterface = processingEnv.classToTypeMirror(Processor::class)

        if (!roundEnv.processingOver()) {
            val processedAggregatingElements = roundEnv
                    .getElementsAnnotatedWith(AutoAggregating::class.java)
                    .validateAnnotatedElements(logger, processorInterface)
                    .map { it.qualifiedName.toString() }
                    .map { GradleResourcesEntry.IncrementalProcessor.Aggregating(it) }
                    .onEach { logger.info("Resource entry: ${it.stringValue}") }

            val processedIsolatingElements = roundEnv
                    .getElementsAnnotatedWith(AutoIsolating::class.java)
                    .validateAnnotatedElements(logger, processorInterface)
                    .map { it.qualifiedName.toString() }
                    .map { GradleResourcesEntry.IncrementalProcessor.Isolating(it) }
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

    /**
     * Validate [Element] annotated for processing by this [Processor] to ensure that they abide by
     * the rules of the annotation. Elements must be classes or else an error will be logged. The
     * classes must implement the [Processor] interface or else a warning will be logged.
     */
    private fun Set<Element>.validateAnnotatedElements(
            logger: Logger,
            processorInterface: TypeMirror
    ): List<TypeElement> {
        return filterIsInstanceOr<Element, TypeElement> {
            logger.error(it, "Annotation is only applicable to classes.")
        }.filterElse({ it.kind == ElementKind.CLASS }) {
            logger.error(it, "Annotation is only applicable to classes.")
        }.filterElse({ processingEnv.implementsInterface(it, processorInterface) }) {
            logger.warning(it, "Annotation should only be used on processors.")
        }
    }

}

private const val OPTIONS_DEBUG = "incremental.debug"
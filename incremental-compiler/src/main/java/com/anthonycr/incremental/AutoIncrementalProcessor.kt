package com.anthonycr.incremental

import com.google.auto.service.AutoService
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedOptions
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic
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
        if (!roundEnv.processingOver()) {
            val processedAggregatingElements = roundEnv
                    .getElementsAnnotatedWith(AutoAggregating::class.java)
                    .filterIsInstance<TypeElement>()
                    .map { GradleResourcesEntry.IncrementalProcessor.Aggregating(it.qualifiedName.toString()) }
                    .toSet()

            val processedIsolatingElements = roundEnv
                    .getElementsAnnotatedWith(AutoIsolating::class.java)
                    .filterIsInstance<TypeElement>()
                    .map { GradleResourcesEntry.IncrementalProcessor.Isolating(it.qualifiedName.toString()) }
                    .toSet()

            processedElements = processedElements
                    .union(processedAggregatingElements)
                    .union(processedIsolatingElements)
        } else {
            val combinedList = GradleResourcesSource(processingEnv.filer)
                    .resourceFileAsList()
                    .union(processedElements)
                    .map { it.stringValue }
                    .also {
                        it.forEach {
                            processingEnv.messager.printMessage(Diagnostic.Kind.NOTE, "Element: $it")
                        }
                    }

            processingEnv.messager.printMessage(Diagnostic.Kind.NOTE, "Writing to resources")

            processingEnv.filer.createResource(StandardLocation.CLASS_OUTPUT, "", RESOURCE_FILE_PATH)
                    .writeListToResource(combinedList)
        }

        return true
    }

}

private const val OPTIONS_DEBUG = "auto.incremental.debug"
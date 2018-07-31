package com.anthonycr.example

import com.anthonycr.incremental.AutoAggregating
import com.anthonycr.incremental.AutoDynamic
import com.google.auto.service.AutoService
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.TypeElement

/**
 * Aggregating annotation processors should be annotated with [AutoAggregating].
 */
@AutoDynamic
@AutoService(Processor::class)
class DynamicProcessor : AbstractProcessor() {

    private var isIsolating: Boolean = true

    override fun getSupportedOptions(): Set<String> {
        return setOf(
                if (isIsolating) {
                    "org.gradle.annotation.processing.aggregating"
                } else {
                    "org.gradle.annotation.processing.isolating"
                }
        )
    }

    override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {
        TODO("not implemented")
    }
}
package com.anthonycr.example

import com.anthonycr.incremental.AutoAggregating
import com.google.auto.service.AutoService
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.TypeElement

/**
 * Aggregating annotation processors should be annotated with [AutoAggregating].
 */
@AutoAggregating
@AutoService(Processor::class)
class AggregatingProcessor : AbstractProcessor() {
    override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {
        TODO("not implemented")
    }
}

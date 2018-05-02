package com.anthonycr.example

import com.anthonycr.incremental.AutoIsolating
import com.google.auto.service.AutoService
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.TypeElement

/**
 * Isolating annotation processors should be annotated with [AutoIsolating].
 */
@AutoIsolating
@AutoService(Processor::class)
class IsolatingProcessor : AbstractProcessor() {
    override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {
        TODO("not implemented")
    }
}
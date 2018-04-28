package com.anthonycr.example

import com.anthonycr.incremental.AutoIsolating
import com.google.auto.service.AutoService
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.TypeElement

/**
 * Created by anthonycr on 4/26/18.
 */
@AutoIsolating
@AutoService(Processor::class)
class IsolatingProcessor : AbstractProcessor() {
    override fun process(p0: MutableSet<out TypeElement>?, p1: RoundEnvironment?): Boolean {
        TODO("not implemented")
    }
}
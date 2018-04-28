package com.anthonycr.incremental

/**
 * Created by anthonycr on 4/27/18.
 */
sealed class GradleResourcesEntry(open val stringValue: String) {

    sealed class IncrementalProcessor(
            processorName: String,
            incrementalType: IncrementalType
    ) : GradleResourcesEntry("$processorName,${incrementalType.value}") {

        data class Isolating(
                private val processorName: String
        ) : IncrementalProcessor(processorName, IncrementalType.ISOLATING)

        data class Aggregating(
                private val processorName: String
        ) : IncrementalProcessor(processorName, IncrementalType.AGGREGATING)
    }

    data class Other(override val stringValue: String) : GradleResourcesEntry(stringValue)

}
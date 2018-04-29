package com.anthonycr.incremental

/**
 * Representation of an entry in META-INF/gradle/incremental.annotation.processors.
 */
sealed class GradleResourcesEntry(open val stringValue: String) {

    /**
     * Representation of an incremental annotation processor entry.
     */
    sealed class IncrementalProcessor(
            processorName: String,
            incrementalType: IncrementalType
    ) : GradleResourcesEntry("$processorName,${incrementalType.value}") {

        /**
         * Representation of an isolating incremental annotation processor entry.
         */
        data class Isolating(
                private val processorName: String
        ) : IncrementalProcessor(processorName, IncrementalType.ISOLATING)

        /**
         * Representation of an isolating aggregating annotation processor entry.
         */
        data class Aggregating(
                private val processorName: String
        ) : IncrementalProcessor(processorName, IncrementalType.AGGREGATING)
    }

    /**
     * Representation of an unknown entry type.
     */
    data class Other(override val stringValue: String) : GradleResourcesEntry(stringValue)

}
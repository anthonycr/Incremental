package com.anthonycr.incremental

/**
 * Representation of the different types of incremental annotation processors.
 *
 * @param value the [String] value that should be written to resources.
 */
enum class IncrementalType(val value: String) {
    AGGREGATING("aggregating"),
    ISOLATING("isolating")
}
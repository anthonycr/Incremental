package com.anthonycr.incremental

/**
 * Perform an action with each item in a collection and return the collection.
 */
inline fun <T> Collection<T>.onEach(block: (T) -> Unit): Collection<T> {
    forEach(block)
    return this
}
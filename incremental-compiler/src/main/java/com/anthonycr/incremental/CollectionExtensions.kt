package com.anthonycr.incremental

/**
 * Perform an action with each item in a collection and return the collection.
 */
inline fun <T> Collection<T>.onEach(block: (T) -> Unit): Collection<T> {
    forEach(block)
    return this
}

/**
 * Filter a [Collection] of type [T] to type [R] and if an item is not of type [R] then execute the
 * [onNotInstance] block.
 */
inline fun <T, reified R> Iterable<T>.filterIsInstanceOr(onNotInstance: (T) -> Unit): List<R> {
    val destination = ArrayList<R>()
    for (element in this) {
        if (element is R) {
            destination.add(element)
        } else {
            onNotInstance(element)
        }
    }
    return destination
}

/**
 * Filters out [T] instances that do not match the provided [predicate] and provides any elements
 * that do not pass the filter to [orElse].
 */
inline fun <T> Collection<T>.filterElse(
        predicate: (T) -> Boolean,
        orElse: (T) -> Unit
): List<T> = filter { element ->
    predicate(element).also {
        if (!it) {
            orElse(element)
        }
    }
}
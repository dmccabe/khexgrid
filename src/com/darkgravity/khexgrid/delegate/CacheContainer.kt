package com.darkgravity.khexgrid.delegate

/**
 * @author Dan McCabe
 */
class CacheContainer {
    private val caches = mutableSetOf<Cache<*>>()

    fun add(cache: Cache<*>) {
        caches += cache
    }

    operator fun plusAssign(cache: Cache<*>) = add(cache)

    fun remove(cache: Cache<*>) {
        caches -= cache
    }

    operator fun minusAssign(cache: Cache<*>) = remove(cache)

    fun invalidate() = caches.forEach { it.invalidate() }
}
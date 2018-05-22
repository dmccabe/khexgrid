package com.darkgravity.khexgrid.delegate

/**
 * @author Dan McCabe
 */
class CacheContainer {
    private val caches = mutableSetOf<CachedProperty<*>>()

    fun add(cache: CachedProperty<*>) {
        caches += cache
    }

    operator fun plusAssign(cache: CachedProperty<*>) = add(cache)

    fun remove(cache: CachedProperty<*>) {
        caches -= cache
    }

    operator fun minusAssign(cache: CachedProperty<*>) = remove(cache)

    fun invalidate() = caches.forEach { it.invalidate() }
}
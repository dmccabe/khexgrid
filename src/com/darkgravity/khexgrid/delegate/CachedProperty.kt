package com.darkgravity.khexgrid.delegate

/**
 * @author Dan McCabe
 */
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class CachedProperty<out T>(private val loader: () -> T) : ReadOnlyProperty<Any, T> {
    private var cachedValue: T? = null

    override fun getValue(thisRef: Any, property: KProperty<*>): T =
        this.cachedValue ?: loader().also { this.cachedValue = it }

    fun invalidate() {
        cachedValue = null
    }
}

fun <T> cache(registry: CacheRegistry, loader: () -> T): CachedProperty<T> =
    CachedProperty(loader).also { registry += it }
package com.darkgravity.khexgrid.observer

/**
 * @author Dan McCabe
 */
interface Observable<T> {
    fun addListener(listener: T)
    fun removeListener(listener: T)
    fun notify(block: (T) -> Unit)
}
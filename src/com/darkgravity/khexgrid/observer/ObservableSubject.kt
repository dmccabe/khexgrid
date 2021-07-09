package com.darkgravity.khexgrid.observer

/**
 * @author Dan McCabe
 */
class ObservableSubject<T> : Observable<T> {
    private val listeners = mutableSetOf<T>()

    override fun addListener(listener: T) {
        listeners += listener
    }

    override fun removeListener(listener: T) {
        listeners -= listener
    }

    override fun notify(block: (T) -> Unit) {
        listeners.forEach(block)
    }
}
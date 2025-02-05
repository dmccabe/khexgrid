package com.darkgravity.khexgrid.observer

class TestListener {
    var callCount = 0
        private set

    fun reset() {
        callCount = 0
    }

    fun eventFired() {
        callCount++
    }
}
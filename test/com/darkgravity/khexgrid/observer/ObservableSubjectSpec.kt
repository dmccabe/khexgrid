package com.darkgravity.khexgrid.observer

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.equalTo
import io.kotest.core.spec.style.DescribeSpec

/**
 * @author Dan McCabe
 */
object ObservableSubjectSpec : DescribeSpec({
    lateinit var subject: ObservableSubject<TestListener>
    val listener = TestListener()

    beforeEach {
        subject = ObservableSubject()
        listener.reset()
    }

    fun notify() = subject.notify { it.eventFired() }

    describe("ObservableSubject") {
        describe(".addListener") {
            it("adds the listener provided") {
                subject.addListener(listener)
                notify()
                assert.that(listener.callCount, equalTo(1))
            }

            it("doesn't duplicate an already added listener") {
                subject.addListener(listener)
                subject.addListener(listener)
                notify()
                assert.that(listener.callCount, equalTo(1))
            }
        }

        describe(".removeListener") {
            it("performs a no-op if listener not attached") {
                subject.removeListener(listener)
                notify()
                assert.that(listener.callCount, equalTo(0))
            }

            it("removes attached listener") {
                subject.addListener(listener)
                subject.removeListener(listener)
                notify()
                assert.that(listener.callCount, equalTo(0))
            }
        }

        describe(".notify") {
            it("notifies registered listeners for each invocation") {
                subject.addListener(listener)
                repeat(3) { notify() }
                assert.that(listener.callCount, equalTo(3))
            }

            it("notifies multiple listeners") {
                val listeners = (0 until 3).map { TestListener() }
                listeners.forEach { subject.addListener(it) }
                repeat(2) { notify() }
                assert.that(listeners.map { it.callCount }, equalTo((0 until 3).map { 2 }))
            }
        }
    }
})
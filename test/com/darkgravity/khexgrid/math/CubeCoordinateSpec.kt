package com.darkgravity.khexgrid.math

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.natpryce.hamkrest.allElements
import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.isIn
import com.natpryce.hamkrest.throws
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.jetbrains.spek.subject.SubjectSpek

/**
 * @author Dan McCabe
 */
object CubeCoordinateSpec : SubjectSpek<CubeCoordinate>( {
    subject { CubeCoordinate(2, 5, -7) }
    val modifier = CubeCoordinate(3, 4, -7)

    describe("CubeCoordinate") {
        describe("init") {
            it("defaults to 0, 0, 0") {
                assert.that(CubeCoordinate(), equalTo(CubeCoordinate(0, 0, 0)))
            }
            it("defaults z to -x - y") {
                assert.that(CubeCoordinate(1, 3), equalTo(CubeCoordinate(1, 3, -4)))
            }
            it("throws exception on invalid values") {
                assert.that( { CubeCoordinate(1, 2, 3) }, throws<IllegalArgumentException>())
            }
        }

        describe(".add") {
            it("does not modify the original") {
                subject + modifier
                assert.that(subject, equalTo(CubeCoordinate(2, 5, -7)))
            }
            it("returns a new instance with the added values") {
                assert.that(subject + modifier, equalTo(CubeCoordinate(5, 9, -14)))
            }
            it("adding to itself yields two times original") {
                assert.that(subject + subject, equalTo(CubeCoordinate(4, 10, -14)))
            }
            it("adding to negative itself yields identity") {
                assert.that(subject + -subject, equalTo(CubeCoordinate()))
            }
        }

        describe(".subtract") {
            it("does not modify the original") {
                subject - modifier
                assert.that(subject, equalTo(CubeCoordinate(2, 5, -7)))
            }
            it("returns a new instance with the subtracted values") {
                assert.that(subject - modifier, equalTo(CubeCoordinate(-1, 1, 0)))
            }
            it("subtracting from itself yields identity") {
                assert.that(subject - subject, equalTo(CubeCoordinate()))
            }
        }

        describe(".unaryMinus") {
            it("negates values") {
                assert.that(-subject, equalTo(CubeCoordinate(-2, -5, 7)))
            }
            it("returns identity on double negation") {
                assert.that(-(-subject), equalTo(subject))
            }
        }

        describe(".unaryPlus") {
            it("has no effect") {
                assert.that(+subject, equalTo(subject))
            }
        }

        describe(".neighbors") {
            it("returns all neighboring coordinates") {
                assert.that(subject.neighbors(), equalTo(CubeCoordinate.DIRECTIONS.map { subject + it }))
            }
        }

        describe(".diagonalNeighbors") {
            it("returns all neighboring coordinates") {
                assert.that(subject.diagonalNeighbors(), equalTo(CubeCoordinate.DIAGONALS.map { subject + it }))
            }
        }

        describe(".distance") {
            it("returns 0 for itself") {
                assert.that(subject.distance(subject), equalTo(0))
            }
            it("returns 1 for a neighbor") {
                assert.that(subject.distance(subject.neighbors().first()), equalTo(1))
            }
            it("returns correct value for non-neighboring location") {
                assert.that(subject.distance(CubeCoordinate(1, 2, -3)), equalTo(4))
            }
        }

        describe(".equals") {
            it("identifies same objects as equal") {
                assert.that(subject, equalTo(subject))
            }
            it("identifies different objects with same values as equal") {
                assert.that(subject, equalTo(CubeCoordinate(2, 5, -7)))
            }
            it("identifies different objects with different values as not equal") {
                assert.that(subject, !equalTo(CubeCoordinate(2, 4, -6)))
            }
        }

        describe(".withinRange") {
            it("returns itself for range 0") {
                assert.that(subject.withinRange(0), equalTo(listOf(subject)))
            }
            it("returns itself and neighbors for range 1") {
                assert.that(subject.withinRange(1).toSet(), equalTo((subject.neighbors() + subject).toSet()))
            }
            it("returns itself, neighbors, and neighbors of neighbors for range 2") {
                assert.that(subject.withinRange(2).toSet(), equalTo(((subject.neighbors().flatMap { it.neighbors() + it } + subject).toSet())))
            }
        }

        describe(".ring") {
            it("returns itself for radius 0") {
                assert.that(subject.ring(0), equalTo(listOf(subject)))
            }
            on("radius 1 with default start direction") {
                val result = subject.ring(1)
                it("returns all neighbors for radius 1") {
                    assert.that(result, allElements(isIn(subject.neighbors())))
                }
                it("returns ring in correct order") {
                    val neighbors = subject.neighbors()
                    assert.that(result, equalTo(neighbors.subList(4, neighbors.lastIndex) + neighbors.subList(0, 4)))
                }
            }
            on("radius 1 with start direction of 2") {
                val result = subject.ring(1, 2)
                it("returns all neighbors for radius 1") {
                    assert.that(result, allElements(isIn(subject.neighbors())))
                }
                it("returns ring in correct order") {
                    val neighbors = subject.neighbors()
                    assert.that(result, equalTo(neighbors.subList(2, neighbors.lastIndex) + neighbors.subList(0, 2)))
                }
            }
            on("radius 2 with default start direction") {
                val result = subject.ring(2)
                it("returns ring in correct order") {
                    assert.that(result, equalTo(listOf(
                        CubeCoordinate(0, 7, -7), CubeCoordinate(1, 7, -8), CubeCoordinate(2, 7, -9),
                        CubeCoordinate(3, 6, -9), CubeCoordinate(4, 5, -9), CubeCoordinate(4, 4, -8),
                        CubeCoordinate(4, 3, -7), CubeCoordinate(3, 3, -6), CubeCoordinate(2, 3, -5),
                        CubeCoordinate(1, 4, -5), CubeCoordinate(0, 5, -5), CubeCoordinate(0, 6, -6)
                    )))
                }
            }
        }

        describe(".toOffsetCoordinate") {
            it("returns correct value for pointy-top odd") {
                val type = OffsetCoordinateType(com.darkgravity.khexgrid.map.HexagonalOrientation.PointyTop, true)
                assert.that(subject.toOffsetCoordinate(type), equalTo(OffsetCoordinate(4, 5, type)))
            }
            it("returns correct value for pointy-top even") {
                val type = OffsetCoordinateType(com.darkgravity.khexgrid.map.HexagonalOrientation.PointyTop, false)
                assert.that(subject.toOffsetCoordinate(type), equalTo(OffsetCoordinate(5, 5, type)))
            }
            it("returns correct value for flat-top odd") {
                val type = OffsetCoordinateType(com.darkgravity.khexgrid.map.HexagonalOrientation.FlatTop, true)
                assert.that(subject.toOffsetCoordinate(type), equalTo(OffsetCoordinate(2, 6, type)))
            }
            it("returns correct value for flat-top even") {
                val type = OffsetCoordinateType(com.darkgravity.khexgrid.map.HexagonalOrientation.FlatTop, false)
                assert.that(subject.toOffsetCoordinate(type), equalTo(OffsetCoordinate(2, 6, type)))
            }
        }

        describe(".toVector2") {
            it("converts the values correctly") {
                assert.that(subject.toVector2(), equalTo(Vector2(subject.x.toFloat(), subject.y.toFloat())))
            }
        }

        describe(".toVector3") {
            it("converts the values correctly") {
                assert.that(subject.toVector3(), equalTo(Vector3(subject.x.toFloat(), subject.y.toFloat(), subject.z.toFloat())))
            }
        }
    }
})
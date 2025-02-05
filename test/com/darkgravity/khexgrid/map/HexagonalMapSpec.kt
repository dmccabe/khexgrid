package com.darkgravity.khexgrid.map

import com.darkgravity.khexgrid.math.OffsetCoordinate
import com.darkgravity.khexgrid.math.OffsetCoordinateType
import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.equalTo
import io.kotest.core.spec.style.DescribeSpec

object HexagonalMapSpec : DescribeSpec({
    lateinit var subject: HexagonalMap

    beforeEach {
        subject = HexagonalMapSharedContext.createMap(5, 5)
    }

    describe("HexagonalMap") {
        describe(".getTerrain") {
            it("returns the correct terrain for a valid location") {
                assert.that(
                    subject.getTerrain(
                        OffsetCoordinate(2, 2, OffsetCoordinateType(subject.orientation)).toCubeCoordinate()
                    ), equalTo(TestTerrain.DESERT)
                )
            }
            it("returns null for an invalid location") {
                assert.that(
                    subject.getTerrain(
                        OffsetCoordinate(-5, -5, OffsetCoordinateType(subject.orientation)).toCubeCoordinate()
                    ), equalTo<Terrain?>(null)
                )
            }
        }

        describe(".isValidLocation") {
            it("correctly identifies valid locations") {
                assert.that(
                    subject.isValidLocation(
                        OffsetCoordinate(0, 0, OffsetCoordinateType(subject.orientation)).toCubeCoordinate()
                    ), equalTo(true)
                )
            }
            it("correctly identifies invalid locations") {
                assert.that(
                    subject.isValidLocation(
                        OffsetCoordinate(10, 10, OffsetCoordinateType(subject.orientation)).toCubeCoordinate()
                    ), equalTo(false)
                )
            }
        }
    }
})
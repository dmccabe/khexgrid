package com.darkgravity.khexgrid.map

import com.darkgravity.khexgrid.math.OffsetCoordinate
import com.darkgravity.khexgrid.math.OffsetCoordinateType
import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.equalTo
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

/**
 * @author Dan McCabe
 */
object HexagonalMapSpec : Spek({
    val subject by memoized { HexagonalMapSharedContext.createMap(5, 5) }

    describe("HexagonalMap") {
        describe(".getTerrain") {
            it("returns the correct terrain for a valid location") {
                assert.that(
                    subject.getTerrain(
                        OffsetCoordinate(2, 2, OffsetCoordinateType(subject.orientation)).toCubeCoordinate()
                    ), equalTo<Terrain>(TestTerrain.DESERT)
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
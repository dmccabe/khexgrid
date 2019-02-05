package com.darkgravity.khexgrid.math

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.equalTo
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

/**
 * @author Dan McCabe
 */
object OffsetCoordinateSpec : Spek( {
    describe("OffsetCoordinate") {
        describe(".toCubeCoordinate") {
            it("returns correct value for pointy-top odd") {
                val type = OffsetCoordinateType(com.darkgravity.khexgrid.map.HexagonalOrientation.PointyTop, true)
                assert.that(OffsetCoordinate(1, 3, type).toCubeCoordinate(), equalTo(CubeCoordinate(0, 3)))
            }
            it("returns correct value for pointy-top even") {
                val type = OffsetCoordinateType(com.darkgravity.khexgrid.map.HexagonalOrientation.PointyTop, false)
                assert.that(OffsetCoordinate(1, 3, type).toCubeCoordinate(), equalTo(CubeCoordinate(-1, 3)))
            }
            it("returns correct value for flat-top odd") {
                val type = OffsetCoordinateType(com.darkgravity.khexgrid.map.HexagonalOrientation.FlatTop, true)
                assert.that(OffsetCoordinate(1, 3, type).toCubeCoordinate(), equalTo(CubeCoordinate(1, 3)))
            }
            it("returns correct value for flat-top even") {
                val type = OffsetCoordinateType(com.darkgravity.khexgrid.map.HexagonalOrientation.FlatTop, false)
                assert.that(OffsetCoordinate(1, 3, type).toCubeCoordinate(), equalTo(CubeCoordinate(1, 2)))
            }
        }
    }
})
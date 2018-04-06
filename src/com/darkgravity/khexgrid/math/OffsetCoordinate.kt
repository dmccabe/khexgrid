package com.darkgravity.khexgrid.math

import com.darkgravity.khexgrid.map.HexagonalOrientation.FlatTop
import com.darkgravity.khexgrid.map.HexagonalOrientation.PointyTop

/**
 * @author Dan McCabe
 */
data class OffsetCoordinate(val x: Int = 0, val y: Int = 0, val offsetType: OffsetCoordinateType = OffsetCoordinateType(PointyTop, true)) {
    fun toCubeCoordinate(): CubeCoordinate =
        CubeCoordinate(
            x - if (offsetType.orientation == PointyTop) adjustCube(y) else 0,
            y - if (offsetType.orientation == FlatTop) adjustCube(x) else 0
        )

    private fun adjustCube(value: Int) = (value + (value and 1) * (if (offsetType.isOdd) -1 else 1)) / 2

    override fun toString(): String = "x = $x, y = $y, orientation = $offsetType.orientation, isOdd = $offsetType.isOdd"
}

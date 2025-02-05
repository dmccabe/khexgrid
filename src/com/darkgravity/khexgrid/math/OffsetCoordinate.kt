package com.darkgravity.khexgrid.math

import com.badlogic.gdx.math.GridPoint2
import com.darkgravity.khexgrid.map.HexagonalOrientation.FlatTop
import com.darkgravity.khexgrid.map.HexagonalOrientation.PointyTop
import ktx.math.ImmutableVector2

data class OffsetCoordinate(
    val x: Int = 0,
    val y: Int = 0,
    val offsetType: OffsetCoordinateType = OffsetCoordinateType(PointyTop, true)
) {
    fun toCubeCoordinate(): CubeCoordinate =
        CubeCoordinate(
            x - if (offsetType.orientation == PointyTop) adjustCube(y) else 0,
            y - if (offsetType.orientation == FlatTop) adjustCube(x) else 0
        )

    fun toVector2(): ImmutableVector2 = ImmutableVector2(x.toFloat(), y.toFloat())

    operator fun plus(coordinate: OffsetCoordinate): OffsetCoordinate =
        OffsetCoordinate(x + coordinate.x, y + coordinate.y, offsetType)

    operator fun plus(point: GridPoint2): OffsetCoordinate =
        OffsetCoordinate(x + point.x, y + point.y, offsetType)

    operator fun minus(coordinate: OffsetCoordinate): OffsetCoordinate =
        OffsetCoordinate(x - coordinate.x, y - coordinate.y, offsetType)

    operator fun minus(point: GridPoint2): OffsetCoordinate =
        OffsetCoordinate(x - point.x, y - point.y, offsetType)

    private fun adjustCube(value: Int) = (value + (value and 1) * (if (offsetType.isOdd) -1 else 1)) / 2

    override fun toString(): String = "x = $x, y = $y, orientation = $offsetType.orientation, isOdd = $offsetType.isOdd"
}

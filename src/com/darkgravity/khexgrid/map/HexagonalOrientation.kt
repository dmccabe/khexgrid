package com.darkgravity.khexgrid.map

import com.darkgravity.khexgrid.math.CubeCoordinate
import com.darkgravity.khexgrid.math.Matrix2
import com.darkgravity.khexgrid.math.OffsetCoordinateType
import ktx.math.ImmutableVector2
import kotlin.math.sqrt

sealed class HexagonalOrientation(
    val forward: Matrix2,
    val backward: Matrix2,
    val startAngle: Float,
    val sizeMultiplier: ImmutableVector2,
    val packedMultiplier: ImmutableVector2
) {
    abstract val id: String

    fun toOffsetCoordinateType(isOdd: Boolean = true): OffsetCoordinateType = OffsetCoordinateType(this, isOdd)

    object PointyTop : HexagonalOrientation(
        forward = Matrix2(sqrt(3f), sqrt(3f) / 2.0f, 0.0f, 3.0f / 2.0f),
        backward = Matrix2(sqrt(3f) / 3.0f, -1.0f / 3.0f, 0.0f, 2.0f / 3.0f),
        startAngle = 0.5f,
        sizeMultiplier = ImmutableVector2(sqrt(3f), 2f),
        packedMultiplier = ImmutableVector2(1f, 3f / 4f)
    ) {
        override val id = "pointy-top"
        val DIRECTION_NE = CubeCoordinate.DIRECTIONS[0]
        val DIRECTION_E = CubeCoordinate.DIRECTIONS[1]
        val DIRECTION_SE = CubeCoordinate.DIRECTIONS[2]
        val DIRECTION_SW = CubeCoordinate.DIRECTIONS[3]
        val DIRECTION_W = CubeCoordinate.DIRECTIONS[4]
        val DIRECTION_NW = CubeCoordinate.DIRECTIONS[5]

        val DIAGONAL_NE = CubeCoordinate.DIAGONALS[0]
        val DIAGONAL_SE = CubeCoordinate.DIAGONALS[1]
        val DIAGONAL_S = CubeCoordinate.DIAGONALS[2]
        val DIAGONAL_SW = CubeCoordinate.DIAGONALS[3]
        val DIAGONAL_NW = CubeCoordinate.DIAGONALS[4]
        val DIAGONAL_N = CubeCoordinate.DIAGONALS[5]
    }

    object FlatTop : HexagonalOrientation(
        forward = Matrix2(3.0f / 2.0f, 0.0f, sqrt(3f) / 2.0f, sqrt(3f)),
        backward = Matrix2(2.0f / 3.0f, 0.0f, -1.0f / 3.0f, sqrt(3f) / 3.0f),
        startAngle = 0.0f,
        sizeMultiplier = ImmutableVector2(2f, sqrt(3f)),
        packedMultiplier = ImmutableVector2(3f / 4f, 1f)
    ) {
        override val id = "flat-top"
        val DIRECTION_NE = CubeCoordinate.DIRECTIONS[0]
        val DIRECTION_SE = CubeCoordinate.DIRECTIONS[1]
        val DIRECTION_S = CubeCoordinate.DIRECTIONS[2]
        val DIRECTION_SW = CubeCoordinate.DIRECTIONS[3]
        val DIRECTION_NW = CubeCoordinate.DIRECTIONS[4]
        val DIRECTION_N = CubeCoordinate.DIRECTIONS[5]

        val DIAGONAL_E = CubeCoordinate.DIAGONALS[0]
        val DIAGONAL_SE = CubeCoordinate.DIAGONALS[1]
        val DIAGONAL_SW = CubeCoordinate.DIAGONALS[2]
        val DIAGONAL_W = CubeCoordinate.DIAGONALS[3]
        val DIAGONAL_NW = CubeCoordinate.DIAGONALS[4]
        val DIAGONAL_NE = CubeCoordinate.DIAGONALS[5]
    }

    companion object {
        fun fromId(id: String): HexagonalOrientation =
            when (id) {
                PointyTop.id -> PointyTop
                FlatTop.id -> FlatTop
                else -> throw IllegalArgumentException("Invalid orientation: $id")
            }

    }
}
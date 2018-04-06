package com.darkgravity.khexgrid.map

import com.darkgravity.khexgrid.math.CubeCoordinate
import com.darkgravity.khexgrid.math.Matrix2
import kotlin.math.sqrt

/**
 * @author Dan McCabe
 */
sealed class HexagonalOrientation(val forward: Matrix2, val backward: Matrix2, val startAngle: Float) {
    object PointyTop : HexagonalOrientation(
        Matrix2(sqrt(3.0).toFloat(), sqrt(3.0).toFloat() / 2.0f, 0.0f, 3.0f / 2.0f),
        Matrix2(sqrt(3.0).toFloat() / 3.0f, -1.0f / 3.0f, 0.0f, 2.0f / 3.0f),
        0.5f
    ) {
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
        Matrix2(3.0f / 2.0f, 0.0f, sqrt(3.0).toFloat() / 2.0f, sqrt(3.0).toFloat()),
        Matrix2(2.0f / 3.0f, 0.0f, -1.0f / 3.0f, sqrt(3.0).toFloat() / 3.0f),
        0.0f
    ) {
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
}
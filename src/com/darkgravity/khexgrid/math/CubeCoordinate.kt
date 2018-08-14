package com.darkgravity.khexgrid.math

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.darkgravity.khexgrid.map.HexagonalOrientation.FlatTop
import com.darkgravity.khexgrid.map.HexagonalOrientation.PointyTop
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * @author Dan McCabe
 */
data class CubeCoordinate(val x: Int = 0, val y: Int = 0, val z: Int = -x - y) {

    init {
        require(x + y + z == 0) { "x, y, and z values must add up to 0" }
    }

    operator fun unaryMinus(): CubeCoordinate = CubeCoordinate(-x, -y, -z)

    operator fun unaryPlus(): CubeCoordinate = this

    operator fun plus(coordinate: CubeCoordinate): CubeCoordinate = add(coordinate)

    operator fun minus(coordinate: CubeCoordinate): CubeCoordinate = subtract(coordinate)

    operator fun times(scalar: Int): CubeCoordinate = multiply(scalar)

    fun add(coordinate: CubeCoordinate): CubeCoordinate = CubeCoordinate(x + coordinate.x, y + coordinate.y, z + coordinate.z)

    fun subtract(coordinate: CubeCoordinate): CubeCoordinate = CubeCoordinate(x - coordinate.x, y - coordinate.y, z - coordinate.z)

    fun multiply(scalar: Int): CubeCoordinate = CubeCoordinate(x * scalar, y * scalar, z * scalar)

    fun neighbor(direction: Int): CubeCoordinate = add(DIRECTIONS[direction])

    fun neighbors(): List<CubeCoordinate> = DIRECTIONS.indices.map { neighbor(it) }

    fun diagonalNeighbor(direction: Int): CubeCoordinate = add(DIAGONALS[direction])

    fun diagonalNeighbors(): List<CubeCoordinate> = DIAGONALS.indices.map { diagonalNeighbor(it) }

    fun length(): Int = ((x.abs() + y.abs() + z.abs()) / 2f).roundToInt()

    fun distance(coordinate: CubeCoordinate): Int = subtract(coordinate).length()

    fun withinRange(range: Int): List<CubeCoordinate> =
        (-range..range).flatMap { dx ->
            (max(-range, -dx - range)..min(range, -dx + range)).map { dy ->
                CubeCoordinate(x + dx, y + dy, z - dx - dy)
            }
        }

    fun withinRange(range: IntRange): List<CubeCoordinate> = withinRange(range.last) - withinRange(range.first)

    fun linearInterpolation(coordinate: CubeCoordinate, percent: Float): Vector3 =
        toVector3() + (coordinate - this).toVector3() * percent

    fun lineDraw(coordinate: CubeCoordinate): List<CubeCoordinate> {
        val distance = distance(coordinate)
        return if (distance > 0) {
            (0..distance).map { linearInterpolation(coordinate, it / distance.toFloat()).toCubeCoordinate() }
        } else {
            listOf()
        }
    }

    fun lineDrawMidpoints(coordinate: CubeCoordinate): List<CubeCoordinate> {
        val coordinates = lineDraw(coordinate)
        return if (coordinates.size > 1) coordinates.subList(1, coordinates.lastIndex) else coordinates
    }

    fun ring(radius: Int, startDirection: Int = 4): List<CubeCoordinate> {
        if (radius == 0) return listOf(this)

        // the directions constants are ordered such that for any direction, in order to move around in a ring, you need
        // to move in the direction four places before that to go in the correct direction. for example, in terms of pointy
        // top, index 4 represents the West direction, so to start a ring there, you need to go Northeast, which is located
        // at index 0.
        var firstSearchDirection = (startDirection - 4) % 6
        if (firstSearchDirection < 0) firstSearchDirection += 6

        var current = this + DIRECTIONS[startDirection] * radius
        val orderedDirections = (firstSearchDirection until 6).map { it } + (0 until firstSearchDirection).map { it }
        return orderedDirections.flatMap { i ->
            (0 until radius).map { _ -> current.also { current = current.neighbor(i) } }
        }
    }

    fun spiralRing(radius: Int, startDirection: Int = 4) = listOf(this) + (1 until radius).flatMap { ring(radius, startDirection) }

    fun toOffsetCoordinate(offsetType: OffsetCoordinateType): OffsetCoordinate =
        OffsetCoordinate(
            x + if (offsetType.orientation == PointyTop) adjustOffset(y, offsetType.isOdd) else 0,
            y + if (offsetType.orientation == FlatTop) adjustOffset(x, offsetType.isOdd) else 0,
            offsetType
        )

    private fun adjustOffset(value: Int, isOdd: Boolean) = (value + (value and 1) * (if (isOdd) -1 else 1)) / 2

    fun toVector2(): Vector2 = Vector2(x.toFloat(), y.toFloat())

    fun toVector3(): Vector3 = Vector3(x.toFloat(), y.toFloat(), z.toFloat())

    companion object {
        var DIRECTIONS = listOf(CubeCoordinate(1, 0, -1), CubeCoordinate(1, -1, 0), CubeCoordinate(0, -1, 1),
            CubeCoordinate(-1, 0, 1), CubeCoordinate(-1, 1, 0), CubeCoordinate(0, 1, -1))

        var DIAGONALS = listOf(CubeCoordinate(2, -1, -1), CubeCoordinate(1, -2, 1), CubeCoordinate(-1, -1, 2),
            CubeCoordinate(-2, 1, 1), CubeCoordinate(-1, 2, -1), CubeCoordinate(1, 1, -2))
    }
}
package com.darkgravity.khexgrid.map

import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Vector3
import com.darkgravity.khexgrid.math.*
import ktx.math.ImmutableVector2
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * @author Dan McCabe
 */
class HexagonalLayout(
    val orientation: HexagonalOrientation,
    val position: ImmutableVector2,
    private val tileRadius: ImmutableVector2
) {

    /**
     * Represents the size of a single hexagon in pixels
     */
    val tileSize: ImmutableVector2 = tileRadius * orientation.sizeMultiplier

    /**
     * Represents the distance between two hexagonal centers when packed in a grid
     */
    val packedTileSize: ImmutableVector2 = tileSize * orientation.packedMultiplier

    fun resize(size: ImmutableVector2): HexagonalLayout = HexagonalLayout(orientation, position, size)

    fun toPixel(coordinate: CubeCoordinate): GridPoint2 = toVector(coordinate).toGridPoint2()

    fun toHex(pixel: GridPoint2): CubeCoordinate {
        val location = orientation.backward * ((pixel - position) / tileRadius)
        return Vector3(location.x, location.y, -location.x - location.y).toCubeCoordinate()
    }

    fun polygonCorners(coordinate: CubeCoordinate): List<ImmutableVector2> {
        val center = toVector(coordinate)
        return (0 until 6).map { (center + cornerOffset(it)) }
    }

    fun polygonVertices(coordinate: CubeCoordinate): FloatArray {
        var nextIndex = 0
        return FloatArray(12).also {
            for (point in polygonCorners(coordinate)) {
                it[nextIndex++] = point.x
                it[nextIndex++] = point.y
            }
        }
    }

    private fun toVector(coordinate: CubeCoordinate): ImmutableVector2 =
        position + (orientation.forward * coordinate.toVector2()) * tileRadius

    private fun cornerOffset(corner: Int): ImmutableVector2 {
        val angle = 2.0 * PI * (corner + orientation.startAngle).toDouble() / 6.0
        return ImmutableVector2(cos(angle).toFloat(), sin(angle).toFloat()) * tileRadius
    }
}

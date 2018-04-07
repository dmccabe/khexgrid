package com.darkgravity.khexgrid.map

import com.badlogic.gdx.math.Vector2
import com.darkgravity.khexgrid.math.CubeCoordinate
import com.darkgravity.khexgrid.math.OffsetCoordinate
import com.darkgravity.khexgrid.math.OffsetCoordinateType

/**
 * @author Dan McCabe
 */
object HexagonalMapSharedContext {
    fun createMap(rows: Int, columns: Int, terrains: List<Terrain> = TestTerrain.values().toList()) : HexagonalMap {
        val orientation = HexagonalOrientation.PointyTop
        val offsetType = OffsetCoordinateType(orientation)
        val tiles: Map<CubeCoordinate, HexagonalTile> = (0 until rows).flatMap { row ->
            (0 until columns).map { column ->
                val coordinate = OffsetCoordinate(column, row, offsetType).toCubeCoordinate()
                coordinate to HexagonalTile(coordinate, terrains[column % terrains.size])
            }
        }.toMap()
        return HexagonalMap(HexagonalLayout(orientation, Vector2(), Vector2(50f, 50f)), columns, rows, tiles)
    }
}

enum class TestTerrain(override val isMoveObstacle: Boolean, override val isViewObstacle: Boolean, override val movementCost: Int) : Terrain {
    GRASS(false, false, 1),
    FOREST(false, false, 2),
    DESERT(false, false, 2),
    WATER(true, false, 3),
    MOUNTAIN(false, true, 3)
}
package com.darkgravity.khexgrid.map

import com.darkgravity.khexgrid.math.CubeCoordinate

/**
 * @author Dan McCabe
 */
data class HexagonalTile(val location: CubeCoordinate, val terrain: Terrain) {
    val isMoveObstacle get() = terrain.isMoveObstacle
    val isViewObstacle get() = terrain.isViewObstacle
    val movementCost get() = terrain.movementCost
}

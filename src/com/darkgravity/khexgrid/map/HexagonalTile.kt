package com.darkgravity.khexgrid.map

import com.darkgravity.khexgrid.math.CubeCoordinate

data class HexagonalTile(val location: CubeCoordinate, val terrain: Terrain) {
    val isMoveObstacle: Boolean get() = terrain.isMoveObstacle
    val isViewObstacle: Boolean get() = terrain.isViewObstacle
    val movementCost: Int get() = terrain.movementCost
}

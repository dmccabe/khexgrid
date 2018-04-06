package com.darkgravity.khexgrid.map

/**
 * @author Dan McCabe
 */
interface Terrain {
    val isMoveObstacle: Boolean
    val isViewObstacle: Boolean
    val movementCost: Int
}
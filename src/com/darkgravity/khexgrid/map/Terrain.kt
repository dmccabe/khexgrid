package com.darkgravity.khexgrid.map

interface Terrain {
    val id: String
    val isMoveObstacle: Boolean
    val isViewObstacle: Boolean
    val movementCost: Int
}
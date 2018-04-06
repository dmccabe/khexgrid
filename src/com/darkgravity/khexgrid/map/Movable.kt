package com.darkgravity.khexgrid.map

import com.darkgravity.khexgrid.math.CubeCoordinate

/**
 * @author Dan McCabe
 */
interface Movable {
    var location: CubeCoordinate
    val movementRange: Int
    val canMove: Boolean
}

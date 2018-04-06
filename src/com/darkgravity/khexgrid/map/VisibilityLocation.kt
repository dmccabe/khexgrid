package com.darkgravity.khexgrid.map

import com.darkgravity.khexgrid.math.CubeCoordinate

/**
 * @author Dan McCabe
 */
interface VisibilityLocation : Locatable {
    override val location: CubeCoordinate
    val visibleRange: Int
}

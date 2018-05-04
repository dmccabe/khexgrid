package com.darkgravity.khexgrid.map

import com.darkgravity.khexgrid.math.CubeCoordinate

/**
 * @author Dan McCabe
 */
interface HexagonalMapListener {
    fun tilesChanged(map: HexagonalMap, locations: List<CubeCoordinate>)
}
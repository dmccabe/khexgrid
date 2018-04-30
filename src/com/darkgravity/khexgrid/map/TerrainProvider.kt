package com.darkgravity.khexgrid.map

/**
 * @author Dan McCabe
 */
interface TerrainProvider {
    operator fun get(id: String): Terrain
}
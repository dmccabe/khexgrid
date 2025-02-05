package com.darkgravity.khexgrid.map

interface TerrainProvider {
    operator fun get(id: String): Terrain
}
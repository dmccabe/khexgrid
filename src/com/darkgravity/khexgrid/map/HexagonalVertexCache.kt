package com.darkgravity.khexgrid.map

import com.darkgravity.khexgrid.math.CubeCoordinate

/**
 * @author Dan McCabe
 */
class HexagonalVertexCache(private val map: HexagonalMap) {
    private val locationVertices = map.tiles.keys.associate { it to CachedEntry(map.polygonVertices(it)) }.toMutableMap()

    operator fun get(tile: HexagonalTile) = this[tile.location]

    operator fun get(coordinate: CubeCoordinate) = locationVertices.getOrPut(coordinate) { CachedEntry(map.polygonVertices(coordinate))}

    class CachedEntry(val vertices: FloatArray) {
        private val xCoordinates = vertices.filterIndexed { index, _ -> index % 2 == 0 }
        private val yCoordinates = vertices.filterIndexed { index, _ -> index % 2 == 1 }

        val minX = xCoordinates.minOrNull() ?: 0f
        val minY = yCoordinates.minOrNull() ?: 0f
        val maxX = xCoordinates.maxOrNull() ?: 0f
        val maxY = yCoordinates.maxOrNull() ?: 0f
        val width = maxX - minX
        val height = maxY - minY
        val midX = minX + width / 2f
        val midY = minY + height / 2f
    }
}
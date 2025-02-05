package com.darkgravity.khexgrid.map

import com.darkgravity.khexgrid.math.CubeCoordinate

class HexagonalVertexCache(private val map: HexagonalMap) {
    private val locationVertices =
        map.tiles.keys.associateWith { CachedEntry(map.polygonVertices(it)) }.toMutableMap()

    operator fun get(tile: HexagonalTile): CachedEntry = this[tile.location]

    operator fun get(coordinate: CubeCoordinate): CachedEntry =
        locationVertices.getOrPut(coordinate) { CachedEntry(map.polygonVertices(coordinate)) }

    class CachedEntry(val vertices: FloatArray) {
        private val xCoordinates = vertices.filterIndexed { index, _ -> index % 2 == 0 }
        private val yCoordinates = vertices.filterIndexed { index, _ -> index % 2 == 1 }

        val minX: Float = xCoordinates.minOrNull() ?: 0f
        val minY: Float = yCoordinates.minOrNull() ?: 0f
        val maxX: Float = xCoordinates.maxOrNull() ?: 0f
        val maxY: Float = yCoordinates.maxOrNull() ?: 0f
        val width: Float = maxX - minX
        val height: Float = maxY - minY
        val midX: Float = minX + width / 2f
        val midY: Float = minY + height / 2f
    }
}
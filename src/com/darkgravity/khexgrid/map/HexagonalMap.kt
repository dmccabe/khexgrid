package com.darkgravity.khexgrid.map

import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Vector2
import com.darkgravity.khexgrid.math.CubeCoordinate
import com.darkgravity.khexgrid.math.OffsetCoordinateType

/**
 * @author Dan McCabe
 */
class HexagonalMap(val layout: HexagonalLayout, tiles: Map<CubeCoordinate, HexagonalTile>) {

    val mutableTiles = tiles.toMutableMap()
    val tiles: Map<CubeCoordinate, HexagonalTile> = mutableTiles

    val orientation get() = layout.orientation
    val position get() = layout.position
    val tileSize get() = layout.tileSize
    val tileCount get() = tiles.size

    val minOffsetX = getOffsetLocations().minBy { it.x }?.x ?: 0
    val minOfsetY = getOffsetLocations().minBy { it.y }?.y ?: 0
    val maxOffsetX = getOffsetLocations().maxBy { it.x }?.x ?: 0
    val maxOffsetY = getOffsetLocations().maxBy { it.y }?.y ?: 0

    val width get() = maxOffsetX - minOffsetX
    val height get() = maxOffsetY - minOfsetY
    val size get() = GridPoint2(width, height)

    val worldWidth get() = width * tileSize.x
    val worldHeight get() = height * tileSize.y
    val worldSize get() = Vector2(worldWidth, worldHeight)

    val movableTileMap: Map<CubeCoordinate, HexagonalTile> by lazy {
        tiles.filterNot { it.value.isMoveObstacle }
    }
    val movableLocations get() = movableTileMap.keys.toList()
    val movableTiles get() = movableTileMap.values.toList()

    val leftEdge get() = getOffsetLine(minOffsetX, null)
    val topEdge get() = getOffsetLine(minOfsetY, null)
    val rightEdge get() = getOffsetLine(maxOffsetX, null)
    val bottomEdge get() = getOffsetLine(maxOffsetY, null)

    val leftMovableEdge = leftEdge.intersect(movableTileMap.keys).toList()
    val topMovableEdge = topEdge.intersect(movableTileMap.keys).toList()
    val rightMovableEdge = rightEdge.intersect(movableTileMap.keys).toList()
    val bottomMovableEdge = bottomEdge.intersect(movableTileMap.keys).toList()

    operator fun set(coordinate: CubeCoordinate, terrain: Terrain) {
        mutableTiles[coordinate] = HexagonalTile(coordinate, terrain)
    }

    fun resizeLayout(size: Vector2): HexagonalMap = HexagonalMap(layout.resize(size), tiles)

    fun toPixel(coordinate: CubeCoordinate): GridPoint2 = layout.toPixel(coordinate)

    fun toHex(pixel: GridPoint2): CubeCoordinate = layout.toHex(pixel)

    fun polygonCorners(coordinate: CubeCoordinate): List<Vector2> = layout.polygonCorners(coordinate)

    fun polygonVertices(coordinate: CubeCoordinate): FloatArray = layout.polygonVertices(coordinate)

    fun getTerrain(coordinate: CubeCoordinate): Terrain? = tiles[coordinate]?.terrain

    fun getReachableLocations(movable: Movable): List<CubeCoordinate> = getReachableLocations(movable, movable.movementRange)

    fun getReachableLocations(movable: Movable, range: Int): List<CubeCoordinate> = getReachableLocations(movable.location, range)

    fun getReachableLocations(coordinate: CubeCoordinate, range: Int): List<CubeCoordinate> =
        tiles[coordinate]?.let { source -> getReachableTiles(source, range).map { it.location } } ?: listOf()

    fun getReachableTiles(source: HexagonalTile, range: Int): List<HexagonalTile> {
        val reachable = mutableMapOf(source to 0)
        var fringe = listOf(source)
        while (fringe.isNotEmpty()) {
            fringe = fringe.flatMap{ tile ->
                val validLocations = tile.location.neighbors().mapNotNull { tiles[it] }.filter { !it.isMoveObstacle }
                val locationCosts = validLocations.associate { it to (reachable[tile] ?: 0) + it.movementCost }
                val validCosts = locationCosts.filter { (it.key !in reachable || it.value < reachable[it.key]!!) && it.value <= range }
                reachable += validCosts
                validCosts.keys
            }
        }
        return reachable.keys.toList()
    }

    fun getVisibleLocations(visibilityLocation: VisibilityLocation): List<CubeCoordinate> =
        getVisibleLocations(visibilityLocation.location, visibilityLocation.visibleRange)

    fun getVisibleLocations(coordinate: CubeCoordinate, range: Int): List<CubeCoordinate> =
        tiles[coordinate]?.let { source -> getVisibleTiles(source, range).map { it.location } } ?: listOf()

    fun getVisibleTiles(source: HexagonalTile, range: Int): List<HexagonalTile> {
        val visible = source.location.withinRange(range) + listOf(source.location)
        return visible.mapNotNull { tiles[it] }.filter { tile ->
            tile.location.lineDrawMidpoints(source.location).mapNotNull { tiles[it] }.none { it.isViewObstacle }
        }
    }

    private fun getOffsetLocations() = tiles.map { it.key.toOffsetCoordinate(OffsetCoordinateType(orientation)) }

    private fun getOffsetLine(targetX: Int?, targetY: Int?): List<CubeCoordinate> {
        val offsetLocations = getOffsetLocations()
        return offsetLocations.filter {
            (targetX == null || it.x == targetX) && (targetY == null || it.y == targetY)
        }.map { it.toCubeCoordinate() }
    }

    fun isMovableLocation(coordinate: CubeCoordinate): Boolean = !(tiles[coordinate]?.isMoveObstacle ?: true)

    fun isValidLocation(coordinate: CubeCoordinate): Boolean = coordinate in tiles

    fun getMovementCost(coordinate: CubeCoordinate): Int = tiles[coordinate]?.movementCost ?: Int.MAX_VALUE
}

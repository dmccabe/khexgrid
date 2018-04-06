package com.darkgravity.khexgrid.map

import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Vector2
import com.darkgravity.khexgrid.math.CubeCoordinate
import com.darkgravity.khexgrid.math.OffsetCoordinate
import com.darkgravity.khexgrid.math.OffsetCoordinateType

/**
 * @author Dan McCabe
 */
class HexagonalMap(val layout: HexagonalLayout, val width: Int, val height: Int, val tiles: Map<CubeCoordinate, HexagonalTile>) {

    val orientation get() = layout.orientation
    val tileCount get() = tiles.size

    val position get() = layout.position
    val size get() = layout.size

    val pixelWidth get() = width * size.x.toInt()
    val pixelHeight get() = height * size.y.toInt()
    val pixelSize get() = GridPoint2(pixelWidth, pixelHeight)

    val movableTileMap: Map<CubeCoordinate, HexagonalTile> by lazy {
        tiles.filterNot { it.value.isMoveObstacle }
    }
    val movableLocations get() = movableTileMap.keys.toList()
    val movableTiles get() = movableTileMap.values.toList()

    val leftEdge get() = getOffsetTiles { locations -> Pair<Int?, Int?>(locations.minBy { it.x }?.x ?: 0, null) }
    val rightEdge get() = getOffsetTiles { locations -> Pair<Int?, Int?>(locations.maxBy { it.x }?.x ?: 0, null) }
    val topEdge get() = getOffsetTiles { locations -> Pair<Int?, Int?>(null, locations.minBy { it.y }?.y ?: 0) }
    val bottomEdge get() = getOffsetTiles { locations -> Pair<Int?, Int?>(null, locations.maxBy { it.y }?.y ?: 0) }

    val leftMovableEdge = leftEdge.intersect(movableTileMap.keys).toList()
    val rightMovableEdge = rightEdge.intersect(movableTileMap.keys).toList()
    val topMovableEdge = topEdge.intersect(movableTileMap.keys).toList()
    val bottomMovableEdge = bottomEdge.intersect(movableTileMap.keys).toList()

    fun resizeLayout(size: Vector2): HexagonalMap = HexagonalMap(layout.resize(size), width, height, tiles)

    fun toPixel(coordinate: CubeCoordinate): GridPoint2 = layout.toPixel(coordinate)

    fun toHex(pixel: GridPoint2): CubeCoordinate = layout.toHex(pixel)

    fun polygonCorners(coordinate: CubeCoordinate): List<Vector2> = layout.polygonCorners(coordinate)

    fun polygonVertices(coordinate: CubeCoordinate): FloatArray = layout.polygonVertices(coordinate)

    fun getTerrain(coordinate: CubeCoordinate): Terrain? = tiles[coordinate]?.terrain

    fun getReachableLocations(movable: Movable): List<CubeCoordinate> = getReachableLocations(movable, movable.movementRange)

    fun getReachableLocations(movable: Movable, range: Int): List<CubeCoordinate> = getReachableLocations(movable.location, range)

    fun getReachableLocations(coordinate: CubeCoordinate, range: Int) =
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

    fun getVisibleLocations(visibilityLocation: VisibilityLocation) =
        getVisibleLocations(visibilityLocation.location, visibilityLocation.visibleRange)

    fun getVisibleLocations(coordinate: CubeCoordinate, range: Int) =
        tiles[coordinate]?.let { source -> getVisibleTiles(source, range).map { it.location } } ?: listOf()

    fun getVisibleTiles(source: HexagonalTile, range: Int): List<HexagonalTile> {
        val visible = source.location.withinRange(range) + listOf(source.location)
        return visible.mapNotNull { tiles[it] }.filter { tile ->
            tile.location.lineDrawMidpoints(source.location).mapNotNull { tiles[it] }.none { it.isViewObstacle }
        }
    }

    private fun getOffsetTiles(block: (List<OffsetCoordinate>) -> Pair<Int?, Int?>): List<CubeCoordinate> {
        val offsetLocations = tiles.map { it.key.toOffsetCoordinate(OffsetCoordinateType(orientation)) }
        val target = block(offsetLocations)
        return offsetLocations.filter {
            (target.first == null || it.x == target.first) && (target.second == null || it.y == target.second)
        }.map { it.toCubeCoordinate() }
    }

    fun isMovableLocation(coordinate: CubeCoordinate) = !(tiles[coordinate]?.isMoveObstacle ?: true)

    fun isValidLocation(coordinate: CubeCoordinate) = coordinate in tiles

    fun getMovementCost(coordinate: CubeCoordinate) = tiles[coordinate]?.movementCost ?: Int.MAX_VALUE
}

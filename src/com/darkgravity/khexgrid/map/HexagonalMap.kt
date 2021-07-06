package com.darkgravity.khexgrid.map

import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Vector2
import com.darkgravity.khexgrid.delegate.CacheRegistry
import com.darkgravity.khexgrid.delegate.cache
import com.darkgravity.khexgrid.math.CubeCoordinate
import com.darkgravity.khexgrid.math.OffsetCoordinateType
import com.darkgravity.khexgrid.observer.Observable
import com.darkgravity.khexgrid.observer.ObservableSubject

/**
 * @author Dan McCabe
 */
class HexagonalMap(val layout: HexagonalLayout, tiles: Map<CubeCoordinate, HexagonalTile>) :
    Observable<HexagonalMapListener> by ObservableSubject() {

    private val mutableTiles = tiles.toMutableMap()
    val tiles: Map<CubeCoordinate, HexagonalTile> = mutableTiles
    val locations: List<CubeCoordinate> get() = tiles.keys.toList()

    val orientation get() = layout.orientation
    val position get() = layout.position

    val tileCount get() = tiles.size
    val tileSize get() = layout.tileSize
    val packedTileSize get() = layout.packedTileSize

    val minOffsetX: Int by lazy { getOffsetLocations().minByOrNull { it.x }?.x ?: 0 }
    val minOfsetY: Int by lazy { getOffsetLocations().minByOrNull { it.y }?.y ?: 0 }
    val maxOffsetX: Int by lazy { getOffsetLocations().maxByOrNull { it.x }?.x ?: 0 }
    val maxOffsetY: Int by lazy { getOffsetLocations().maxByOrNull { it.y }?.y ?: 0 }

    val width: Int get() = maxOffsetX - minOffsetX + 1
    val height: Int get() = maxOffsetY - minOfsetY + 1
    val aspectRatio: Float get() = width.toFloat() / height.toFloat()
    val size: GridPoint2 get() = GridPoint2(width, height)

    val worldWidth get() = width * packedTileSize.x
    val worldHeight get() = height * packedTileSize.y
    val worldAspectRatio get() = worldWidth / worldHeight
    val worldSize get() = Vector2(worldWidth, worldHeight)

    val rows: List<List<CubeCoordinate>> get() = getOffsetLocations().groupBy { it.y }.map { it.value.map { it.toCubeCoordinate() } }
    val columns: List<List<CubeCoordinate>> get() = getOffsetLocations().groupBy { it.x }.map { it.value.map { it.toCubeCoordinate() } }

    val leftEdge: List<CubeCoordinate> get() = rows.map { locations -> checkNotNull(locations.minByOrNull { it.x }) }
    val bottomEdge: List<CubeCoordinate> get() = columns.map { locations -> checkNotNull(locations.minByOrNull { it.y }) }
    val rightEdge: List<CubeCoordinate> get() = rows.map { locations -> checkNotNull(locations.maxByOrNull { it.x }) }
    val topEdge: List<CubeCoordinate> get() = columns.map { locations -> checkNotNull(locations.maxByOrNull { it.y }) }

    private val movableCache = CacheRegistry()
    val movableTiles: Map<CubeCoordinate, HexagonalTile> by cache(movableCache) { calculateMovableTiles() }
    val movableLocations: List<CubeCoordinate> get() = this.movableTiles.keys.toList()
    val leftMovableEdge: List<CubeCoordinate> by cache(movableCache) { calculateMovableEdge(leftEdge) }
    val topMovableEdge: List<CubeCoordinate> by cache(movableCache) { calculateMovableEdge(topEdge) }
    val rightMovableEdge: List<CubeCoordinate> by cache(movableCache) { calculateMovableEdge(rightEdge) }
    val bottomMovableEdge: List<CubeCoordinate> by cache(movableCache) { calculateMovableEdge(bottomEdge) }

    private fun calculateMovableTiles() = tiles.filterNot { it.value.isMoveObstacle }

    private fun calculateMovableEdge(edge: List<CubeCoordinate>) = edge.intersect(movableLocations).toList()

    private fun tilesChanged(locations: List<CubeCoordinate>) {
        movableCache.invalidate()
        notify { it.tilesChanged(this, locations) }
    }

    operator fun get(coordinate: CubeCoordinate) : Terrain? = tiles[coordinate]?.terrain

    operator fun set(coordinate: CubeCoordinate, terrain: Terrain) {
        mutableTiles[coordinate] = HexagonalTile(coordinate, terrain)
        tilesChanged(listOf(coordinate))
    }

    operator fun plusAssign(tile: HexagonalTile) {
        mutableTiles += tile.location to tile
        tilesChanged(listOf(tile.location))
    }

    operator fun plusAssign(tiles: List<HexagonalTile>) {
        val locations = tiles.map { it.location to it }
        mutableTiles += locations
        tilesChanged(locations.map { it.first })
    }

    operator fun minusAssign(coordinate: CubeCoordinate) {
        mutableTiles -= coordinate
        tilesChanged(listOf(coordinate))
    }

    operator fun minusAssign(coordinates: List<CubeCoordinate>) {
        mutableTiles -= coordinates
        tilesChanged(coordinates)
    }

    fun resizeLayout(size: Vector2): HexagonalMap = HexagonalMap(layout.resize(size), tiles)

    fun toPixel(coordinate: CubeCoordinate): GridPoint2 = layout.toPixel(coordinate)

    fun toHex(pixel: GridPoint2): CubeCoordinate = layout.toHex(pixel)

    fun polygonCorners(coordinate: CubeCoordinate): List<Vector2> = layout.polygonCorners(coordinate)

    fun polygonVertices(coordinate: CubeCoordinate): FloatArray = layout.polygonVertices(coordinate)

    fun getTerrain(coordinate: CubeCoordinate): Terrain? = tiles[coordinate]?.terrain

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

    fun getVisibleLocations(coordinate: CubeCoordinate, range: Int): List<CubeCoordinate> =
        tiles[coordinate]?.let { source -> getVisibleTiles(source, range).map { it.location } } ?: listOf()

    fun getVisibleTiles(source: HexagonalTile, range: Int): List<HexagonalTile> {
        val visible = source.location.withinRange(range) + listOf(source.location)
        return visible.mapNotNull { tiles[it] }.filter { tile ->
            tile.location.lineToMidpoints(source.location).asSequence().mapNotNull { tiles[it] }.none { it.isViewObstacle }
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

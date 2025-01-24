# KHexGrid
Kotlin implementation of hexagonal grids using LibGDX.

Allows for grids of any size and shape - square, rectangular, triangular, or even irregular. Supports both pointy-top and flat-top hexes.

Based on Red Blob Games' [fantastic guide](https://www.redblobgames.com/grids/hexagons/) on hexagonal grids.

## Overview
Locations in the map are represented as `CubeCoordinate`s. A `CubeCoordinate` has many handy operations built-in, but knows nothing of the external map structure. These objects are immutable by design, but can readily be converted to several other types, such as `OffsetCoordinate`s or vectors.

A `HexagonalMap` consists of a series of `CubeCoordinate`s mapped to `HexagonalTile`s at those locations. The corresponding `HexagonalLayout` and `HexagonalOrientation` can be used to control the position and appearance of the tiles. A `HexagonalTile` is composed of a location and a `Terrain`, which defines rules for tiles such as movement cost and visibility.

A map can be drawn using a `LayeredRenderer`. The renderer will draw any `Layer`s provided. It will also handle culling the tiles that are drawn to the camera's viewable area to reduce unnecessary drawing. Some simple `Layer` implementations are also included.


## Example Usage
Create map
```kotlin
    private val rows = 1
    private val columns = 1
    private val orientation = HexagonalOrientation.PointyTop
    private val layout = HexagonalLayout(orientation, ImmutableVector2(30f,30f), ImmutableVector2(24f,24f))

    data object ExampleTerrain : Terrain {
        override val id = "example"
        override val isMoveObstacle = false
        override val isViewObstacle = false
        override val movementCost = 0
    }

    private val tiles: Map<CubeCoordinate, HexagonalTile> = (0 until rows).flatMap { row ->
        (0 until columns).map { column ->
            val coordinate = OffsetCoordinate(column, row, OffsetCoordinateType(orientation)).toCubeCoordinate()
            coordinate to HexagonalTile(coordinate, ExampleTerrain)
        }
    }.toMap()

    private val map = HexagonalMap(layout, tiles)
```

Find all valid locations within 10 tiles of a location (`CubeCoordinate`)
```kotlin
    location.withinRange(10).filter { map.isValidLocation(it) }
```
    
Find all locations that are 3 tiles away from a location
```kotlin
    location.ring(3)    
```

Find locations that can be moved to at a movement cost of 5 or less
```kotlin
    map.getReachableLocations(location, 5)
```
    
Find visible locations (i.e. tiles with unobstructed view) within 7 tiles of location
```kotlin
    map.getVisibleLocations(location, 7)
```
    
Draw map to screen
```kotlin
    private val pbatch = PolygonSpriteBatch()
    
    private val exampleView = TerrainView(
        ExampleTerrain,
        TextureRegion(image),
        Color.RED
    )
    private val terrainViews: Map<Terrain, TerrainView> = mapOf(
        ExampleTerrain to exampleView
    )
    private val shapeRenderer = ShapeRenderer()
    private val camera = OrthographicCamera()

    private val hexRenderer = HexagonalRenderer(map, ImmutableVector2(0f, 0f))
    private val renderer = LayeredRenderer(map, listOf(
        TerrainLayer(map, hexRenderer, terrainViews),
        TileOutlineLayer(hexRenderer, shapeRenderer)
    ), camera)
```
and then in render
```kotlin
    renderer.render(pbatch)
```
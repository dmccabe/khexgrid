# KHexGrid
Kotlin implementation of hexagonal grids using LibGDX

Based on Red Blob Games' [fantastic guide](https://www.redblobgames.com/grids/hexagons/) on hexagonal grids.

## Overview
Locations in the map are represented as `CubeCoordinate`s. A `CubeCoordinate` has many handy operations built-in, but knows nothing of the external map structure. These objects are immutable by design, but can readily be converted to several other types, such as `OffsetCoordinate`s or vectors.

A `HexagonalMap` consists of a series of `CubeCoordinate`s mapped to `HexagonalTile`s at those locations. The corresponding `HexagonalLayout` and `HexagonalOrientation` can be used to control the position and appearance of the tiles.

A map can be drawn using a `LayeredRenderer`. The renderer will draw any `Layer`s provided. It will also handle culling the tiles that are drawn to the camera's viewable area to reduce unnecessary drawing. Some simple `Layer` implementations are also included.


## Example Usage
Create map

    val tiles = (0 until rows).flatMap { row ->
        (0 until columns).map { column ->
            val coordinate = OffsetCoordinate(column, row, OffsetCoordinateType(HexagonalOrientation.POINTY_TOP)).toCubeCoordinate()
            coordinate to HexagonalTile(coordinate, getTerrain(column, row))
        }
    }.toMap()
    val map = HexagonalMap(layout, columns, rows, tiles)

Find movable locations within 3 tiles of location (`CubeCoordinate`)

    map.getReachableLocations(location, 3)
    
Find visible locations (i.e. tiles with unobstructed view) within 5 tiles of location

    map.getVisibleLocations(location, 5)
    
Find all valid locations within 10 tiles of location

    location.withinRange(10).filter { map.isValidLocation(it) }
    
Find all tiles that are 3 tiles away from location

    location.ring(3)
    
Draw map to screen

    val hexRenderer = HexagonalRenderer(map, Vector2(32, 32))
    val renderer = LayeredRenderer(map, listOf(
        TerrainLayer(map, hexRenderer, terrainViews),
        TileOutlineLayer(hexRenderer, shapeRenderer)
    ), camera)
    renderer.render(batch)
    
More documentation to come!

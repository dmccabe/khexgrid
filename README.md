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
    val tiles = (0 until rows).flatMap { row ->
        (0 until columns).map { column ->
            val coordinate = OffsetCoordinate(column, row, OffsetCoordinateType(HexagonalOrientation.POINTY_TOP)).toCubeCoordinate()
            coordinate to HexagonalTile(coordinate, generateTerrain(column, row))
        }
    }.toMap()
    val map = HexagonalMap(layout, tiles)
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
    val hexRenderer = HexagonalRenderer(map, Vector2(32, 32))
    val renderer = LayeredRenderer(map, listOf(
        TerrainLayer(map, hexRenderer, terrainViews),
        TileOutlineLayer(hexRenderer, shapeRenderer)
    ), camera)
    renderer.render(batch)
```

## LibGDX Example

Sample screen implementation using LibGDX

```kotlin
package com.darkgravity.khexgrid.sample

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Disposable
import com.darkgravity.khexgrid.map.*
import com.darkgravity.khexgrid.math.CubeCoordinate
import com.darkgravity.khexgrid.math.OffsetCoordinate
import com.darkgravity.khexgrid.math.OffsetCoordinateType
import com.darkgravity.khexgrid.render.HexagonalRenderer
import com.darkgravity.khexgrid.render.LayeredHexagonalRenderer
import com.darkgravity.khexgrid.render.TerrainLayer
import com.darkgravity.khexgrid.render.TileOutlineLayer
import ktx.math.ImmutableVector2

class HexGridScreen(rowCount: Int, columnCount: Int) : Screen {
    private val layout = HexagonalLayout(
        orientation = HexagonalOrientation.PointyTop,
        position = ImmutableVector2(30f, 30f),
        tileRadius = ImmutableVector2(24f, 24f)
    )

    private val tiles: Map<CubeCoordinate, HexagonalTile> =
        (0 until rowCount).flatMap { row ->
            (0 until columnCount).map { column ->
                createTile(OffsetCoordinate(column, row, OffsetCoordinateType(layout.orientation)))
            }
        }.associateBy { it.location }

    private val map = HexagonalMap(layout, tiles)

    private val terrainViews = SampleTerrain.entries.associateWith {
        TerrainView(it, TextureRegion(createColorPixmap(it.color, 1, 1).toTexture()), it.color)
    }

    private val batch = PolygonSpriteBatch()
    private val shapeRenderer = ShapeRenderer()
    private val camera = OrthographicCamera().also { it.setToOrtho(false) }

    private val hexRenderer = HexagonalRenderer(map, hexagonSpriteSize = ImmutableVector2(1f, 1f))
    private val renderer = LayeredHexagonalRenderer(
        map,
        listOf(
            TerrainLayer(map, hexRenderer, terrainViews),
            TileOutlineLayer(hexRenderer, shapeRenderer)
        ),
        camera
    )

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0.75f, 0.75f, 0.75f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        camera.update()
        batch.projectionMatrix = camera.combined
        renderer.render(batch)
    }

    override fun show() {
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun hide() {
    }

    override fun dispose() {
        batch.dispose()
        shapeRenderer.dispose()
        terrainViews.values.map { it.texture }.forEach { it.texture.dispose() }
    }

    private fun createColorPixmap(color: Color, width: Int, height: Int): Pixmap =
        Pixmap(width, height, Pixmap.Format.RGBA8888).apply {
            setColor(color)
            fill()
        }

    private fun Pixmap.toTexture(): Texture =
        use { Texture(it) }

    fun <T : Disposable, U> T.use(block: (T) -> U): U =
        block(this).also { this.dispose() }

    private fun createTile(coordinate: OffsetCoordinate): HexagonalTile =
        HexagonalTile(coordinate.toCubeCoordinate(), calculateTerrain(coordinate))

    private fun calculateTerrain(coordinate: OffsetCoordinate): Terrain =
        SampleTerrain.entries.random()

    enum class SampleTerrain(val color: Color) : Terrain {
        PLAINS(Color.CHARTREUSE) {
            override val isViewObstacle: Boolean = false
            override val isMoveObstacle: Boolean = false
            override val movementCost: Int = 1
        },
        RIVER(Color.CYAN) {
            override val isViewObstacle: Boolean = false
            override val isMoveObstacle: Boolean = true
            override val movementCost: Int = 1
        },
        MOUNTAIN(Color.BROWN) {
            override val isViewObstacle: Boolean = true
            override val isMoveObstacle: Boolean = false
            override val movementCost: Int = 2
        };

        override val id = name
    }
}
```
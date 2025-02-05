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

/**
 * Sample implementation of a LibGDX screen using khexgrid
 */
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
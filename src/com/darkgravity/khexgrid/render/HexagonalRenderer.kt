package com.darkgravity.khexgrid.render

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import com.darkgravity.khexgrid.map.HexagonalMap
import com.darkgravity.khexgrid.map.HexagonalVertexCache
import com.darkgravity.khexgrid.math.CubeCoordinate
import com.darkgravity.khexgrid.math.ceilToInt
import ktx.math.ImmutableVector2

class HexagonalRenderer(map: HexagonalMap, private val hexagonSpriteSize: ImmutableVector2) {

    private val drawVertices = map.polygonVertices(CubeCoordinate())
    private val cache = HexagonalVertexCache(map)

    init {
        val minX = drawVertices.filterIndexed { index: Int, _: Float -> index % 2 == 0 }.minOrNull() ?: 0f
        val minY = drawVertices.filterIndexed { index: Int, _: Float -> index % 2 == 1 }.minOrNull() ?: 0f
        for (i in drawVertices.indices) {
            drawVertices[i] = drawVertices[i] - if (i % 2 == 0) minX else minY
        }
    }

    fun getVertexEntry(location: CubeCoordinate): HexagonalVertexCache.CachedEntry = cache[location]

    fun renderTexture(batch: PolygonSpriteBatch, texture: Texture, location: CubeCoordinate) {
        renderTexture(batch, TextureRegion(texture), location)
    }

    fun renderTexture(batch: PolygonSpriteBatch, textureRegion: TextureRegion, location: CubeCoordinate) {
        // draw colored tiles using the polygon vertices so they have the right shape
        if (textureRegion.regionWidth == 1 || textureRegion.regionHeight == 1) {
            renderPolygonTexture(batch, textureRegion, location)
            // draw sprite tiles as a normal sprite since they already have the right shape
        } else {
            renderSpriteTexture(batch, textureRegion, location)
        }
    }

    fun renderPolygonTexture(batch: PolygonSpriteBatch, textureRegion: TextureRegion, location: CubeCoordinate) {
        // the PolygonRegion is used to cut a polygonal shape out of a texture region, which means the draw vertices
        // need to be at the origin to work correctly
        with(PolygonSprite(PolygonRegion(textureRegion, drawVertices, TRIANGLE_DRAW_ORDER))) {
            val entry = cache[location]
            setPosition(entry.minX, entry.minY)
            draw(batch)
        }
    }

    fun renderSpriteTexture(batch: PolygonSpriteBatch, textureRegion: TextureRegion, location: CubeCoordinate) {
        with(Sprite(textureRegion)) {
            val entry = cache[location]
            val scale = kotlin.math.max(entry.width / hexagonSpriteSize.x, entry.height / hexagonSpriteSize.y)
            setCenter(entry.midX, entry.midY)
            setScale(scale)
            draw(batch)
        }
    }

    fun renderOutline(renderer: ShapeRenderer, location: CubeCoordinate, thickness: Float = 1f) {
        val vertices = cache[location].vertices
        var i = 0
        val vertexCount = vertices.size
        while (i < vertexCount) {
            val isLast = i + 2 >= vertexCount
            val x1 = vertices[i]
            val y1 = vertices[i + 1]
            val x2 = if (isLast) vertices[0] else vertices[i + 2]
            val y2 = if (isLast) vertices[1] else vertices[i + 3]

            val angle = MathUtils.atan2(y2 - y1, x2 - x1)
            val xAdjust = (MathUtils.cos(angle) * thickness / 4f).ceilToInt()
            val yAdjust = (MathUtils.sin(angle) * thickness / 4f).ceilToInt()

            renderer.rectLine(x1 - xAdjust, y1 - yAdjust, x2 + xAdjust, y2 + yAdjust, thickness)
            i += 2
        }
    }

    companion object {
        private val TRIANGLE_DRAW_ORDER = shortArrayOf(0, 1, 2, 0, 2, 3, 0, 3, 4, 0, 4, 5)
    }
}

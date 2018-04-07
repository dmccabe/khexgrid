package com.darkgravity.khexgrid.render

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.PolygonRegion
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.darkgravity.khexgrid.map.HexagonalMap
import com.darkgravity.khexgrid.map.HexagonalVertexCache
import com.darkgravity.khexgrid.math.CubeCoordinate
import kotlin.math.max

/**
 * @author Dan McCabe
 */
class HexagonalRenderer(map: HexagonalMap, private val hexagonSpriteSize: Vector2) {

    private val cache = HexagonalVertexCache(map)

    fun renderTexture(batch: PolygonSpriteBatch, texture: Texture, location: CubeCoordinate) =
        renderTexture(batch, TextureRegion(texture), location)

    fun renderTexture(batch: PolygonSpriteBatch, textureRegion: TextureRegion, location: CubeCoordinate) {
        val entry = cache[location]!!

        // draw colored tiles using the polygon vertices so they have the right shape
        if (textureRegion.regionWidth == 1 || textureRegion.regionHeight == 1) {
            with(batch) {
                color = Color.WHITE
                draw(PolygonRegion(textureRegion, entry.vertices, TRIANGLE_DRAW_ORDER), 0f, 0f)
            }

        // draw sprite tiles as a normal sprite since they already have the right shape
        } else {
            with(Sprite(textureRegion)) {
                val scale = max(entry.width / hexagonSpriteSize.x, entry.height / hexagonSpriteSize.y)
                setCenter(entry.midX, entry.midY)
                setScale(scale)
                draw(batch)
            }
        }
    }

    fun renderOutline(renderer: ShapeRenderer, location: CubeCoordinate, thickness: Int = 1) {
        val vertices = cache[location]!!.vertices
        var i = 0
        val vertexCount = vertices.size
        while (i < vertexCount) {
            val isLast = i + 2 >= vertexCount
            val x1 = vertices[i]
            val y1 = vertices[i + 1]
            val x2 = if (isLast) vertices[0] else vertices[i + 2]
            val y2 = if (isLast) vertices[1] else vertices[i + 3]
            renderer.rectLine(x1, y1, x2, y2, thickness.toFloat())
            i += 2
        }
    }

    companion object {
        private val TRIANGLE_DRAW_ORDER = shortArrayOf(0, 1, 2, 0, 2, 3, 0, 3, 4, 0, 4, 5)
    }
}

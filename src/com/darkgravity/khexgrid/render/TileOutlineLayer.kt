package com.darkgravity.khexgrid.render

import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.darkgravity.khexgrid.map.HexagonalTile

/**
 * @author Dan McCabe
 */
class TileOutlineLayer(private val hexagonalRenderer: HexagonalRenderer, shapeRenderer: ShapeRenderer) : ShapeRendererLayer(shapeRenderer) {
    override fun render(batch: PolygonSpriteBatch, tiles: Collection<HexagonalTile>) =
        tiles.forEach {
            hexagonalRenderer.renderOutline(shapeRenderer, it.location, 2)
        }
}

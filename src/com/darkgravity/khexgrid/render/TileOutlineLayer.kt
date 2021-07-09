package com.darkgravity.khexgrid.render

import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.darkgravity.khexgrid.map.HexagonalTile

/**
 * @author Dan McCabe
 */
class TileOutlineLayer(
    private val hexagonalRenderer: HexagonalRenderer, shapeRenderer: ShapeRenderer,
    private val filterBlock: ((HexagonalTile) -> Boolean)? = null
) : ShapeRendererLayer(shapeRenderer) {

    override fun handleRender(batch: PolygonSpriteBatch, tiles: Collection<HexagonalTile>) {
        (filterBlock?.let { tiles.filter(it) } ?: tiles)
            .forEach {
                hexagonalRenderer.renderOutline(shapeRenderer, it.location, 5f)
            }
    }
}

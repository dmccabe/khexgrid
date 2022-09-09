package com.darkgravity.khexgrid.render

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.darkgravity.khexgrid.map.HexagonalTile

/**
 * @author Dan McCabe
 */
class TileOutlineLayer(
    private val hexagonalRenderer: HexagonalRenderer,
    shapeRenderer: ShapeRenderer,
    private val color: Color = Color.BLACK,
    private val filterBlock: ((HexagonalTile) -> Boolean)? = null
) : ShapeRendererLayer(shapeRenderer) {

    override fun handleRender(batch: PolygonSpriteBatch, tiles: Collection<HexagonalTile>) {
        val originalColor = shapeRenderer.color
        shapeRenderer.color = this.color
        (filterBlock?.let { tiles.filter(it) } ?: tiles)
            .forEach {
                hexagonalRenderer.renderOutline(shapeRenderer, it.location, 5f)
            }
        shapeRenderer.color = originalColor
    }
}

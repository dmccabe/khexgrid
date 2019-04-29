package com.darkgravity.khexgrid.render

import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.darkgravity.khexgrid.map.HexagonalTile

/**
 * @author Dan McCabe
 */
abstract class LayerAdapter : Layer {
    protected open fun preRender(batch: PolygonSpriteBatch) {}
    protected open fun postRender(batch: PolygonSpriteBatch) {}

    override fun render(batch: PolygonSpriteBatch, tiles: Collection<HexagonalTile>) {
        batch.begin()
        preRender(batch)
        handleRender(batch, tiles)
        postRender(batch)
        batch.end()
    }

    protected abstract fun handleRender(batch: PolygonSpriteBatch, tiles: Collection<HexagonalTile>)
}

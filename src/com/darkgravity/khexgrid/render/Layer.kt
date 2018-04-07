package com.darkgravity.khexgrid.render

import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.darkgravity.khexgrid.map.HexagonalTile

/**
 * @author Dan McCabe
 */
interface Layer {
    fun use(batch: PolygonSpriteBatch, action: () -> Unit)
    fun begin(batch: PolygonSpriteBatch)
    fun render(batch: PolygonSpriteBatch, tiles: Collection<HexagonalTile>)
    fun end(batch: PolygonSpriteBatch)
}

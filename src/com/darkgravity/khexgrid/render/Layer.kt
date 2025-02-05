package com.darkgravity.khexgrid.render

import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.darkgravity.khexgrid.map.HexagonalTile

interface Layer {
    fun render(batch: PolygonSpriteBatch, tiles: Collection<HexagonalTile>)
}

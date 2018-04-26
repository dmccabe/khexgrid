package com.darkgravity.khexgrid.render

import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.darkgravity.khexgrid.map.HexagonalMap
import com.darkgravity.khexgrid.map.HexagonalTile
import com.darkgravity.khexgrid.map.Terrain
import com.darkgravity.khexgrid.map.TerrainView

/**
 * @author Dan McCabe
 */
class TerrainLayer(private val map: HexagonalMap,
                   private val hexagonalRenderer: HexagonalRenderer,
                   private val terrainViews: Map<Terrain, TerrainView>) : LayerAdapter() {
    override fun render(batch: PolygonSpriteBatch, tiles: Collection<HexagonalTile>) =
        tiles.forEach {
            val texture = terrainViews[map.getTerrain(it.location)]?.texture ?: return@forEach
            hexagonalRenderer.renderTexture(batch, texture, it.location)
        }
}

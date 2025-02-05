package com.darkgravity.khexgrid.render

import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.darkgravity.khexgrid.map.HexagonalMap
import com.darkgravity.khexgrid.map.HexagonalTile
import com.darkgravity.khexgrid.map.Terrain
import com.darkgravity.khexgrid.map.TerrainView

class TerrainLayer<T : Terrain>(
    private val map: HexagonalMap,
    private val hexagonalRenderer: HexagonalRenderer,
    private val terrainViews: Map<T, TerrainView>
) : LayerAdapter() {
    override fun handleRender(batch: PolygonSpriteBatch, tiles: Collection<HexagonalTile>) {
        tiles
            .mapNotNull { tile ->
                terrainViews[map.getTerrain(tile.location)]?.texture?.let { tile.location to it }
            }
            .forEach { (coordinate, texture) ->
                hexagonalRenderer.renderTexture(batch, texture, coordinate)
            }
    }
}

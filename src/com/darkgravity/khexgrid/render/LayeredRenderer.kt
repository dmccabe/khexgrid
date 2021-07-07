package com.darkgravity.khexgrid.render

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.math.Rectangle
import com.darkgravity.khexgrid.map.HexagonalMap
import com.darkgravity.khexgrid.map.HexagonalTile
import com.darkgravity.khexgrid.math.minus
import com.darkgravity.khexgrid.math.toVector2
import ktx.math.div
import ktx.math.toMutable

/**
 * @author Dan McCabe
 */
class LayeredRenderer(private val map: HexagonalMap, private val layers: List<Layer>, private val camera: OrthographicCamera) {

    private var culledTiles: Collection<HexagonalTile> = cullTiles(map.tiles.values, camera.viewArea)

    fun render(batch: PolygonSpriteBatch) {
        layers.forEach { it.render(batch, culledTiles) }
    }

    private fun cullTiles(tiles: Collection<HexagonalTile>, cullingArea: Rectangle): Collection<HexagonalTile> {
        // adding extra buffer around tile size to make drawing smooth while panning camera
        val tileSize = map.tileSize * 5f
        val halfTileSize = tileSize / 2f
        val area = Rectangle(0f, 0f, tileSize.x, tileSize.y)
        return tiles.filter {
            area.setPosition((map.toPixel(it.location).toVector2() - halfTileSize).toMutable())
            cullingArea.overlaps(area)
        }
    }

    fun update() {
        culledTiles = cullTiles(map.tiles.values, camera.viewArea)
    }
}

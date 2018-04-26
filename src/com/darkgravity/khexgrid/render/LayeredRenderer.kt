package com.darkgravity.khexgrid.render

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.math.Rectangle
import com.darkgravity.khexgrid.map.HexagonalMap
import com.darkgravity.khexgrid.map.HexagonalTile
import com.darkgravity.khexgrid.math.minus
import com.darkgravity.khexgrid.math.times
import com.darkgravity.khexgrid.math.toVector2

/**
 * @author Dan McCabe
 */
class LayeredRenderer(private val map: HexagonalMap, private val layers: List<Layer>, private val camera: OrthographicCamera) {

    private var culledTiles: Collection<HexagonalTile> = cullTiles(map.tiles.values, camera.viewArea)

    fun render(batch: PolygonSpriteBatch) {
        batch.begin()
        layers.forEach { layer ->
            layer.use(batch) { layer.render(batch, culledTiles) }
        }
        batch.end()
    }

    private fun cullTiles(tiles: Collection<HexagonalTile>, cullingArea: Rectangle): Collection<HexagonalTile> {
        // using size slightly bigger than tile size to avoid partially shown tiles from getting culled
        val size = map.tileSize * 1.5f
        val area = Rectangle(0f, 0f, size.x * 2f + map.tileSize.x, size.y * 2f + map.tileSize.y)
        return tiles.filter {
            area.setPosition(map.toPixel(it.location).toVector2() - size)
            cullingArea.overlaps(area)
        }
    }

    fun update() {
        culledTiles = cullTiles(map.tiles.values, camera.viewArea)
    }
}

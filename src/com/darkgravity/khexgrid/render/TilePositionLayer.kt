package com.darkgravity.khexgrid.render

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.darkgravity.khexgrid.map.HexagonalMap
import com.darkgravity.khexgrid.map.HexagonalTile
import com.darkgravity.khexgrid.math.CubeCoordinate
import com.darkgravity.khexgrid.math.OffsetCoordinateType
import ktx.math.ImmutableVector2

class TilePositionLayer(private val map: HexagonalMap, private val font: BitmapFont) : LayerAdapter() {

    private val textPositions = map.tiles.keys.associateWith { coordinate ->
        val layout = calculateTextLayout(coordinate)
        val pixel = map.toPixel(coordinate)
        ImmutableVector2(pixel.x - layout.width / 2, pixel.y + layout.height / 2)
    }

    override fun handleRender(batch: PolygonSpriteBatch, tiles: Collection<HexagonalTile>) {
        tiles.forEach {
            val position = textPositions[it.location] ?: ImmutableVector2.ZERO
            font.draw(batch, coordinateText(it.location), position.x, position.y)
        }
    }

    private fun coordinateText(cubeCoordinate: CubeCoordinate): String {
        val offsetCoordinate = cubeCoordinate.toOffsetCoordinate(OffsetCoordinateType(map.orientation))
        return "${offsetCoordinate.x},${offsetCoordinate.y}"
    }

    private fun calculateTextLayout(coordinate: CubeCoordinate): GlyphLayout =
        GlyphLayout(font, coordinateText(coordinate))
}

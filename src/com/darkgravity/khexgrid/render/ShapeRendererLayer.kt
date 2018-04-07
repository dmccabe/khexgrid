package com.darkgravity.khexgrid.render

import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

/**
 * @author Dan McCabe
 */
open class ShapeRendererLayer(val shapeRenderer: ShapeRenderer) : LayerAdapter() {
    override fun begin(batch: PolygonSpriteBatch) {
        shapeRenderer.projectionMatrix = batch.projectionMatrix
        shapeRenderer.updateMatrices()
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
    }

    override fun end(batch: PolygonSpriteBatch) = shapeRenderer.end()
}

package com.darkgravity.khexgrid.render

import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

/**
 * @author Dan McCabe
 */
abstract class ShapeRendererLayer(val shapeRenderer: ShapeRenderer) : LayerAdapter() {
    override fun preRender(batch: PolygonSpriteBatch) {
        shapeRenderer.projectionMatrix = batch.projectionMatrix
        shapeRenderer.updateMatrices()
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
    }

    override fun postRender(batch: PolygonSpriteBatch) = shapeRenderer.end()
}

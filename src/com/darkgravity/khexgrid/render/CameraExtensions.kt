package com.darkgravity.khexgrid.render

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.darkgravity.khexgrid.math.toVector2
import ktx.math.div
import ktx.math.minus
import ktx.math.times

val OrthographicCamera.viewArea: Rectangle get() {
    val zoomedViewportSize = Vector2(viewportWidth, viewportHeight) * zoom
    val position = position.toVector2() - zoomedViewportSize / 2f
    return Rectangle(position.x, position.y, zoomedViewportSize.x, zoomedViewportSize.y)
}
package com.darkgravity.khexgrid.math

import ktx.math.ImmutableVector2

/**
 * @author Dan McCabe
 */
class Matrix2(m00: Float, m10: Float, m01: Float, m11: Float) {

    private val values = FloatArray(4)

    init {
        values[M00] = m00
        values[M10] = m10
        values[M01] = m01
        values[M11] = m11
    }

    operator fun get(position: Int): Float = values[position]

    fun multiply(vector: ImmutableVector2): ImmutableVector2 =
        ImmutableVector2(
            values[M00] * vector.x + values[M10] * vector.y,
            values[M01] * vector.x + values[M11] * vector.y
        )

    operator fun times(vector: ImmutableVector2): ImmutableVector2 = multiply(vector)

    companion object {
        const val M00 = 0
        const val M10 = 1
        const val M01 = 2
        const val M11 = 3
    }
}

package com.darkgravity.khexgrid.math

import com.badlogic.gdx.math.*
import ktx.math.ImmutableVector2
import kotlin.math.abs
import kotlin.math.roundToInt

fun Float.abs(): Float = abs(this)
fun Float.floor(): Float = kotlin.math.floor(this)
fun Float.floorToInt(): Int = MathUtils.floor(this)
fun Float.round(): Float = kotlin.math.round(this)
fun Float.ceil(): Float = kotlin.math.ceil(this)
fun Float.ceilToInt(): Int = MathUtils.ceil(this)

operator fun GridPoint2.plus(vector: ImmutableVector2): ImmutableVector2 = ImmutableVector2(x + vector.x, y + vector.y)
operator fun GridPoint2.minus(vector: ImmutableVector2): ImmutableVector2 = ImmutableVector2(x - vector.x, y - vector.y)
fun GridPoint2.toVector2(): ImmutableVector2 = ImmutableVector2(x.toFloat(), y.toFloat())

fun GridPoint3.toCubeCoordinate(): CubeCoordinate = CubeCoordinate(x, y, z)

operator fun ImmutableVector2.div(vector: ImmutableVector2): ImmutableVector2 =
    this * ImmutableVector2(1f / vector.x, 1f / vector.y)

operator fun ImmutableVector2.div(number: Number): ImmutableVector2 =
    this * ImmutableVector2(1f / number.toFloat(), 1f / number.toFloat())

fun ImmutableVector2.toGridPoint2(): GridPoint2 = GridPoint2(x.roundToInt(), y.roundToInt())
fun ImmutableVector2.toCubeCoordinate(): CubeCoordinate = CubeCoordinate(x.roundToInt(), y.roundToInt())
fun ImmutableVector2.toVector3(): Vector3 = Vector3(x, y, 0f)

operator fun Vector3.plus(vector: Vector3): Vector3 = cpy().add(vector)
operator fun Vector3.plus(gridPoint: GridPoint3): Vector3 =
    cpy().add(gridPoint.x.toFloat(), gridPoint.y.toFloat(), gridPoint.z.toFloat())

operator fun Vector3.minus(vector: Vector3): Vector3 = cpy().sub(vector)
operator fun Vector3.minus(gridPoint: GridPoint3): Vector3 =
    cpy().sub(gridPoint.x.toFloat(), gridPoint.y.toFloat(), gridPoint.z.toFloat())

operator fun Vector3.times(vector: Vector3): Vector3 = cpy().scl(vector)
operator fun Vector3.times(number: Number): Vector3 = cpy().scl(number.toFloat(), number.toFloat(), number.toFloat())
operator fun Vector3.div(vector: Vector3): Vector3 = cpy().scl(1f / vector.x, 1f / vector.y, 1f / vector.z)
operator fun Vector3.div(number: Number): Vector3 =
    cpy().scl(1f / number.toFloat(), 1f / number.toFloat(), 1f / number.toFloat())

operator fun Vector3.plusAssign(vector: Vector3) {
    add(vector)
}

operator fun Vector3.minusAssign(vector: Vector3) {
    sub(vector)
}

operator fun Vector3.timesAssign(vector: Vector3) {
    scl(vector)
}

operator fun Vector3.timesAssign(number: Number) {
    scl(number.toFloat(), number.toFloat(), number.toFloat())
}

operator fun Vector3.divAssign(vector: Vector3) {
    scl(1 / vector.x, 1 / vector.y, 1 / vector.z)
}

operator fun Vector3.divAssign(number: Number) {
    scl(1 / number.toFloat(), 1 / number.toFloat(), 1 / number.toFloat())
}

fun Vector3.abs(): Vector3 = Vector3(abs(x), abs(y), abs(z))
fun Vector3.round(): GridPoint3 = GridPoint3(x.roundToInt(), y.roundToInt(), z.roundToInt())
fun Vector3.toVector2(): Vector2 = Vector2(x, y)

fun Vector3.toCubeCoordinate(): CubeCoordinate {
    val rounded = round()
    val diff = (this - rounded).abs()
    if (diff.x > diff.y && diff.x > diff.z) {
        rounded.x = -rounded.y - rounded.z
    } else if (diff.y > diff.z) {
        rounded.y = -rounded.x - rounded.z
    } else {
        rounded.z = -rounded.x - rounded.y
    }
    return rounded.toCubeCoordinate()
}
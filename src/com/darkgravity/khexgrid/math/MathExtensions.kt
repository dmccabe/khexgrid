package com.darkgravity.khexgrid.math

import com.badlogic.gdx.math.*
import kotlin.math.*

/**
 * @author Dan McCabe
 */
fun Int.abs(): Int = abs(this)
fun Int.clamp(min: Int, max: Int): Int = MathUtils.clamp(this, min, max)
fun Int.clamp(range: IntRange): Int = MathUtils.clamp(this, range.start, range.endInclusive)
fun IntRange.random(): Int = MathUtils.random(start, endInclusive)

fun Long.abs(): Long = abs(this)
fun Long.clamp(min: Long, max: Long): Long = MathUtils.clamp(this, min, max)
fun Long.clamp(range: LongRange): Long = MathUtils.clamp(this, range.start, range.endInclusive)
fun LongRange.random(): Long = MathUtils.random(start, endInclusive)

fun Float.abs(): Float = abs(this)
fun Float.floor(): Float = floor(this)
fun Float.floorToInt(): Int = MathUtils.floor(this)
fun Float.round(): Float = round(this)
fun Float.ceil(): Float = ceil(this)
fun Float.ceilToInt(): Int = MathUtils.ceil(this)
fun Float.clamp(min: Float, max: Float): Float = MathUtils.clamp(this, min, max)
fun Float.clamp(range: ClosedFloatingPointRange<Float>): Float = MathUtils.clamp(this, range.start, range.endInclusive)
fun ClosedFloatingPointRange<Float>.random(): Float = MathUtils.random(start, endInclusive)

fun Double.abs(): Double = abs(this)
fun Double.floor(): Double = floor(this)
fun Double.round(): Double = round(this)
fun Double.ceil(): Double = ceil(this)
fun Double.clamp(min: Double, max: Double): Double = MathUtils.clamp(this, min, max)
fun Double.clamp(range: ClosedFloatingPointRange<Double>): Double = MathUtils.clamp(this, range.start, range.endInclusive)

operator fun GridPoint2.plus(vector: Vector2): Vector2 = Vector2(x + vector.x, y + vector.y)

operator fun GridPoint2.minus(vector: Vector2): Vector2 = Vector2(x - vector.x, y - vector.y)

fun GridPoint2.toVector2(): Vector2 = Vector2(x.toFloat(), y.toFloat())

fun GridPoint3.toCubeCoordinate(): CubeCoordinate = CubeCoordinate(x, y, z)


operator fun Vector2.plus(vector: Vector2): Vector2 = cpy().add(vector)

operator fun Vector2.minus(vector: Vector2): Vector2 = cpy().sub(vector)

operator fun Vector2.times(vector: Vector2): Vector2 = cpy().scl(vector)

operator fun Vector2.times(number: Number): Vector2 = cpy().scl(number.toFloat(), number.toFloat())

operator fun Vector2.div(vector: Vector2): Vector2 = cpy().scl(1f / vector.x, 1f / vector.y)

operator fun Vector2.div(number: Number): Vector2 = cpy().scl(1f / number.toFloat(), 1f / number.toFloat())

operator fun Vector2.plusAssign(vector: Vector2) {
    add(vector)
}

operator fun Vector2.minusAssign(vector: Vector2) {
    sub(vector)
}

operator fun Vector2.timesAssign(vector: Vector2) {
    scl(vector)
}

operator fun Vector2.timesAssign(number: Number) {
    scl(number.toFloat(), number.toFloat())
}

operator fun Vector2.divAssign(vector: Vector2) {
    scl(1 / vector.x, 1 / vector.y)
}

operator fun Vector2.divAssign(number: Number) {
    scl(1 / number.toFloat(), 1 / number.toFloat())
}

fun Vector2.toGridPoint2(): GridPoint2 = GridPoint2(x.roundToInt(), y.roundToInt())

fun Vector2.toCubeCoordinate(): CubeCoordinate = CubeCoordinate(x.roundToInt(), y.roundToInt())

fun Vector2.toVector3(): Vector3 = Vector3(x, y, 0f)


operator fun Vector3.plus(vector: Vector3): Vector3 = cpy().add(vector)

operator fun Vector3.plus(gridPoint: GridPoint3): Vector3 = cpy().add(gridPoint.x.toFloat(), gridPoint.y.toFloat(), gridPoint.z.toFloat())

operator fun Vector3.minus(vector: Vector3): Vector3 = cpy().sub(vector)

operator fun Vector3.minus(gridPoint: GridPoint3): Vector3 = cpy().sub(gridPoint.x.toFloat(), gridPoint.y.toFloat(), gridPoint.z.toFloat())

operator fun Vector3.times(vector: Vector3): Vector3 = cpy().scl(vector)

operator fun Vector3.times(number: Number): Vector3 = cpy().scl(number.toFloat(), number.toFloat(), number.toFloat())

operator fun Vector3.div(vector: Vector3): Vector3 = cpy().scl(1f / vector.x, 1f / vector.y, 1f / vector.z)

operator fun Vector3.div(number: Number): Vector3 = cpy().scl(1f / number.toFloat(), 1f / number.toFloat(), 1f / number.toFloat())

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

fun Vector3.abs(): Vector3 = Vector3(x.abs(), y.abs(), z.abs())

fun Vector3.round(): GridPoint3 = GridPoint3(x.roundToInt(), y.roundToInt(), z.roundToInt())

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

fun Vector3.toVector2(): Vector2 = Vector2(x, y)

val Rectangle.minX: Float get() = x

val Rectangle.minY: Float get() = y

val Rectangle.maxX: Float get() = x + width

val Rectangle.maxY: Float get() = y + height

operator fun Rectangle.times(scalar: Float): Rectangle = Rectangle(x * scalar, y * scalar, width * scalar, height * scalar)

operator fun Rectangle.div(scalar: Float): Rectangle = Rectangle(x / scalar, y / scalar, width / scalar, height / scalar)

operator fun Rectangle.times(vector: Vector2): Rectangle = Rectangle(x * vector.x, y * vector.y, width * vector.x, height * vector.y)

operator fun Rectangle.div(vector: Vector2): Rectangle = Rectangle(x / vector.x, y / vector.y, width / vector.x, height / vector.y)
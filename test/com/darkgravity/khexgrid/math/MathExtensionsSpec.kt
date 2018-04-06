package com.darkgravity.khexgrid.math

import com.badlogic.gdx.math.*
import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.throws
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

/**
 * @author Dan McCabe
 */
object MathExtensionsSpec : Spek( {
    describe("GridPoint2") {
        val point = GridPoint2(2, 3)

        describe(".plus") {
            it("returns same values when given identity") {
                assert.that(point + Vector2(0f, 0f), equalTo(Vector2(2f, 3f)))
            }

            it("adds values when given non-identity") {
                assert.that(point + Vector2(4f, 5f), equalTo(Vector2(6f, 8f)))
            }
        }

        describe(".minus") {
            it("returns same values when given identity") {
                assert.that(point - Vector2(0f, 0f), equalTo(Vector2(2f, 3f)))
            }

            it("subtracts values when given non-identity") {
                assert.that(point - Vector2(1f, 7f), equalTo(Vector2(1f, -4f)))
            }
        }

        describe(".toVector2") {
            it("returns equivalent Vector") {
                assert.that(point.toVector2(), equalTo(Vector2(2f, 3f)))
            }
        }
    }

    describe("GridPoint3") {
        describe(".toCubeCoordinate") {
            it("throws exception on invalid values") {
                assert.that( { GridPoint3(2, 3, 4).toCubeCoordinate() }, throws<IllegalArgumentException>())
            }

            it("copies values correctly on valid values") {
                assert.that(GridPoint3(2, 3, -5).toCubeCoordinate(), equalTo(CubeCoordinate(2, 3, -5)))
            }
        }
    }

    describe("Vector2") {
        val vector = Vector2()
        beforeEachTest { vector.set(2f, 3f) }

        describe(".plus") {
            on("when given identity") {
                val result = vector + Vector2(0f, 0f)

                it("returns same values") {
                    assert.that(result, equalTo(vector))
                }

                it("does not modify original") {
                    assert.that(vector, equalTo(Vector2(2f, 3f)))
                }
            }

            on("when given non-identity") {
                val result = vector + Vector2(4f, 5f)

                it("returns added values") {
                    assert.that(result, equalTo(Vector2(6f, 8f)))
                }

                it("does not modify original") {
                    assert.that(vector, equalTo(Vector2(2f, 3f)))
                }
            }
        }

        describe(".minus") {
            on("when given identity") {
                val result = vector - Vector2(0f, 0f)

                it("returns same values") {
                    assert.that(result, equalTo(vector))
                }

                it("does not modify original") {
                    assert.that(vector, equalTo(Vector2(2f, 3f)))
                }
            }

            on("when given non-identity") {
                val result = vector - Vector2(1f, 7f)

                it("returns added values") {
                    assert.that(result, equalTo(Vector2(1f, -4f)))
                }

                it("does not modify original") {
                    assert.that(vector, equalTo(Vector2(2f, 3f)))
                }
            }
        }

        describe(".times") {
            context("Number") {
                on("when given identity") {
                    val result = vector * 1f

                    it("returns same values") {
                        assert.that(result, equalTo(vector))
                    }

                    it("does not modify original") {
                        assert.that(vector, equalTo(Vector2(2f, 3f)))
                    }
                }

                on("when given non-identity") {
                    val result = vector * 5f

                    it("returns added values") {
                        assert.that(result, equalTo(Vector2(10f, 15f)))
                    }

                    it("does not modify original") {
                        assert.that(vector, equalTo(Vector2(2f, 3f)))
                    }
                }
            }

            context("Vector") {
                on("when given identity") {
                    val result = vector * Vector2(1f, 1f)

                    it("returns same values") {
                        assert.that(result, equalTo(vector))
                    }

                    it("does not modify original") {
                        assert.that(vector, equalTo(Vector2(2f, 3f)))
                    }
                }

                on("when given non-identity") {
                    val result = vector * Vector2(4f, 5f)

                    it("returns added values") {
                        assert.that(result, equalTo(Vector2(8f, 15f)))
                    }

                    it("does not modify original") {
                        assert.that(vector, equalTo(Vector2(2f, 3f)))
                    }
                }
            }
        }

        describe(".div") {
            context("Number") {
                on("when given identity") {
                    val result = vector / 1f

                    it("returns same values") {
                        assert.that(result, equalTo(vector))
                    }

                    it("does not modify original") {
                        assert.that(vector, equalTo(Vector2(2f, 3f)))
                    }
                }

                on("when given non-identity") {
                    val result = vector / 5f

                    it("returns added values") {
                        assert.that(result, equalTo(Vector2(0.4f, 0.6f)))
                    }

                    it("does not modify original") {
                        assert.that(vector, equalTo(Vector2(2f, 3f)))
                    }
                }
            }

            context("Vector") {
                on("when given identity") {
                    val result = vector / Vector2(1f, 1f)

                    it("returns same values") {
                        assert.that(result, equalTo(vector))
                    }

                    it("does not modify original") {
                        assert.that(vector, equalTo(Vector2(2f, 3f)))
                    }
                }

                on("when given non-identity") {
                    val result = vector / Vector2(4f, 5f)

                    it("returns added values") {
                        assert.that(result, equalTo(Vector2(0.5f, 0.6f)))
                    }

                    it("does not modify original") {
                        assert.that(vector, equalTo(Vector2(2f, 3f)))
                    }
                }
            }
        }

        describe(".plusAssign") {
            on("when given identity") {
                val addend = Vector2(0f, 0f)
                vector += addend

                it("stores correct result") {
                    assert.that(vector, equalTo(Vector2(2f, 3f)))
                }

                it("does not modify original") {
                    assert.that(addend, equalTo(Vector2(0f, 0f)))
                }
            }

            on("when given non-identity") {
                val addend = Vector2(4f, 5f)
                vector += addend

                it("stores correct result") {
                    assert.that(vector, equalTo(Vector2(6f, 8f)))
                }

                it("does not modify addend") {
                    assert.that(addend, equalTo(Vector2(4f, 5f)))
                }
            }
        }

        describe(".minusAssign") {
            on("when given identity") {
                val subtrahend = Vector2(0f, 0f)
                vector -= subtrahend

                it("stores correct result") {
                    assert.that(vector, equalTo(Vector2(2f, 3f)))
                }

                it("does not modify subtrahend") {
                    assert.that(subtrahend, equalTo(Vector2(0f, 0f)))
                }
            }

            on("when given non-identity") {
                val subtrahend = Vector2(1f, 7f)
                vector -= subtrahend

                it("stores correct result") {
                    assert.that(vector, equalTo(Vector2(1f, -4f)))
                }

                it("does not modify subtrahend") {
                    assert.that(subtrahend, equalTo(Vector2(1f, 7f)))
                }
            }
        }

        describe(".timesAssign") {
            on("when given identity") {
                val multiplicand = Vector2(1f, 1f)
                vector *= multiplicand

                it("stores correct result") {
                    assert.that(vector, equalTo(Vector2(2f, 3f)))
                }

                it("does not modify multiplicand") {
                    assert.that(multiplicand, equalTo(Vector2(1f, 1f)))
                }
            }

            on("when given non-identity") {
                val multiplicand = Vector2(4f, 5f)
                vector *= multiplicand

                it("stores correct result") {
                    assert.that(vector, equalTo(Vector2(8f, 15f)))
                }

                it("does not modify multiplicand") {
                    assert.that(multiplicand, equalTo(Vector2(4f, 5f)))
                }
            }
        }

        describe(".divAssign") {
            on("when given identity") {
                val divisor = Vector2(1f, 1f)
                vector /= divisor

                it("stores correct result") {
                    assert.that(vector, equalTo(Vector2(2f, 3f)))
                }

                it("does not modify divisor") {
                    assert.that(divisor, equalTo(Vector2(1f, 1f)))
                }
            }

            on("when given non-identity") {
                val divisor = Vector2(4f, 5f)
                vector /= divisor

                it("stores correct result") {
                    assert.that(vector, equalTo(Vector2(0.5f, 0.6f)))
                }

                it("does not modify divisor") {
                    assert.that(divisor, equalTo(Vector2(4f, 5f)))
                }
            }
        }

        describe(".toGridPoint2") {
            it("rounds values correctly") {
                assert.that(Vector2(2.2f, 2.8f).toGridPoint2(), equalTo(GridPoint2(2, 3)))
            }
        }

        describe(".toCubeCoordinate") {
            it("rounds values correctly") {
                assert.that(Vector2(2.2f, 2.8f).toCubeCoordinate(), equalTo(CubeCoordinate(2, 3, -5)))
            }
        }

        describe(".toVector3") {
            it("copies values correctly") {
                assert.that(Vector2(2.2f, 2.8f).toVector3(), equalTo(Vector3(2.2f, 2.8f, 0f)))
            }
        }
    }

    describe("Vector3") {
        val vector = Vector3()
        beforeEachTest { vector.set(2f, 3f, 4f) }

        describe(".plus") {
            on("when given identity") {
                val result = vector + Vector3(0f, 0f, 0f)

                it("returns same values") {
                    assert.that(result, equalTo(vector))
                }

                it("does not modify original") {
                    assert.that(vector, equalTo(Vector3(2f, 3f, 4f)))
                }
            }

            on("when given non-identity") {
                val result = vector + Vector3(4f, 5f, 6f)

                it("returns added values") {
                    assert.that(result, equalTo(Vector3(6f, 8f, 10f)))
                }

                it("does not modify original") {
                    assert.that(vector, equalTo(Vector3(2f, 3f, 4f)))
                }
            }
        }

        describe(".minus") {
            on("when given identity") {
                val result = vector - Vector3(0f, 0f, 0f)

                it("returns same values") {
                    assert.that(result, equalTo(vector))
                }

                it("does not modify original") {
                    assert.that(vector, equalTo(Vector3(2f, 3f, 4f)))
                }
            }

            on("when given non-identity") {
                val result = vector - Vector3(1f, 7f, 10f)

                it("returns added values") {
                    assert.that(result, equalTo(Vector3(1f, -4f, -6f)))
                }

                it("does not modify original") {
                    assert.that(vector, equalTo(Vector3(2f, 3f, 4f)))
                }
            }
        }

        describe(".times") {
            context("Number") {
                on("when given identity") {
                    val result = vector * 1f

                    it("returns same values") {
                        assert.that(result, equalTo(vector))
                    }

                    it("does not modify original") {
                        assert.that(vector, equalTo(Vector3(2f, 3f, 4f)))
                    }
                }

                on("when given non-identity") {
                    val result = vector * 5f

                    it("returns added values") {
                        assert.that(result, equalTo(Vector3(10f, 15f, 20f)))
                    }

                    it("does not modify original") {
                        assert.that(vector, equalTo(Vector3(2f, 3f, 4f)))
                    }
                }
            }

            context("Vector") {
                on("when given identity") {
                    val result = vector * Vector3(1f, 1f, 1f)

                    it("returns same values") {
                        assert.that(result, equalTo(vector))
                    }

                    it("does not modify original") {
                        assert.that(vector, equalTo(Vector3(2f, 3f, 4f)))
                    }
                }

                on("when given non-identity") {
                    val result = vector * Vector3(4f, 5f, 6f)

                    it("returns added values") {
                        assert.that(result, equalTo(Vector3(8f, 15f, 24f)))
                    }

                    it("does not modify original") {
                        assert.that(vector, equalTo(Vector3(2f, 3f, 4f)))
                    }
                }
            }
        }

        describe(".div") {
            context("Number") {
                on("when given identity") {
                    val result = vector / 1f

                    it("returns same values") {
                        assert.that(result, equalTo(vector))
                    }

                    it("does not modify original") {
                        assert.that(vector, equalTo(Vector3(2f, 3f, 4f)))
                    }
                }

                on("when given non-identity") {
                    val result = vector / 5f

                    it("returns added values") {
                        assert.that(result, equalTo(Vector3(0.4f, 0.6f, 0.8f)))
                    }

                    it("does not modify original") {
                        assert.that(vector, equalTo(Vector3(2f, 3f, 4f)))
                    }
                }
            }

            context("Vector") {
                on("when given identity") {
                    val result = vector / Vector3(1f, 1f, 1f)

                    it("returns same values") {
                        assert.that(result, equalTo(vector))
                    }

                    it("does not modify original") {
                        assert.that(vector, equalTo(Vector3(2f, 3f, 4f)))
                    }
                }

                on("when given non-identity") {
                    val result = vector / Vector3(4f, 5f, 5f)

                    it("returns added values") {
                        assert.that(result, equalTo(Vector3(0.5f, 0.6f, 0.8f)))
                    }

                    it("does not modify original") {
                        assert.that(vector, equalTo(Vector3(2f, 3f, 4f)))
                    }
                }
            }
        }

        describe(".plusAssign") {
            on("when given identity") {
                val addend = Vector3(0f, 0f, 0f)
                vector += addend

                it("stores correct result") {
                    assert.that(vector, equalTo(Vector3(2f, 3f, 4f)))
                }

                it("does not modify original") {
                    assert.that(addend, equalTo(Vector3(0f, 0f, 0f)))
                }
            }

            on("when given non-identity") {
                val addend = Vector3(4f, 5f, 6f)
                vector += addend

                it("stores correct result") {
                    assert.that(vector, equalTo(Vector3(6f, 8f, 10f)))
                }

                it("does not modify addend") {
                    assert.that(addend, equalTo(Vector3(4f, 5f, 6f)))
                }
            }
        }

        describe(".minusAssign") {
            on("when given identity") {
                val subtrahend = Vector3(0f, 0f, 0f)
                vector -= subtrahend

                it("stores correct result") {
                    assert.that(vector, equalTo(Vector3(2f, 3f, 4f)))
                }

                it("does not modify subtrahend") {
                    assert.that(subtrahend, equalTo(Vector3(0f, 0f, 0f)))
                }
            }

            on("when given non-identity") {
                val subtrahend = Vector3(1f, 7f, 10f)
                vector -= subtrahend

                it("stores correct result") {
                    assert.that(vector, equalTo(Vector3(1f, -4f, -6f)))
                }

                it("does not modify subtrahend") {
                    assert.that(subtrahend, equalTo(Vector3(1f, 7f, 10f)))
                }
            }
        }

        describe(".timesAssign") {
            on("when given identity") {
                val multiplicand = Vector3(1f, 1f, 1f)
                vector *= multiplicand

                it("stores correct result") {
                    assert.that(vector, equalTo(Vector3(2f, 3f, 4f)))
                }

                it("does not modify multiplicand") {
                    assert.that(multiplicand, equalTo(Vector3(1f, 1f, 1f)))
                }
            }

            on("when given non-identity") {
                val multiplicand = Vector3(4f, 5f, 6f)
                vector *= multiplicand

                it("stores correct result") {
                    assert.that(vector, equalTo(Vector3(8f, 15f, 24f)))
                }

                it("does not modify multiplicand") {
                    assert.that(multiplicand, equalTo(Vector3(4f, 5f, 6f)))
                }
            }
        }

        describe(".divAssign") {
            on("when given identity") {
                val divisor = Vector3(1f, 1f, 1f)
                vector /= divisor

                it("stores correct result") {
                    assert.that(vector, equalTo(Vector3(2f, 3f, 4f)))
                }

                it("does not modify divisor") {
                    assert.that(divisor, equalTo(Vector3(1f, 1f, 1f)))
                }
            }

            on("when given non-identity") {
                val divisor = Vector3(4f, 5f, 5f)
                vector /= divisor

                it("stores correct result") {
                    assert.that(vector, equalTo(Vector3(0.5f, 0.6f, 0.8f)))
                }

                it("does not modify divisor") {
                    assert.that(divisor, equalTo(Vector3(4f, 5f, 5f)))
                }
            }
        }

        describe(".abs") {
            it("does not affect positive values") {
                assert.that(Vector3(1f, 2f, 5f).abs(), equalTo(Vector3(1f, 2f, 5f)))
            }

            it("makes negative values positive") {
                assert.that(Vector3(-1f, -2f, -5f).abs(), equalTo(Vector3(1f, 2f, 5f)))
            }
        }

        describe(".round") {
            it("rounds values correctly") {
                assert.that(Vector3(0f, 2.2f, 2.8f).round(), equalTo(GridPoint3(0, 2, 3)))
            }
        }

        describe(".toCubeCoordinate") {
            it("converts identity correctly") {
                assert.that(Vector3(0f, 0f, 0f).toCubeCoordinate(), equalTo(CubeCoordinate(0, 0, 0)))
            }

            it("converts values with largest x diff correctly") {
                assert.that(Vector3(3.5f, 2.3f, 0.9f).toCubeCoordinate(), equalTo(CubeCoordinate(-3, 2, 1)))
            }

            it("converts values with largest y diff correctly") {
                assert.that(Vector3(2.3f, 3.5f, 0.9f).toCubeCoordinate(), equalTo(CubeCoordinate(2, -3, 1)))
            }

            it("converts values with largest z diff correctly") {
                assert.that(Vector3(2.3f, 0.9f, 3.5f).toCubeCoordinate(), equalTo(CubeCoordinate(2, 1, -3)))
            }
        }

        describe(".toVector2") {
            it("copies values correctly") {
                assert.that(vector.toVector2(), equalTo(Vector2(2f, 3f)))
            }
        }
    }

    describe("Rectangle") {
        val rect = Rectangle(2f, 3f, 10f, 20f)

        describe(".minX") {
            it("returns x value") {
                assert.that(rect.minX, equalTo(2f))
            }
        }

        describe(".minY") {
            it("returns y value") {
                assert.that(rect.minY, equalTo(3f))
            }
        }

        describe(".maxX") {
            it("returns value of x + width") {
                assert.that(rect.maxX, equalTo(12f))
            }
        }

        describe(".maxY") {
            it("returns value of y + height") {
                assert.that(rect.maxY, equalTo(23f))
            }
        }
    }
})
package com.darkgravity.khexgrid.math

import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.GridPoint3
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.throws
import io.kotest.core.spec.style.DescribeSpec
import ktx.math.ImmutableVector2

/**
 * @author Dan McCabe
 */
object MathExtensionsSpec : DescribeSpec({
    describe("GridPoint2") {
        val point = GridPoint2(2, 3)

        describe(".plus") {
            it("returns same values when given identity") {
                assert.that(point + ImmutableVector2(0f, 0f), equalTo(ImmutableVector2(2f, 3f)))
            }

            it("adds values when given non-identity") {
                assert.that(point + ImmutableVector2(4f, 5f), equalTo(ImmutableVector2(6f, 8f)))
            }
        }

        describe(".minus") {
            it("returns same values when given identity") {
                assert.that(point - ImmutableVector2(0f, 0f), equalTo(ImmutableVector2(2f, 3f)))
            }

            it("subtracts values when given non-identity") {
                assert.that(point - ImmutableVector2(1f, 7f), equalTo(ImmutableVector2(1f, -4f)))
            }
        }

        describe(".toVector2") {
            it("returns equivalent Vector") {
                assert.that(point.toVector2(), equalTo(ImmutableVector2(2f, 3f)))
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

    describe("ImmutableVector2") {
        describe(".toGridPoint2") {
            it("rounds values correctly") {
                assert.that(ImmutableVector2(2.2f, 2.8f).toGridPoint2(), equalTo(GridPoint2(2, 3)))
            }
        }

        describe(".toCubeCoordinate") {
            it("rounds values correctly") {
                assert.that(ImmutableVector2(2.2f, 2.8f).toCubeCoordinate(), equalTo(CubeCoordinate(2, 3, -5)))
            }
        }

        describe(".toVector3") {
            it("copies values correctly") {
                assert.that(ImmutableVector2(2.2f, 2.8f).toVector3(), equalTo(Vector3(2.2f, 2.8f, 0f)))
            }
        }
    }

    describe("Vector3") {
        val vector = Vector3()
        beforeEach { vector.set(2f, 3f, 4f) }

        describe(".plus") {
            describe("when given identity") {
                lateinit var result: Vector3
                beforeEach { result = vector + Vector3(0f, 0f, 0f) }

                it("returns same values") {
                    assert.that(result, equalTo(vector))
                }

                it("does not modify original") {
                    assert.that(vector, equalTo(Vector3(2f, 3f, 4f)))
                }
            }

            describe("when given non-identity") {
                lateinit var result: Vector3
                beforeEach { result = vector + Vector3(4f, 5f, 6f) }

                it("returns added values") {
                    assert.that(result, equalTo(Vector3(6f, 8f, 10f)))
                }

                it("does not modify original") {
                    assert.that(vector, equalTo(Vector3(2f, 3f, 4f)))
                }
            }
        }

        describe(".minus") {
            describe("when given identity") {
                lateinit var result: Vector3
                beforeEach { result = vector - Vector3(0f, 0f, 0f) }

                it("returns same values") {
                    assert.that(result, equalTo(vector))
                }

                it("does not modify original") {
                    assert.that(vector, equalTo(Vector3(2f, 3f, 4f)))
                }
            }

            describe("when given non-identity") {
                lateinit var result: Vector3
                beforeEach { result = vector - Vector3(1f, 7f, 10f) }

                it("returns added values") {
                    assert.that(result, equalTo(Vector3(1f, -4f, -6f)))
                }

                it("does not modify original") {
                    assert.that(vector, equalTo(Vector3(2f, 3f, 4f)))
                }
            }
        }

        describe(".times") {
            describe("Number") {
                describe("when given identity") {
                    lateinit var result: Vector3
                    beforeEach { result = vector * 1f }

                    it("returns same values") {
                        assert.that(result, equalTo(vector))
                    }

                    it("does not modify original") {
                        assert.that(vector, equalTo(Vector3(2f, 3f, 4f)))
                    }
                }

                describe("when given non-identity") {
                    lateinit var result: Vector3
                    beforeEach { result = vector * 5f }

                    it("returns added values") {
                        assert.that(result, equalTo(Vector3(10f, 15f, 20f)))
                    }

                    it("does not modify original") {
                        assert.that(vector, equalTo(Vector3(2f, 3f, 4f)))
                    }
                }
            }

            describe("Vector") {
                describe("when given identity") {
                    lateinit var result: Vector3
                    beforeEach { result = vector * Vector3(1f, 1f, 1f) }

                    it("returns same values") {
                        assert.that(result, equalTo(vector))
                    }

                    it("does not modify original") {
                        assert.that(vector, equalTo(Vector3(2f, 3f, 4f)))
                    }
                }

                describe("when given non-identity") {
                    lateinit var result: Vector3
                    beforeEach { result = vector * Vector3(4f, 5f, 6f) }

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
            describe("Number") {
                describe("when given identity") {
                    lateinit var result: Vector3
                    beforeEach { result = vector / 1f }

                    it("returns same values") {
                        assert.that(result, equalTo(vector))
                    }

                    it("does not modify original") {
                        assert.that(vector, equalTo(Vector3(2f, 3f, 4f)))
                    }
                }

                describe("when given non-identity") {
                    lateinit var result: Vector3
                    beforeEach { result = vector / 5f }

                    it("returns added values") {
                        assert.that(result, equalTo(Vector3(0.4f, 0.6f, 0.8f)))
                    }

                    it("does not modify original") {
                        assert.that(vector, equalTo(Vector3(2f, 3f, 4f)))
                    }
                }
            }

            describe("Vector") {
                describe("when given identity") {
                    lateinit var result: Vector3
                    beforeEach { result = vector / Vector3(1f, 1f, 1f) }

                    it("returns same values") {
                        assert.that(result, equalTo(vector))
                    }

                    it("does not modify original") {
                        assert.that(vector, equalTo(Vector3(2f, 3f, 4f)))
                    }
                }

                describe("when given non-identity") {
                    lateinit var result: Vector3
                    beforeEach { result = vector / Vector3(4f, 5f, 5f) }

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
            describe("when given identity") {
                val addend = Vector3(0f, 0f, 0f)
                beforeEach { vector += addend }

                it("stores correct result") {
                    assert.that(vector, equalTo(Vector3(2f, 3f, 4f)))
                }

                it("does not modify original") {
                    assert.that(addend, equalTo(Vector3(0f, 0f, 0f)))
                }
            }

            describe("when given non-identity") {
                val addend = Vector3(4f, 5f, 6f)
                beforeEach { vector += addend }

                it("stores correct result") {
                    assert.that(vector, equalTo(Vector3(6f, 8f, 10f)))
                }

                it("does not modify addend") {
                    assert.that(addend, equalTo(Vector3(4f, 5f, 6f)))
                }
            }
        }

        describe(".minusAssign") {
            describe("when given identity") {
                val subtrahend = Vector3(0f, 0f, 0f)
                beforeEach { vector -= subtrahend }

                it("stores correct result") {
                    assert.that(vector, equalTo(Vector3(2f, 3f, 4f)))
                }

                it("does not modify subtrahend") {
                    assert.that(subtrahend, equalTo(Vector3(0f, 0f, 0f)))
                }
            }

            describe("when given non-identity") {
                val subtrahend = Vector3(1f, 7f, 10f)
                beforeEach { vector -= subtrahend }

                it("stores correct result") {
                    assert.that(vector, equalTo(Vector3(1f, -4f, -6f)))
                }

                it("does not modify subtrahend") {
                    assert.that(subtrahend, equalTo(Vector3(1f, 7f, 10f)))
                }
            }
        }

        describe(".timesAssign") {
            describe("when given identity") {
                val multiplicand = Vector3(1f, 1f, 1f)
                beforeEach { vector *= multiplicand }

                it("stores correct result") {
                    assert.that(vector, equalTo(Vector3(2f, 3f, 4f)))
                }

                it("does not modify multiplicand") {
                    assert.that(multiplicand, equalTo(Vector3(1f, 1f, 1f)))
                }
            }

            describe("when given non-identity") {
                val multiplicand = Vector3(4f, 5f, 6f)
                beforeEach { vector *= multiplicand }

                it("stores correct result") {
                    assert.that(vector, equalTo(Vector3(8f, 15f, 24f)))
                }

                it("does not modify multiplicand") {
                    assert.that(multiplicand, equalTo(Vector3(4f, 5f, 6f)))
                }
            }
        }

        describe(".divAssign") {
            describe("when given identity") {
                val divisor = Vector3(1f, 1f, 1f)
                beforeEach { vector /= divisor }

                it("stores correct result") {
                    assert.that(vector, equalTo(Vector3(2f, 3f, 4f)))
                }

                it("does not modify divisor") {
                    assert.that(divisor, equalTo(Vector3(1f, 1f, 1f)))
                }
            }

            describe("when given non-identity") {
                val divisor = Vector3(4f, 5f, 5f)
                beforeEach { vector /= divisor }

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
})
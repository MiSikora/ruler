package io.mehow.ruler

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.comparables.shouldBeEqualComparingTo
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.filterNot
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.numericDoubles
import io.kotest.property.checkAll
import io.mehow.ruler.test.DistanceGenerator
import java.math.BigDecimal
import kotlin.Long.Companion.MAX_VALUE
import kotlin.Long.Companion.MIN_VALUE

internal class DistanceSpec : DescribeSpec({
  describe("zero distance") {
    val zeroDistance = Distance.Zero

    it("does not have any meters") {
      zeroDistance.meters shouldBeEqualComparingTo BigDecimal.ZERO
    }
  }

  describe("min distance") {
    val minDistance = Distance.Min

    it("has smallest possible meters") {
      minDistance.meters shouldBeEqualComparingTo MIN_VALUE.toBigDecimal()
    }

    it("cannot be subtracted from") {
      shouldThrow<ArithmeticException> { minDistance - Distance.ofNanometers(1) }
    }

    it("can be added to") {
      checkAll(DistanceGenerator.create(min = Distance.Zero)) { distance ->
        shouldNotThrowAny { minDistance + distance }
      }
    }
  }

  describe("max distance") {
    val maxDistance = Distance.Max

    it("has largest possible meters") {
      maxDistance.meters shouldBe MAX_VALUE.toBigDecimal() + 999_999_999.toBigDecimal().movePointLeft(9)
    }

    it("cannot be added to") {
      shouldThrow<ArithmeticException> { maxDistance + Distance.ofNanometers(1) }
    }

    it("can be subtracted from") {
      checkAll(DistanceGenerator.create(min = Distance.Zero)) { distance ->
        shouldNotThrowAny { maxDistance - distance }
      }
    }
  }

  describe("distance") {
    val distanceGenerator = DistanceGenerator.create(
        min = Distance.ofGigameters(-1),
        max = Distance.ofGigameters(1),
    )
    // Filter small values to avoid division explosion.
    val wholeGenerator = Arb.long(-500_000, 500_000).filterNot { it == 0L }
    val realGenerator = Arb.numericDoubles(-500_000.0, 500_000.0).filterNot { it in -0.000_001..0.000_001 }

    context("can be multiplied") {
      it("by a whole number") {
        checkAll(distanceGenerator, wholeGenerator) { distance, multiplier ->
          distance * multiplier shouldBe Distance.create(distance.meters * multiplier.toBigDecimal())
        }
      }

      it("by a real number") {
        checkAll(distanceGenerator, realGenerator) { distance, multiplier ->
          distance * multiplier shouldBe Distance.create(distance.meters * multiplier.toBigDecimal())
        }
      }
    }

    context("can be divided") {
      it("by a whole number") {
        checkAll(distanceGenerator, wholeGenerator) { distance, multiplier ->
          distance / multiplier shouldBe Distance.create(distance.meters / multiplier.toBigDecimal())
        }
      }

      it("by a real number") {
        checkAll(distanceGenerator, realGenerator) { distance, multiplier ->
          distance / multiplier shouldBe Distance.create(distance.meters / multiplier.toBigDecimal())
        }
      }
    }

    it("can have absolute value computed") {
      checkAll(distanceGenerator) { distance ->
        distance.abs() shouldBe Distance.create(distance.meters.abs())
      }
    }
  }

  describe("two distances") {
    val generator = DistanceGenerator.create(
        min = Distance.ofGigameters(-1),
        max = Distance.ofGigameters(1),
    )

    it("can be added") {
      checkAll(generator, generator) { first, second ->
        first + second shouldBe Distance.create(first.meters + second.meters)
      }
    }

    it("can be subtracted") {
      checkAll(generator, generator) { first, second ->
        first - second shouldBe Distance.create(first.meters - second.meters)
      }
    }

    it("can be compared") {
      checkAll(generator, generator) { first, second ->
        first.compareTo(second) shouldBe first.meters.compareTo(second.meters)
      }
    }
  }
})

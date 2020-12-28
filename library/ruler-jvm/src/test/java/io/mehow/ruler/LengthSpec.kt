package io.mehow.ruler

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.comparables.shouldBeEqualComparingTo
import io.kotest.matchers.comparables.shouldNotBeGreaterThan
import io.kotest.matchers.comparables.shouldNotBeLessThan
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.filterNot
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.numericDoubles
import io.kotest.property.checkAll
import io.mehow.ruler.ImperialLengthUnit.Foot
import io.mehow.ruler.ImperialLengthUnit.Inch
import io.mehow.ruler.ImperialLengthUnit.Mile
import io.mehow.ruler.ImperialLengthUnit.Yard
import io.mehow.ruler.SiLengthUnit.Gigameter
import io.mehow.ruler.SiLengthUnit.Kilometer
import io.mehow.ruler.SiLengthUnit.Megameter
import io.mehow.ruler.SiLengthUnit.Meter
import io.mehow.ruler.SiLengthUnit.Micrometer
import io.mehow.ruler.SiLengthUnit.Millimeter
import io.mehow.ruler.SiLengthUnit.Nanometer
import io.mehow.ruler.test.LengthGenerator

internal class LengthSpec : DescribeSpec({
  fun Distance.toLength(unit: LengthUnit<*>) = when (unit) {
    Nanometer -> toLength(Nanometer)
    Micrometer -> toLength(Micrometer)
    Millimeter -> toLength(Millimeter)
    Meter -> toLength(Meter)
    Kilometer -> toLength(Kilometer)
    Megameter -> toLength(Megameter)
    Gigameter -> toLength(Gigameter)
    Inch -> toLength(Inch)
    Foot -> toLength(Foot)
    Yard -> toLength(Yard)
    Mile -> toLength(Mile)
  }

  fun LengthUnit<*>.capacity() = bounds.endInclusive.toLength(this).measure.toLong()

  describe("length") {
    val lengthGenerator = LengthGenerator.create(
        min = Distance.ofGigameters(-1),
        max = Distance.ofGigameters(1),
    )
    // Filter small values to avoid division explosion.
    val wholeGenerator = Arb.long(-500_000, 500_000).filterNot { it == 0L }
    val realGenerator = Arb.numericDoubles(-500_000.0, 500_000.0).filterNot { it in -0.000_001..0.000_001 }

    context("can be multiplied") {
      it("by a whole number") {
        checkAll(lengthGenerator, wholeGenerator) { length, multiplier ->
          length * multiplier shouldBe (length.distance * multiplier).toLength(length.unit)
        }
      }

      it("by a real number") {
        checkAll(lengthGenerator, realGenerator) { length, multiplier ->
          length * multiplier shouldBe (length.distance * multiplier).toLength(length.unit)
        }
      }
    }

    context("can be divided") {
      it("by a whole number") {
        checkAll(lengthGenerator, wholeGenerator) { length, multiplier ->
          length / multiplier shouldBe (length.distance / multiplier).toLength(length.unit)
        }
      }

      it("by a real number") {
        checkAll(lengthGenerator, realGenerator) { length, multiplier ->
          length / multiplier shouldBe (length.distance / multiplier).toLength(length.unit)
        }
      }
    }

    it("can have absolute value computed") {
      checkAll(lengthGenerator) { length ->
        length.abs() shouldBe length.distance.abs().toLength(length.unit)
      }
    }

    it("has correct measured distance") {
      for (unit in LengthUnit.units) {
        checkAll(Arb.long(-1_000_000, 1_000_000)) { unitCount ->
          val length = Distance.of(unitCount, unit).toLength(unit)
          length.measure shouldBeEqualComparingTo unitCount.toBigDecimal()
        }
      }
    }

    context("with some distance") {
      it("can change unit with an automatic fit") {
        for (unit in LengthUnit.units) {
          val inRangeValue = unit.capacity()
          val generator = LengthGenerator.forUnit(
              unit = unit,
              ranges = listOf(
                  Distance.of(-inRangeValue, unit)..Distance.of(-1, unit),
                  Distance.of(1, unit)..Distance.of(inRangeValue, unit),
              ),
          )
          checkAll(generator) { length ->
            length.withAutoUnit().unit shouldBe unit
          }
        }
      }
    }

    context("with zero distance") {
      it("is not affected by unit automatic fit") {
        for (unit in LengthUnit.units) {
          Distance.Zero.toLength(unit).withAutoUnit().unit shouldBe unit
        }
      }
    }

    it("can have unit coerced to min") {
      for (unit in SiLengthUnit.units) {
        checkAll(LengthGenerator.si()) { length ->
          length.coerceUnitAtLeastTo(unit).unit shouldNotBeLessThan unit
        }
      }

      for (unit in ImperialLengthUnit.units) {
        checkAll(LengthGenerator.imperial()) { length ->
          length.coerceUnitAtLeastTo(unit).unit shouldNotBeLessThan unit
        }
      }
    }

    it("can have unit coerced to max") {
      for (unit in SiLengthUnit.units) {
        checkAll(LengthGenerator.si()) { length ->
          length.coerceUnitAtMostTo(unit).unit shouldNotBeGreaterThan unit
        }
      }

      for (unit in ImperialLengthUnit.units) {
        checkAll(LengthGenerator.imperial()) { length ->
          length.coerceUnitAtMostTo(unit).unit shouldNotBeGreaterThan unit
        }
      }
    }

    it("can have unit coerced between in a range") {
      val siUnitRanges = SiLengthUnit.units.windowed(size = 2, step = 3).map { it.first() to it.last() }
      for ((start, end) in siUnitRanges) {
        checkAll(LengthGenerator.si()) { length ->
          length.coerceUnitIn(start..end).unit shouldNotBeLessThan start
          length.coerceUnitIn(start..end).unit shouldNotBeGreaterThan end
        }
      }

      val imperialUnitRanges = ImperialLengthUnit.units.windowed(size = 2, step = 3).map { it.first() to it.last() }
      for ((start, end) in imperialUnitRanges) {
        checkAll(LengthGenerator.imperial()) { length ->
          length.coerceUnitIn(start..end).unit shouldNotBeLessThan start
          length.coerceUnitIn(start..end).unit shouldNotBeGreaterThan end
        }
      }
    }

    it("can have unit coerced between min and max") {
      val siUnitRanges = SiLengthUnit.units.windowed(size = 2, step = 3).map { it.first() to it.last() }
      for ((start, end) in siUnitRanges) {
        checkAll(LengthGenerator.si()) { length ->
          length.coerceUnitIn(start, end).unit shouldNotBeLessThan start
          length.coerceUnitIn(start, end).unit shouldNotBeGreaterThan end
        }
      }

      val imperialUnitRanges = ImperialLengthUnit.units.windowed(size = 2, step = 3).map { it.first() to it.last() }
      for ((start, end) in imperialUnitRanges) {
        checkAll(LengthGenerator.imperial()) { length ->
          length.coerceUnitIn(start, end).unit shouldNotBeLessThan start
          length.coerceUnitIn(start, end).unit shouldNotBeGreaterThan end
        }
      }
    }
  }

  describe("two lengths") {
    val generator = LengthGenerator.create(
        min = Distance.ofGigameters(-1),
        max = Distance.ofGigameters(1),
    )

    it("can be added") {
      checkAll(generator, generator) { first, second ->
        first + second shouldBe (first.distance + second.distance).toLength(first.unit)
      }
    }

    it("can be subtracted") {
      checkAll(generator, generator) { first, second ->
        first - second shouldBe (first.distance - second.distance).toLength(first.unit)
      }
    }

    it("can be compared") {
      checkAll(generator, generator) { first, second ->
        first.compareTo(second) shouldBe first.distance.compareTo(second.distance)
      }
    }
  }
})

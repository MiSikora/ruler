package io.mehow.ruler

import io.kotlintest.matchers.types.shouldBeSameInstanceAs
import io.kotlintest.properties.assertAll
import io.kotlintest.shouldBe
import io.kotlintest.specs.AbstractBehaviorSpec
import io.kotlintest.specs.BehaviorSpec
import io.mehow.ruler.ImperialDistanceUnit.Foot
import io.mehow.ruler.ImperialDistanceUnit.Inch
import io.mehow.ruler.ImperialDistanceUnit.Mile
import io.mehow.ruler.ImperialDistanceUnit.Yard
import io.mehow.ruler.SiDistanceUnit.Gigameter
import io.mehow.ruler.SiDistanceUnit.Kilometer
import io.mehow.ruler.SiDistanceUnit.Megameter
import io.mehow.ruler.SiDistanceUnit.Meter
import io.mehow.ruler.SiDistanceUnit.Micrometer
import io.mehow.ruler.SiDistanceUnit.Millimeter
import io.mehow.ruler.SiDistanceUnit.Nanometer
import kotlin.Long.Companion.MAX_VALUE

class DistanceSpec : BehaviorSpec({
  Given("two distances") {
    When("they are added") {
      Then("unit of the left operand is preserved") {
        assertAll(
            LengthsGenerator(max = Length.create(MAX_VALUE / 2 - 1, 500_000_000)),
            DistanceUnitGenerator,
            LengthsGenerator(max = Length.create(MAX_VALUE / 2 - 1, 500_000_000)),
            DistanceUnitGenerator
        ) { length1, unit1, length2, unit2 ->
          val distance1 = DistanceUnitGenerator.createDistance(length1, unit1)
          val distance2 = DistanceUnitGenerator.createDistance(length2, unit2)

          val newDistance = distance1 + distance2

          newDistance.unit shouldBe distance1.unit
        }
      }
    }

    When("they are subtracted") {
      Then("unit of the left operand is preserved") {
        assertAll(
            LengthsGenerator(min = Length.ofMeters(MAX_VALUE / 2)),
            DistanceUnitGenerator,
            LengthsGenerator(max = Length.ofMeters(MAX_VALUE / 2)),
            DistanceUnitGenerator
        ) { length1, unit1, length2, unit2 ->
          val distance1 = DistanceUnitGenerator.createDistance(length1, unit1)
          val distance2 = DistanceUnitGenerator.createDistance(length2, unit2)

          val newDistance = distance1 - distance2

          newDistance.unit shouldBe distance1.unit
        }
      }
    }
  }

  checkSiUnit(Nanometer)

  checkSiUnit(Micrometer)

  checkSiUnit(Millimeter)

  checkSiUnit(Meter)

  checkSiUnit(Kilometer)

  checkSiUnit(Megameter)

  checkSiUnit(Gigameter)

  checkImperialUnit(Inch, 11)

  checkImperialUnit(Foot, 2)

  checkImperialUnit(Yard, 1759)

  checkImperialUnit(Mile, 1_000_000)
})

private fun AbstractBehaviorSpec.checkSiUnit(unit: SiDistanceUnit) {
  Given("$unit length") {
    When("I auto unit it") {
      Then("it uses $unit as a unit") {
        assertAll(LengthsGenerator(Length.of(1, unit), Length.of(999, unit))) { length ->
          val distance = length.toDistance(SiDistanceUnit.values().random()).withAutoUnit()

          distance.unit shouldBeSameInstanceAs unit
        }
      }
    }

    When("I get measured length") {
      Then("It converts original length correctly") {
        assertAll(LongGenerator(min = 0L, max = 1_000L)) { value ->
          val length = Length.of(value, unit)
          val distance = length.toDistance(unit)

          distance.measuredLength.toDouble() shouldBe value.toDouble()
        }
      }
    }

    Then("I can coerce distance in range") {
      assertAll(LengthsGenerator()) { length ->
        val distance = length.toDistance(unit)

        SiDistanceUnit.values().filter { it > unit && it < Gigameter }.forEach {
          distance.coerceUnitIn(it..Gigameter).unit shouldBeSameInstanceAs it
        }
      }
    }

    Then("I can coerce distance in min and max") {
      assertAll(LengthsGenerator()) { length ->
        val distance = length.toDistance(unit)

        SiDistanceUnit.values().filter { it > unit && it < Gigameter }.forEach {
          distance.coerceUnitIn(it, Gigameter).unit shouldBeSameInstanceAs it
        }
      }
    }

    Then("I can coerce distance to min") {
      assertAll(LengthsGenerator()) { length ->
        val distance = length.toDistance(unit)

        SiDistanceUnit.values().filter { it > unit }.forEach {
          distance.coerceUnitAtLeastTo(it).unit shouldBeSameInstanceAs it
        }

        SiDistanceUnit.values().filter { it <= unit }.forEach {
          distance.coerceUnitAtLeastTo(it).unit shouldBeSameInstanceAs unit
        }
      }
    }

    Then("I can coerce distance to max") {
      assertAll(LengthsGenerator()) { length ->
        val distance = length.toDistance(unit)

        SiDistanceUnit.values().filter { it < unit }.forEach {
          distance.coerceUnitAtMostTo(it).unit shouldBeSameInstanceAs it
        }

        SiDistanceUnit.values().filter { it >= unit }.forEach {
          distance.coerceUnitAtMostTo(it).unit shouldBeSameInstanceAs unit
        }
      }
    }
  }
}

private fun AbstractBehaviorSpec.checkImperialUnit(
  unit: ImperialDistanceUnit,
  maxRange: Long
) {
  Given("$unit length") {
    When("I auto unit it") {
      Then("it uses $unit as a unit") {
        assertAll(LengthsGenerator(Length.of(1, unit), Length.of(maxRange, unit))) { length ->
          val distance = length.toDistance(ImperialDistanceUnit.values().random()).withAutoUnit()

          distance.unit shouldBeSameInstanceAs unit
        }
      }
    }

    When("I get measured length") {
      Then("It converts original length correctly") {
        assertAll(LongGenerator(min = 0L, max = 1_000L)) { value ->
          val length = Length.of(value, unit)
          val distance = length.toDistance(unit)

          distance.measuredLength.toDouble() shouldBe value.toDouble()
        }
      }
    }

    Then("I can coerce distance in range") {
      assertAll(LengthsGenerator()) { length ->
        val distance = length.toDistance(unit)

        ImperialDistanceUnit.values().filter { it > unit && it < Mile }.forEach {
          distance.coerceUnitIn(it..Mile).unit shouldBeSameInstanceAs it
        }
      }
    }

    Then("I can coerce distance in min and max") {
      assertAll(LengthsGenerator()) { length ->
        val distance = length.toDistance(unit)

        ImperialDistanceUnit.values().filter { it > unit && it < Mile }.forEach {
          distance.coerceUnitIn(it, Mile).unit shouldBeSameInstanceAs it
        }
      }
    }

    Then("I can coerce distance to min") {
      assertAll(LengthsGenerator()) { length ->
        val distance = length.toDistance(unit)

        ImperialDistanceUnit.values().filter { it > unit }.forEach {
          distance.coerceUnitAtLeastTo(it).unit shouldBeSameInstanceAs it
        }

        ImperialDistanceUnit.values().filter { it <= unit }.forEach {
          distance.coerceUnitAtLeastTo(it).unit shouldBeSameInstanceAs unit
        }
      }
    }

    Then("I can coerce distance to max") {
      assertAll(LengthsGenerator()) { length ->
        val distance = length.toDistance(unit)

        ImperialDistanceUnit.values().filter { it < unit }.forEach {
          distance.coerceUnitAtMostTo(it).unit shouldBeSameInstanceAs it
        }

        ImperialDistanceUnit.values().filter { it >= unit }.forEach {
          distance.coerceUnitAtMostTo(it).unit shouldBeSameInstanceAs unit
        }
      }
    }
  }
}

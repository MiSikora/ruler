package io.mehow.ruler

import io.kotlintest.properties.assertAll
import io.kotlintest.shouldBe
import io.kotlintest.specs.BehaviorSpec
import io.mehow.ruler.SiDistanceUnit.Gigameter
import io.mehow.ruler.SiDistanceUnit.Kilometer
import io.mehow.ruler.SiDistanceUnit.Megameter
import io.mehow.ruler.SiDistanceUnit.Meter
import io.mehow.ruler.SiDistanceUnit.Micrometer
import io.mehow.ruler.SiDistanceUnit.Millimeter
import io.mehow.ruler.SiDistanceUnit.Nanometer
import kotlin.Long.Companion.MAX_VALUE

class SiDistanceUnitSpec : BehaviorSpec({
  Given("nanometers") {
    val unit = Nanometer

    Then("length can be created from it") {
      assertAll(LongGenerator(0L)) { value ->
        val expectedLength = Length.create(value / 1_000_000_000, value % 1_000_000_000)

        val length1 = unit.toLength(value)
        val length2 = Length.ofNanometers(value)

        length1 shouldBe expectedLength
        length2 shouldBe expectedLength
      }
    }
  }

  Given("micrometers") {
    val unit = Micrometer

    Then("length can be created from it") {
      assertAll(LongGenerator(0L)) { value ->
        val expectedLength = Length.create(value / 1_000_000, (value % 1_000_000) * 1_000)

        val length1 = unit.toLength(value)
        val length2 = Length.ofMicrometers(value)

        length1 shouldBe expectedLength
        length2 shouldBe expectedLength
      }
    }
  }

  Given("millimeters") {
    val unit = Millimeter

    Then("length can be created from it") {
      assertAll(LongGenerator(0L)) { value ->
        val expectedLength = Length.create(value / 1_000, (value % 1_000) * 1_000_000)

        val length1 = unit.toLength(value)
        val length2 = Length.ofMillimeters(value)

        length1 shouldBe expectedLength
        length2 shouldBe expectedLength
      }
    }
  }

  Given("meters") {
    val unit = Meter

    Then("length can be created from it") {
      assertAll(LongGenerator(0L)) { value ->
        val expectedLength = Length.create(value)

        val length1 = unit.toLength(value)
        val length2 = Length.ofMeters(value)

        length1 shouldBe expectedLength
        length2 shouldBe expectedLength
      }
    }
  }

  Given("kilometers") {
    val unit = Kilometer

    Then("length can be created from it") {
      assertAll(LongGenerator(0L, MAX_VALUE / 1_000)) { value ->
        val expectedLength = Length.create(value * 1_000)

        val length1 = unit.toLength(value)
        val length2 = Length.ofKilometers(value)

        length1 shouldBe expectedLength
        length2 shouldBe expectedLength
      }
    }
  }

  Given("megameters") {
    val unit = Megameter

    Then("length can be created from it") {
      assertAll(LongGenerator(0L, MAX_VALUE / 1_000_000)) { value ->
        val expectedLength = Length.create(value * 1_000_000)

        val length1 = unit.toLength(value)
        val length2 = Length.ofMegameters(value)

        length1 shouldBe expectedLength
        length2 shouldBe expectedLength
      }
    }
  }

  Given("gigameters") {
    val unit = Gigameter

    Then("length can be created from it") {
      assertAll(LongGenerator(0L, MAX_VALUE / 1_000_000_000)) { value ->
        val expectedLength = Length.create(value * 1_000_000_000)

        val length1 = unit.toLength(value)
        val length2 = Length.ofGigameters(value)

        length1 shouldBe expectedLength
        length2 shouldBe expectedLength
      }
    }
  }
})

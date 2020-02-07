package io.mehow.ruler

import io.kotlintest.properties.assertAll
import io.kotlintest.shouldBe
import io.kotlintest.specs.BehaviorSpec
import io.mehow.ruler.SiLengthUnit.Gigameter
import io.mehow.ruler.SiLengthUnit.Kilometer
import io.mehow.ruler.SiLengthUnit.Megameter
import io.mehow.ruler.SiLengthUnit.Meter
import io.mehow.ruler.SiLengthUnit.Micrometer
import io.mehow.ruler.SiLengthUnit.Millimeter
import io.mehow.ruler.SiLengthUnit.Nanometer
import kotlin.Long.Companion.MAX_VALUE
import kotlin.Long.Companion.MIN_VALUE

class SiLengthUnitSpec : BehaviorSpec({
  Given("nanometers") {
    val unit = Nanometer

    Then("length can be created from it") {
      assertAll(LongGenerator()) { value ->
        val expectedLength = Distance.create(value / 1_000_000_000, value % 1_000_000_000)

        val length1 = unit.toDistance(value)
        val length2 = Distance.ofNanometers(value)

        length1 shouldBe expectedLength
        length2 shouldBe expectedLength
      }
    }
  }

  Given("micrometers") {
    val unit = Micrometer

    Then("length can be created from it") {
      assertAll(LongGenerator()) { value ->
        val expectedLength = Distance.create(value / 1_000_000, (value % 1_000_000) * 1_000)

        val length1 = unit.toDistance(value)
        val length2 = Distance.ofMicrometers(value)

        length1 shouldBe expectedLength
        length2 shouldBe expectedLength
      }
    }
  }

  Given("millimeters") {
    val unit = Millimeter

    Then("length can be created from it") {
      assertAll(LongGenerator()) { value ->
        val expectedLength = Distance.create(value / 1_000, (value % 1_000) * 1_000_000)

        val length1 = unit.toDistance(value)
        val length2 = Distance.ofMillimeters(value)

        length1 shouldBe expectedLength
        length2 shouldBe expectedLength
      }
    }
  }

  Given("meters") {
    val unit = Meter

    Then("length can be created from it") {
      assertAll(LongGenerator()) { value ->
        val expectedLength = Distance.create(value)

        val length1 = unit.toDistance(value)
        val length2 = Distance.ofMeters(value)

        length1 shouldBe expectedLength
        length2 shouldBe expectedLength
      }
    }
  }

  Given("kilometers") {
    val unit = Kilometer

    Then("length can be created from it") {
      assertAll(LongGenerator(MIN_VALUE / 1_000, MAX_VALUE / 1_000)) { value ->
        val expectedLength = Distance.create(value * 1_000)

        val length1 = unit.toDistance(value)
        val length2 = Distance.ofKilometers(value)

        length1 shouldBe expectedLength
        length2 shouldBe expectedLength
      }
    }
  }

  Given("megameters") {
    val unit = Megameter

    Then("length can be created from it") {
      assertAll(LongGenerator(MIN_VALUE / 1_000_000, MAX_VALUE / 1_000_000)) { value ->
        val expectedLength = Distance.create(value * 1_000_000)

        val length1 = unit.toDistance(value)
        val length2 = Distance.ofMegameters(value)

        length1 shouldBe expectedLength
        length2 shouldBe expectedLength
      }
    }
  }

  Given("gigameters") {
    val unit = Gigameter

    Then("length can be created from it") {
      assertAll(LongGenerator(MIN_VALUE / 1_000_000_000, MAX_VALUE / 1_000_000_000)) { value ->
        val expectedLength = Distance.create(value * 1_000_000_000)

        val length1 = unit.toDistance(value)
        val length2 = Distance.ofGigameters(value)

        length1 shouldBe expectedLength
        length2 shouldBe expectedLength
      }
    }
  }
})

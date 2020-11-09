package io.mehow.ruler

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.long
import io.kotest.property.checkAll
import kotlin.Long.Companion.MAX_VALUE
import kotlin.Long.Companion.MIN_VALUE

class SiLengthUnitSpec : BehaviorSpec({
  Given("nanometers") {
    Then("length can be created from it") {
      checkAll(Arb.long()) { value ->
        val expectedLength = Distance.create(value / 1_000_000_000, value % 1_000_000_000)
        val length = Distance.ofNanometers(value)
        length shouldBe expectedLength
      }
    }
  }

  Given("micrometers") {
    Then("length can be created from it") {
      checkAll(Arb.long()) { value ->
        val expectedLength = Distance.create(value / 1_000_000, (value % 1_000_000) * 1_000)
        val length = Distance.ofMicrometers(value)
        length shouldBe expectedLength
      }
    }
  }

  Given("millimeters") {
    Then("length can be created from it") {
      checkAll(Arb.long()) { value ->
        val expectedLength = Distance.create(value / 1_000, (value % 1_000) * 1_000_000)
        val length = Distance.ofMillimeters(value)
        length shouldBe expectedLength
      }
    }
  }

  Given("meters") {
    Then("length can be created from it") {
      checkAll(Arb.long()) { value ->
        val expectedLength = Distance.create(value)
        val length = Distance.ofMeters(value)
        length shouldBe expectedLength
      }
    }
  }

  Given("kilometers") {
    Then("length can be created from it") {
      checkAll(Arb.long(MIN_VALUE / 1_000, MAX_VALUE / 1_000)) { value ->
        val expectedLength = Distance.create(value * 1_000)
        val length = Distance.ofKilometers(value)
        length shouldBe expectedLength
      }
    }
  }

  Given("megameters") {
    Then("length can be created from it") {
      checkAll(Arb.long(MIN_VALUE / 1_000_000, MAX_VALUE / 1_000_000)) { value ->
        val expectedLength = Distance.create(value * 1_000_000)
        val length = Distance.ofMegameters(value)
        length shouldBe expectedLength
      }
    }
  }

  Given("gigameters") {
    Then("length can be created from it") {
      checkAll(Arb.long(MIN_VALUE / 1_000_000_000, MAX_VALUE / 1_000_000_000)) { value ->
        val expectedLength = Distance.create(value * 1_000_000_000)
        val length = Distance.ofGigameters(value)
        length shouldBe expectedLength
      }
    }
  }
})

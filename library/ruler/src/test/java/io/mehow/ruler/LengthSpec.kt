package io.mehow.ruler

import io.kotlintest.matchers.numerics.shouldBeZero
import io.kotlintest.properties.assertAll
import io.kotlintest.properties.forAll
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotThrowAny
import io.kotlintest.shouldThrow
import io.kotlintest.specs.BehaviorSpec
import kotlin.Long.Companion.MAX_VALUE

class LengthSpec : BehaviorSpec({
  Given("zero length") {
    val zeroLength = Length.Zero

    Then("it should not have any meters") {
      zeroLength.metersPart shouldBe 0L
    }

    Then("it should not have any nanometers") {
      zeroLength.nanosPart.shouldBeZero()
    }

    When("I add it to itself") {
      val newLength = zeroLength + zeroLength

      Then("it is still zero") {
        newLength shouldBe zeroLength
      }
    }

    When("I subtract it from itself") {
      val newLength = zeroLength - zeroLength

      Then("it is still zero") {
        newLength shouldBe zeroLength
      }
    }

    When("I subtract any length from it") {
      Then("it should fail") {
        assertAll(LengthsGenerator(Length.ofNanometers(1))) { length ->
          shouldThrow<IllegalArgumentException> { zeroLength - length }
        }
      }
    }

    When("I add a length to it") {
      Then("it should be equal to that length") {
        assertAll(LengthsGenerator()) { length ->
          val newLength = zeroLength + length
          newLength shouldBe length
        }
      }
    }
  }

  Given("max length") {
    val maxLength = Length.Max

    Then("it should have maximum value of meters") {
      maxLength.metersPart shouldBe MAX_VALUE
    }

    Then("it should have maximum value of nanometers") {
      maxLength.nanosPart shouldBe 999_999_999
    }

    When("I add even one nanometer to it") {
      Then("it fails") {
        shouldThrow<ArithmeticException> { maxLength + Length.create(nanometers = 1) }
      }
    }

    When("I add zero length to it") {
      val newLength = maxLength + Length.Zero

      Then("it stays the same") {
        newLength shouldBe maxLength
      }
    }

    When("I remove any length from it") {
      Then("it does not fail") {
        assertAll(LengthsGenerator()) { length ->
          shouldNotThrowAny { maxLength - length }
        }
      }
    }
  }

  Given("two lengths") {
    When("they are added") {
      Then("nanometer parts should carry over for overflow") {
        assertAll(LongGenerator(0L, 999_999_999), LongGenerator(0L, 999_999_999)) { nano1, nano2 ->
          val totalNanometers = nano1 + nano2
          val shouldCarryOver = totalNanometers >= 1_000_000_000
          val length = Length.create(nanometers = nano1) + Length.create(nanometers = nano2)

          if (shouldCarryOver) length shouldBe Length.create(1, totalNanometers - 1_000_000_000)
          else length shouldBe Length.create(0, totalNanometers)
        }
      }
    }

    When("they are subtracted") {
      Then("nanometer parts should carry over for overflow") {
        assertAll(LongGenerator(0L, 999_999_999), LongGenerator(0L, 999_999_999)) { nano1, nano2 ->
          val nanoDiff = nano1 - nano2
          val shouldCarryOver = nanoDiff < 0
          val length = Length.create(1, nano1) - Length.create(0, nano2)

          if (shouldCarryOver) length shouldBe Length.create(0, 1_000_000_000 + nanoDiff)
          else length shouldBe Length.create(1, nanoDiff)
        }
      }
    }

    Then("they can be compared to each other") {
      forAll(
          LongGenerator(0L, MAX_VALUE),
          LongGenerator(0L, 999_999_999),
          LongGenerator(0L, MAX_VALUE),
          LongGenerator(0L, 999_999_999)
      ) { meter1, nano1, meter2, nano2 ->
        val length1 = Length.create(meter1, nano1)
        val length2 = Length.create(meter2, nano2)

        return@forAll when {
          meter1 > meter2 -> length1 > length2
          meter1 == meter2 && nano1 > nano2 -> length1 > length2
          meter1 == meter2 && nano1 == nano2 -> length1.compareTo(length2) == 0
          else -> length1 < length2
        }
      }
    }
  }
})

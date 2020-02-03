package io.mehow.ruler

import io.kotlintest.matchers.numerics.shouldBeZero
import io.kotlintest.properties.assertAll
import io.kotlintest.properties.forAll
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.BehaviorSpec
import java.math.BigInteger
import kotlin.Long.Companion.MAX_VALUE

class LengthSpec : BehaviorSpec({
  Given("a zero length") {
    val zeroLength = Length.Zero

    Then("it should not have any meters") {
      zeroLength.metersPart shouldBe BigInteger.ZERO
    }

    Then("it should not have any nanometers") {
      zeroLength.nanometersPart.shouldBeZero()
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

  Given("two lengths") {
    When("they are added") {
      Then("nanometer parts should carry over for overflow") {
        assertAll(LongGenerator(0L, 999_999_999), LongGenerator(0L, 999_999_999)) { nano1, nano2 ->
          val totalNanometers = nano1 + nano2
          val shouldCarryOver = totalNanometers >= 1_000_000_000
          val length = Length.ofNanometers(nano1) + Length.ofNanometers(nano2)

          if (shouldCarryOver) length shouldBe Length.create(1, totalNanometers - 1_000_000_000)
          else length shouldBe Length.create(0, totalNanometers)
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

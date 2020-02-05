package io.mehow.ruler

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import io.kotlintest.matchers.string.shouldBeEmpty
import io.kotlintest.shouldBe
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class ImperialDistanceFormatterTest {
  private val context: Context get() = ApplicationProvider.getApplicationContext()

  @Test fun `all distance parts are properly formatted`() {
    val distance = Distance.ofMiles(10) +
        Distance.ofYards(11) +
        Distance.ofFeet(2) +
        Distance.ofInches(7)

    val formattedDistance = ImperialDistanceFormatter.Full.format(distance, context)

    formattedDistance shouldBe "10mi 11yd 2ft 7in"
  }

  @Test fun `formatter handles zero miles value`() {
    val zeroFormatter = ImperialDistanceFormatter.Builder()
        .withMiles(printZeros = true)
        .build()
    val noZeroFormatter = ImperialDistanceFormatter.Builder()
        .withMiles()
        .build()

    val distanceWithZero = zeroFormatter.format(Distance.Zero, context)
    val distanceWithoutZero = noZeroFormatter.format(Distance.Zero, context)

    distanceWithZero shouldBe "0mi"
    distanceWithoutZero.shouldBeEmpty()
  }

  @Test fun `formatter handles zero yards value`() {
    val zeroFormatter = ImperialDistanceFormatter.Builder()
        .withYards(printZeros = true)
        .build()
    val noZeroFormatter = ImperialDistanceFormatter.Builder()
        .withYards()
        .build()

    val distanceWithZero = zeroFormatter.format(Distance.Zero, context)
    val distanceWithoutZero = noZeroFormatter.format(Distance.Zero, context)

    distanceWithZero shouldBe "0yd"
    distanceWithoutZero.shouldBeEmpty()
  }

  @Test fun `formatter handles zero feet value`() {
    val zeroFormatter = ImperialDistanceFormatter.Builder()
        .withFeet(printZeros = true)
        .build()
    val noZeroFormatter = ImperialDistanceFormatter.Builder()
        .withFeet()
        .build()

    val distanceWithZero = zeroFormatter.format(Distance.Zero, context)
    val distanceWithoutZero = noZeroFormatter.format(Distance.Zero, context)

    distanceWithZero shouldBe "0ft"
    distanceWithoutZero.shouldBeEmpty()
  }

  @Test fun `formatter handles zero inches value`() {
    val zeroFormatter = ImperialDistanceFormatter.Builder()
        .withInches(printZeros = true)
        .build()
    val noZeroFormatter = ImperialDistanceFormatter.Builder()
        .withInches()
        .build()

    val distanceWithZero = zeroFormatter.format(Distance.Zero, context)
    val distanceWithoutZero = noZeroFormatter.format(Distance.Zero, context)

    distanceWithZero shouldBe "0in"
    distanceWithoutZero.shouldBeEmpty()
  }

  @Test fun `formatter can be have any order of parts`() {
    val formatter = ImperialDistanceFormatter.Builder()
        .withInches(printZeros = true)
        .withFeet(printZeros = true)
        .withYards(printZeros = true)
        .withMiles(printZeros = true)
        .build()

    val formattedDistance = formatter.format(Distance.Zero, context)

    formattedDistance shouldBe "0in 0ft 0yd 0mi"
  }

  @Test fun `yards formatter accumulates miles part`() {
    val distance = Distance.ofMiles(2) + Distance.ofYards(5)
    val formatter = ImperialDistanceFormatter.Builder()
        .withInches()
        .withFeet()
        .withYards()
        .build()

    val formattedDistance = formatter.format(distance, context)

    formattedDistance shouldBe "3525yd"
  }

  @Test fun `feet formatter accumulates miles and yards part`() {
    val distance = Distance.ofMiles(2) + Distance.ofYards(5) + Distance.ofFeet(3)
    val formatter = ImperialDistanceFormatter.Builder()
        .withInches()
        .withFeet()
        .build()

    val formattedDistance = formatter.format(distance, context)

    formattedDistance shouldBe "10578ft"
  }

  @Test fun `inches formatter accumulates miles, yards and feet part`() {
    val distance = Distance.ofMiles(2) +
        Distance.ofYards(5) +
        Distance.ofFeet(3) +
        Distance.ofInches(11)
    val formatter = ImperialDistanceFormatter.Builder()
        .withInches()
        .build()

    val formattedDistance = formatter.format(distance, context)

    formattedDistance shouldBe "126947in"
  }

  @Test @Config(qualifiers = "ar") fun `parts are reversed for RTL locales`() {
    val formattedDistance = ImperialDistanceFormatter.Full.format(Distance.Zero, context)

    formattedDistance shouldBe "0in 0ft 0yd 0mi"
  }
}

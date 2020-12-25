package io.mehow.ruler

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import io.kotest.matchers.shouldBe
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
internal class ImperialDistanceFormatterTest {
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

    val distanceWithZero = zeroFormatter.format(Distance.Zero, context)

    distanceWithZero shouldBe "0mi"
  }

  @Test fun `formatter handles zero yards value`() {
    val zeroFormatter = ImperialDistanceFormatter.Builder()
        .withMiles()
        .withYards(printZeros = true)
        .build()
    val noZeroFormatter = ImperialDistanceFormatter.Builder()
        .withMiles()
        .withYards()
        .build()

    val distanceWithZero = zeroFormatter.format(Distance.ofMiles(1), context)
    val distanceWithoutZero = noZeroFormatter.format(Distance.ofMiles(1), context)

    distanceWithZero shouldBe "1mi 0yd"
    distanceWithoutZero shouldBe "1mi"
  }

  @Test fun `formatter handles zero feet value`() {
    val zeroFormatter = ImperialDistanceFormatter.Builder()
        .withMiles()
        .withFeet(printZeros = true)
        .build()
    val noZeroFormatter = ImperialDistanceFormatter.Builder()
        .withMiles()
        .withFeet()
        .build()

    val distanceWithZero = zeroFormatter.format(Distance.ofMiles(1), context)
    val distanceWithoutZero = noZeroFormatter.format(Distance.ofMiles(1), context)

    distanceWithZero shouldBe "1mi 0ft"
    distanceWithoutZero shouldBe "1mi"
  }

  @Test fun `formatter handles zero inches value`() {
    val zeroFormatter = ImperialDistanceFormatter.Builder()
        .withMiles()
        .withInches(printZeros = true)
        .build()
    val noZeroFormatter = ImperialDistanceFormatter.Builder()
        .withMiles()
        .withInches()
        .build()

    val distanceWithZero = zeroFormatter.format(Distance.ofMiles(1), context)
    val distanceWithoutZero = noZeroFormatter.format(Distance.ofMiles(1), context)

    distanceWithZero shouldBe "1mi 0in"
    distanceWithoutZero shouldBe "1mi"
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

    formattedDistance shouldBe "0بوصة 0قدم 0ياردة 0ميل"
  }

  @Test fun `zero miles are printed when no mile length is present for miles formatter`() {
    val distance = Distance.ofYards(1_000) + Distance.ofFeet(2) + Distance.ofInches(11)
    val formatter = ImperialDistanceFormatter.Builder()
        .withMiles(printZeros = false)
        .build()

    val formattedDistance = formatter.format(distance, context)

    formattedDistance shouldBe "0mi"
  }

  @Test fun `zero yards are printed when no yard length is present for yards formatter`() {
    val distance = Distance.ofFeet(2) + Distance.ofInches(11)
    val formatter = ImperialDistanceFormatter.Builder()
        .withMiles(printZeros = false)
        .withYards(printZeros = false)
        .build()

    val formattedDistance = formatter.format(distance, context)

    formattedDistance shouldBe "0yd"
  }

  @Test fun `zero feet are printed when no foot length is present for feet formatter`() {
    val distance = Distance.ofInches(11)
    val formatter = ImperialDistanceFormatter.Builder()
        .withMiles(printZeros = false)
        .withYards(printZeros = false)
        .withFeet(printZeros = false)
        .build()

    val formattedDistance = formatter.format(distance, context)

    formattedDistance shouldBe "0ft"
  }

  @Test fun `zero inches are printed when no inch length is present for inches formatter`() {
    val distance = Distance.Zero
    val formatter = ImperialDistanceFormatter.Builder()
        .withMiles(printZeros = false)
        .withYards(printZeros = false)
        .withFeet(printZeros = false)
        .withInches(printZeros = false)
        .build()

    val formattedDistance = formatter.format(distance, context)

    formattedDistance shouldBe "0in"
  }
}

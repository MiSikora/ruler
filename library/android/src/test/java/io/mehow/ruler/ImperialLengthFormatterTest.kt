package io.mehow.ruler

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import io.kotest.matchers.shouldBe
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
internal class ImperialLengthFormatterTest {
  private val context = ApplicationProvider.getApplicationContext<Context>()

  @Test fun `all unit parts are displayed`() {
    val distance = Distance.ofMiles(10) +
        Distance.ofYards(11) +
        Distance.ofFeet(2) +
        Distance.ofInches(7)
    val formatter = ImperialLengthFormatter.Full

    val formattedDistance = distance.format(context, formatter = formatter, converter = null)

    formattedDistance shouldBe "10mi 11yd 2ft 7in"
  }

  @Test fun `zero unit parts are ignored`() {
    val distance = Distance.ofMiles(10)
    val formatter = ImperialLengthFormatter.Full

    val formattedDistance = distance.format(context, formatter = formatter, converter = null)

    formattedDistance shouldBe "10mi"
  }

  @Test fun `zero miles distance is displayed`() {
    val distance = Distance.Zero
    val formatter = ImperialLengthFormatter.Builder()
        .withMiles()
        .build()

    val formattedDistance = distance.format(context, formatter = formatter, converter = null)

    formattedDistance shouldBe "0mi"
  }

  @Test fun `zero yards distance is displayed`() {
    val distance = Distance.Zero
    val formatter = ImperialLengthFormatter.Builder()
        .withYards()
        .build()

    val formattedDistance = distance.format(context, formatter = formatter, converter = null)

    formattedDistance shouldBe "0yd"
  }

  @Test fun `zero feet distance is displayed`() {
    val distance = Distance.Zero
    val formatter = ImperialLengthFormatter.Builder()
        .withFeet()
        .build()

    val formattedDistance = distance.format(context, formatter = formatter, converter = null)

    formattedDistance shouldBe "0ft"
  }

  @Test fun `zero inches distance is displayed`() {
    val distance = Distance.Zero
    val formatter = ImperialLengthFormatter.Builder()
        .withInches()
        .build()

    val formattedDistance = distance.format(context, formatter = formatter, converter = null)

    formattedDistance shouldBe "0in"
  }

  @Test fun `only smallest unit part is displayed for zero distance`() {
    val distance = Distance.Zero
    val formatter = ImperialLengthFormatter.Full

    val formattedDistance = distance.format(context, formatter = formatter, converter = null)

    formattedDistance shouldBe "0in"
  }

  @Test fun `yards accumulate miles`() {
    val distance = Distance.ofMiles(2) + Distance.ofYards(5)
    val formatter = ImperialLengthFormatter.Builder()
        .withYards()
        .build()

    val formattedDistance = distance.format(context, formatter = formatter, converter = null)

    formattedDistance shouldBe "3525yd"
  }

  @Test fun `feet accumulate miles and yards`() {
    val distance = Distance.ofMiles(2) +
        Distance.ofYards(5) +
        Distance.ofFeet(3)
    val formatter = ImperialLengthFormatter.Builder()
        .withFeet()
        .build()

    val formattedDistance = distance.format(context, formatter = formatter, converter = null)

    formattedDistance shouldBe "10578ft"
  }

  @Test fun `inches accumulate miles, yards and feet`() {
    val distance = Distance.ofMiles(2) +
        Distance.ofYards(5) +
        Distance.ofFeet(3) +
        Distance.ofInches(11)
    val formatter = ImperialLengthFormatter.Builder()
        .withInches()
        .build()

    val formattedDistance = distance.format(context, formatter = formatter, converter = null)

    formattedDistance shouldBe "126947in"
  }

  @Test fun `miles do not accumulate yards, feet and inches`() {
    val distance = Distance.ofYards(1759) +
        Distance.ofFeet(2) +
        Distance.ofInches(11)
    val formatter = ImperialLengthFormatter.Builder()
        .withMiles()
        .build()

    val formattedDistance = distance.format(context, formatter = formatter, converter = null)

    formattedDistance shouldBe "0mi"
  }

  @Test fun `yards do not accumulate feet and inches`() {
    val distance = Distance.ofFeet(2) +
        Distance.ofInches(11)
    val formatter = ImperialLengthFormatter.Builder()
        .withYards()
        .build()

    val formattedDistance = distance.format(context, formatter = formatter, converter = null)

    formattedDistance shouldBe "0yd"
  }

  @Test fun `feet do not accumulate inches`() {
    val distance = Distance.ofInches(11)
    val formatter = ImperialLengthFormatter.Builder()
        .withFeet()
        .build()

    val formattedDistance = distance.format(context, formatter = formatter, converter = null)

    formattedDistance shouldBe "0ft"
  }

  @Test @Config(qualifiers = "ar") fun `unit parts are reversed for RTL locales`() {
    val distance = Distance.ofMiles(10) +
        Distance.ofYards(11) +
        Distance.ofFeet(2) +
        Distance.ofInches(7)
    val formatter = ImperialLengthFormatter.Full

    val formattedDistance = distance.format(context, formatter = formatter, converter = null)

    formattedDistance shouldBe "10ميل 11ياردة 2قدم 7بوصة"
  }
}

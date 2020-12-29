package io.mehow.ruler

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import io.kotest.matchers.shouldBe
import io.mehow.ruler.SiLengthUnit.Meter
import io.mehow.ruler.test.format
import io.mehow.ruler.test.getApplicationContext
import io.mehow.ruler.test.localise
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.Locale

@RunWith(RobolectricTestRunner::class)
internal class ImperialLengthFormatterTest {
  private val context = getApplicationContext()

  @Test fun `all unit parts are displayed`() {
    val distance = Distance.ofMiles(10) +
        Distance.ofYards(11) +
        Distance.ofFeet(2) +
        Distance.ofInches(7)
    val length = distance.toLength(Meter)
    val formatter = ImperialLengthFormatter.Full

    val formattedLength = formatter.format(length, context)

    formattedLength shouldBe "10mi 11yd 2ft 7in"
  }

  @Test fun `negative length is displayed`() {
    val distance = -(Distance.ofMiles(10) +
        Distance.ofYards(11) +
        Distance.ofFeet(2) +
        Distance.ofInches(7))
    val length = distance.toLength(Meter)
    val formatter = ImperialLengthFormatter.Full

    val formattedLength = formatter.format(length, context)

    formattedLength shouldBe "-(10mi 11yd 2ft 7in)"
  }

  @Test fun `zero unit parts are ignored`() {
    val distance = Distance.ofMiles(10)
    val length = distance.toLength(Meter)
    val formatter = ImperialLengthFormatter.Full

    val formattedLength = formatter.format(length, context)

    formattedLength shouldBe "10mi"
  }

  @Test fun `zero miles length is displayed`() {
    val distance = Distance.Zero
    val length = distance.toLength(Meter)
    val formatter = ImperialLengthFormatter.Builder()
        .withMiles()
        .build()

    val formattedLength = formatter.format(length, context)

    formattedLength shouldBe "0mi"
  }

  @Test fun `zero yards length is displayed`() {
    val distance = Distance.Zero
    val length = distance.toLength(Meter)
    val formatter = ImperialLengthFormatter.Builder()
        .withYards()
        .build()

    val formattedLength = formatter.format(length, context)

    formattedLength shouldBe "0yd"
  }

  @Test fun `zero feet length is displayed`() {
    val distance = Distance.Zero
    val length = distance.toLength(Meter)
    val formatter = ImperialLengthFormatter.Builder()
        .withFeet()
        .build()

    val formattedLength = formatter.format(length, context)

    formattedLength shouldBe "0ft"
  }

  @Test fun `zero inches length is displayed`() {
    val distance = Distance.Zero
    val length = distance.toLength(Meter)
    val formatter = ImperialLengthFormatter.Builder()
        .withInches()
        .build()

    val formattedLength = formatter.format(length, context)

    formattedLength shouldBe "0in"
  }

  @Test fun `only smallest unit part is displayed for zero length`() {
    val distance = Distance.Zero
    val length = distance.toLength(Meter)
    val formatter = ImperialLengthFormatter.Full

    val formattedLength = formatter.format(length, context)

    formattedLength shouldBe "0in"
  }

  @Test fun `yards accumulate miles`() {
    val distance = Distance.ofMiles(2) + Distance.ofYards(5)
    val length = distance.toLength(Meter)
    val formatter = ImperialLengthFormatter.Builder()
        .withYards()
        .build()

    val formattedLength = formatter.format(length, context)

    formattedLength shouldBe "3525yd"
  }

  @Test fun `feet accumulate miles and yards`() {
    val distance = Distance.ofMiles(2) +
        Distance.ofYards(5) +
        Distance.ofFeet(3)
    val length = distance.toLength(Meter)
    val formatter = ImperialLengthFormatter.Builder()
        .withFeet()
        .build()

    val formattedLength = formatter.format(length, context)

    formattedLength shouldBe "10578ft"
  }

  @Test fun `inches accumulate miles, yards and feet`() {
    val distance = Distance.ofMiles(2) +
        Distance.ofYards(5) +
        Distance.ofFeet(3) +
        Distance.ofInches(11)
    val length = distance.toLength(Meter)
    val formatter = ImperialLengthFormatter.Builder()
        .withInches()
        .build()

    val formattedLength = formatter.format(length, context)

    formattedLength shouldBe "126947in"
  }

  @Test fun `miles do not accumulate yards, feet and inches`() {
    val distance = Distance.ofYards(1759) +
        Distance.ofFeet(2) +
        Distance.ofInches(11)
    val length = distance.toLength(Meter)
    val formatter = ImperialLengthFormatter.Builder()
        .withMiles()
        .build()

    val formattedLength = formatter.format(length, context)

    formattedLength shouldBe "0mi"
  }

  @Test fun `yards do not accumulate feet and inches`() {
    val distance = Distance.ofFeet(2) +
        Distance.ofInches(11)
    val length = distance.toLength(Meter)
    val formatter = ImperialLengthFormatter.Builder()
        .withYards()
        .build()

    val formattedLength = formatter.format(length, context)

    formattedLength shouldBe "0yd"
  }

  @Test fun `feet do not accumulate inches`() {
    val distance = Distance.ofInches(11)
    val length = distance.toLength(Meter)
    val formatter = ImperialLengthFormatter.Builder()
        .withFeet()
        .build()

    val formattedLength = formatter.format(length, context)

    formattedLength shouldBe "0ft"
  }

  @Test fun `unit parts are reversed with RTL locale`() {
    val context = context.localise(Locale("ar"))
    val distance = Distance.ofMiles(4) +
        Distance.ofYards(3) +
        Distance.ofFeet(2) +
        Distance.ofInches(1)
    val length = distance.toLength(Meter)
    val formatter = ImperialLengthFormatter.Full

    val formattedLength = formatter.format(length, context)

    formattedLength shouldBe "4ميل 3ياردة 2قدم 1بوصة"
  }

  @Test fun `imperial formatter without specified parts has a fallback`() {
    val distance = Distance.ofMiles(10) +
        Distance.ofYards(11) +
        Distance.ofFeet(2) +
        Distance.ofInches(7)
    val length = distance.toLength(Meter)
    val formatter = ImperialLengthFormatter.Builder().build()

    val formattedLength = formatter.format(length, context)

    formattedLength shouldBe "17611yd"
  }

  @Test fun `custom unit separator is displayed in a correct position`() {
    val distance = Distance.ofMiles(4) +
        Distance.ofYards(3) +
        Distance.ofFeet(2) +
        Distance.ofInches(1)
    val length = distance.toLength(Meter)
    val formatter = ImperialLengthFormatter.Full

    val formattedLength = formatter.format(length, context, "|")

    formattedLength shouldBe "4|mi 3|yd 2|ft 1|in"
  }

  @Test fun `custom unit separator is displayed in a correct position with RTL locale`() {
    val context = context.localise(Locale("ar"))
    val distance = Distance.ofMiles(4) +
        Distance.ofYards(3) +
        Distance.ofFeet(2) +
        Distance.ofInches(1)
    val length = distance.toLength(Meter)
    val formatter = ImperialLengthFormatter.Full

    val formattedLength = formatter.format(length, context, "|")

    formattedLength shouldBe "4|ميل 3|ياردة 2|قدم 1|بوصة"
  }



  @Test fun `custom part separator is displayed in a correct position`() {
    val distance = Distance.ofMiles(4) +
        Distance.ofYards(3) +
        Distance.ofFeet(2) +
        Distance.ofInches(1)
    val length = distance.toLength(Meter)
    val formatter = ImperialLengthFormatter.Builder()
        .withMiles()
        .withYards()
        .withFeet()
        .withInches()
        .withPartSeparator("|")
        .build()

    val formattedLength = formatter.format(length, context)

    formattedLength shouldBe "4mi|3yd|2ft|1in"
  }

  @Test fun `custom part separator is displayed in a correct position with RTL locale`() {
    val context = context.localise(Locale("ar"))
    val distance = Distance.ofMiles(4) +
        Distance.ofYards(3) +
        Distance.ofFeet(2) +
        Distance.ofInches(1)
    val length = distance.toLength(Meter)
    val formatter = ImperialLengthFormatter.Builder()
        .withMiles()
        .withYards()
        .withFeet()
        .withInches()
        .withPartSeparator("|")
        .build()

    val formattedLength = formatter.format(length, context)

    formattedLength shouldBe "4ميل|3ياردة|2قدم|1بوصة"
  }
}

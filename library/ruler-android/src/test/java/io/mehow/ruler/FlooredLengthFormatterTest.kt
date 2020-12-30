package io.mehow.ruler

import io.kotest.matchers.shouldBe
import io.mehow.ruler.ImperialLengthUnit.Foot
import io.mehow.ruler.ImperialLengthUnit.Inch
import io.mehow.ruler.ImperialLengthUnit.Mile
import io.mehow.ruler.ImperialLengthUnit.Yard
import io.mehow.ruler.SiLengthUnit.Centimeter
import io.mehow.ruler.SiLengthUnit.Decameter
import io.mehow.ruler.SiLengthUnit.Decimeter
import io.mehow.ruler.SiLengthUnit.Gigameter
import io.mehow.ruler.SiLengthUnit.Hectometer
import io.mehow.ruler.SiLengthUnit.Kilometer
import io.mehow.ruler.SiLengthUnit.Megameter
import io.mehow.ruler.SiLengthUnit.Meter
import io.mehow.ruler.SiLengthUnit.Micrometer
import io.mehow.ruler.SiLengthUnit.Millimeter
import io.mehow.ruler.SiLengthUnit.Nanometer
import io.mehow.ruler.test.format
import io.mehow.ruler.test.getApplicationContext
import io.mehow.ruler.test.localise
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.Locale

@RunWith(RobolectricTestRunner::class)
internal class FlooredLengthFormatterTest {
  private val context = getApplicationContext()

  @Test fun `max length is displayed without overflow`() {
    val length = Distance.Max.toLength(Nanometer)

    FlooredLengthFormatter.format(length, context) shouldBe "9,223,372,036,854,775,807,999,999,999nm"
  }

  @Test fun `min length is displayed without overflow`() {
    val length = Distance.Min.toLength(Nanometer)

    FlooredLengthFormatter.format(length, context) shouldBe "-9,223,372,036,854,775,807,999,999,999nm"
  }

  @Test fun `positive length is displayed correctly with RTL locale`() {
    val context = context.localise(Locale("ar"))
    val length = Length.ofMeters(12.36)

    FlooredLengthFormatter.format(length, context) shouldBe "١٢م"
  }

  @Test fun `negative length is displayed correctly with RTL locale`() {
    val context = context.localise(Locale("ar"))
    val length = Length.ofMeters(-12.36)

    FlooredLengthFormatter.format(length, context) shouldBe "؜-١٢م"
  }

  @Test fun `custom separator is displayed in a correct position`() {
    val length = Length.ofMeters(12.36)

    FlooredLengthFormatter.format(length, context, "|") shouldBe "12|m"
  }

  @Test fun `custom separator is displayed in a correct position with RTL locale`() {
    val context = context.localise(Locale("ar"))
    val length = Length.ofMeters(12.36)

    FlooredLengthFormatter.format(length, context, "|") shouldBe "١٢|م"
  }

  private val rainbowSiDistance = Distance.ofNanometers(1) +
      Distance.ofMicrometers(2) +
      Distance.ofMillimeters(3) +
      Distance.ofCentimeters(4) +
      Distance.ofDecimeters(5) +
      Distance.ofMeters(6) +
      Distance.ofDecameters(7) +
      Distance.ofHectometers(8) +
      Distance.ofKilometers(9) +
      Distance.ofMegameters(10) +
      Distance.ofGigameters(11)

  @Test fun `gigameters do not display smaller unit parts`() {
    val length = rainbowSiDistance.toLength(Gigameter)

    FlooredLengthFormatter.format(length, context) shouldBe "11Gm"
  }

  @Test fun `gigameters display zero distance`() {
    val length = Distance.Zero.toLength(Gigameter)

    FlooredLengthFormatter.format(length, context) shouldBe "0Gm"
  }

  @Test fun `megameters do not display smaller unit parts`() {
    val length = rainbowSiDistance.toLength(Megameter)

    FlooredLengthFormatter.format(length, context) shouldBe "11,010Mm"
  }

  @Test fun `megameters display zero distance`() {
    val length = Distance.Zero.toLength(Megameter)

    FlooredLengthFormatter.format(length, context) shouldBe "0Mm"
  }

  @Test fun `kilometers do not display smaller unit parts`() {
    val length = rainbowSiDistance.toLength(Kilometer)

    FlooredLengthFormatter.format(length, context) shouldBe "11,010,009km"
  }

  @Test fun `kilometers display zero distance`() {
    val length = Distance.Zero.toLength(Kilometer)

    FlooredLengthFormatter.format(length, context) shouldBe "0km"
  }

  @Test fun `hectometers do not display smaller unit parts`() {
    val length = rainbowSiDistance.toLength(Hectometer)

    FlooredLengthFormatter.format(length, context) shouldBe "110,100,098hm"
  }

  @Test fun `hectometers display zero distance`() {
    val length = Distance.Zero.toLength(Hectometer)

    FlooredLengthFormatter.format(length, context) shouldBe "0hm"
  }

  @Test fun `decameters do not display smaller unit parts`() {
    val length = rainbowSiDistance.toLength(Decameter)

    FlooredLengthFormatter.format(length, context) shouldBe "1,101,000,987dam"
  }

  @Test fun `decameters display zero distance`() {
    val length = Distance.Zero.toLength(Decameter)

    FlooredLengthFormatter.format(length, context) shouldBe "0dam"
  }

  @Test fun `meters do not display smaller unit parts`() {
    val length = rainbowSiDistance.toLength(Meter)

    FlooredLengthFormatter.format(length, context) shouldBe "11,010,009,876m"
  }

  @Test fun `meters display zero distance`() {
    val length = Distance.Zero.toLength(Meter)

    FlooredLengthFormatter.format(length, context) shouldBe "0m"
  }

  @Test fun `decimeters do not display smaller unit parts`() {
    val length = rainbowSiDistance.toLength(Decimeter)

    FlooredLengthFormatter.format(length, context) shouldBe "110,100,098,765dm"
  }

  @Test fun `decimeters display zero distance`() {
    val length = Distance.Zero.toLength(Decimeter)

    FlooredLengthFormatter.format(length, context) shouldBe "0dm"
  }

  @Test fun `centimeters do not display smaller unit parts`() {
    val length = rainbowSiDistance.toLength(Centimeter)

    FlooredLengthFormatter.format(length, context) shouldBe "1,101,000,987,654cm"
  }

  @Test fun `centimeters display zero distance`() {
    val length = Distance.Zero.toLength(Centimeter)

    FlooredLengthFormatter.format(length, context) shouldBe "0cm"
  }

  @Test fun `millimeters do not display smaller unit parts`() {
    val length = rainbowSiDistance.toLength(Millimeter)

    FlooredLengthFormatter.format(length, context) shouldBe "11,010,009,876,543mm"
  }

  @Test fun `millimeters display zero distance`() {
    val length = Distance.Zero.toLength(Millimeter)

    FlooredLengthFormatter.format(length, context) shouldBe "0mm"
  }

  @Test fun `micrometers do not display smaller unit parts`() {
    val length = rainbowSiDistance.toLength(Micrometer)

    FlooredLengthFormatter.format(length, context) shouldBe "11,010,009,876,543,002µm"
  }

  @Test fun `micrometers display zero distance`() {
    val length = Distance.Zero.toLength(Micrometer)

    FlooredLengthFormatter.format(length, context) shouldBe "0µm"
  }

  @Test fun `nanometers display all unit parts`() {
    val length = rainbowSiDistance.toLength(Nanometer)

    FlooredLengthFormatter.format(length, context) shouldBe "11,010,009,876,543,002,001nm"
  }

  @Test fun `nanometers display zero distance`() {
    val length = Distance.Zero.toLength(Nanometer)

    FlooredLengthFormatter.format(length, context) shouldBe "0nm"
  }

  private val rainbowImperialDistance = Distance.ofInches(1) +
      Distance.ofFeet(2) +
      Distance.ofYards(3) +
      Distance.ofMiles(4)

  @Test fun `miles do not display smaller unit parts`() {
    val length = rainbowImperialDistance.toLength(Mile)

    FlooredLengthFormatter.format(length, context) shouldBe "4mi"
  }

  @Test fun `miles display zero distance`() {
    val length = Distance.Zero.toLength(Mile)

    FlooredLengthFormatter.format(length, context) shouldBe "0mi"
  }

  @Test fun `yards do not display smaller unit parts`() {
    val length = rainbowImperialDistance.toLength(Yard)

    FlooredLengthFormatter.format(length, context) shouldBe "7,043yd"
  }

  @Test fun `yards display zero distance`() {
    val length = Distance.Zero.toLength(Yard)

    FlooredLengthFormatter.format(length, context) shouldBe "0yd"
  }

  @Test fun `feet do not display smaller unit parts`() {
    val length = rainbowImperialDistance.toLength(Foot)

    FlooredLengthFormatter.format(length, context) shouldBe "21,131ft"
  }

  @Test fun `feet display zero distance`() {
    val length = Distance.Zero.toLength(Foot)

    FlooredLengthFormatter.format(length, context) shouldBe "0ft"
  }

  @Test fun `inches display all unit parts`() {
    val length = rainbowImperialDistance.toLength(Inch)

    FlooredLengthFormatter.format(length, context) shouldBe "253,573in"
  }

  @Test fun `inches display zero distance`() {
    val length = Distance.Zero.toLength(Inch)

    FlooredLengthFormatter.format(length, context) shouldBe "0in"
  }
}

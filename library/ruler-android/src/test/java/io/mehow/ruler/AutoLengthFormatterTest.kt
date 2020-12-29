package io.mehow.ruler

import io.kotest.matchers.shouldBe
import io.mehow.ruler.SiLengthUnit.Nanometer
import io.mehow.ruler.test.format
import io.mehow.ruler.test.getApplicationContext
import io.mehow.ruler.test.localise
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.Locale

@RunWith(RobolectricTestRunner::class)
internal class AutoLengthFormatterTest {
  private val context = getApplicationContext()

  @Test fun `max length is displayed without overflow`() {
    val length = Distance.Max.toLength(Nanometer)

    AutoLengthFormatter.format(length, context) shouldBe "9223372036854776000000000000.00nm"
  }

  @Test fun `min length is displayed without overflow`() {
    val length = Distance.Min.toLength(Nanometer)

    AutoLengthFormatter.format(length, context) shouldBe "-9223372036854776000000000000.00nm"
  }

  @Test fun `positive length is displayed correctly with RTL locale`() {
    val context = context.localise(Locale("ar"))
    val length = Length.ofMeters(12.36)

    AutoLengthFormatter.format(length, context) shouldBe "12.36م"
  }

  @Test fun `negative length is displayed correctly with RTL locale`() {
    val context = context.localise(Locale("ar"))
    val length = Length.ofMeters(-12.36)

    AutoLengthFormatter.format(length, context) shouldBe "-12.36م"
  }

  @Test fun `custom separator is displayed in a correct position`() {
    val length = Length.ofMeters(12.36)

    AutoLengthFormatter.format(length, context, "|") shouldBe "12.36|m"
  }

  @Test fun `custom separator is displayed in a correct position with RTL locale`() {
    val context = context.localise(Locale("ar"))
    val length = Length.ofMeters(12.36)

    AutoLengthFormatter.format(length, context, "|") shouldBe "12.36|م"
  }

  @Test fun `correct decimal point character is used for different locale`() {
    val context = context.localise(Locale("pl"))
    val length = Length.ofMeters(12.36)

    AutoLengthFormatter.format(length, context) shouldBe "12,36m"
  }

  @Test fun `gigameters length is properly formatted`() {
    val length = Length.ofGigameters(15.642)

    AutoLengthFormatter.format(length, context) shouldBe "15.64Gm"
  }

  @Test fun `megameters length is properly formatted`() {
    val length = Length.ofMegameters(19.091)

    AutoLengthFormatter.format(length, context) shouldBe "19.09Mm"
  }

  @Test fun `kilometers length is properly formatted`() {
    val length = Length.ofKilometers(891.119)

    AutoLengthFormatter.format(length, context) shouldBe "891.12km"
  }

  @Test fun `hectometers length is properly formatted`() {
    val length = Length.ofHectometers(111.222)

    AutoLengthFormatter.format(length, context) shouldBe "111.22hm"
  }

  @Test fun `decameters length is properly formatted`() {
    val length = Length.ofDecameters(6_314.321)

    AutoLengthFormatter.format(length, context) shouldBe "6314.32dam"
  }

  @Test fun `meters length is properly formatted`() {
    val length = Length.ofMeters(7.111)

    AutoLengthFormatter.format(length, context) shouldBe "7.11m"
  }

  @Test fun `decimeters length is properly formatted`() {
    val length = Length.ofDecimeters(895.0)

    AutoLengthFormatter.format(length, context) shouldBe "895.00dm"
  }

  @Test fun `centimeters length is properly formatted`() {
    val length = Length.ofCentimeters(11_312.820_123)

    AutoLengthFormatter.format(length, context) shouldBe "11312.82cm"
  }

  @Test fun `micrometers length is properly formatted`() {
    val length = Length.ofMicrometers(904.55)

    AutoLengthFormatter.format(length, context) shouldBe "904.55µm"
  }

  @Test fun `nanometers length is properly formatted`() {
    val length = Length.ofNanometers(12.09)

    // Distance can't hold any decimal part of nanometers.
    AutoLengthFormatter.format(length, context) shouldBe "12.00nm"
  }

  @Test fun `miles length is properly formatted`() {
    val length = Length.ofMiles(338.1)

    AutoLengthFormatter.format(length, context) shouldBe "338.10mi"
  }

  @Test fun `yards length is properly formatted`() {
    val length = Length.ofYards(120.120)

    AutoLengthFormatter.format(length, context) shouldBe "120.12yd"
  }

  @Test fun `feet length is properly formatted`() {
    val length = Length.ofFeet(17.999)

    AutoLengthFormatter.format(length, context) shouldBe "18.00ft"
  }

  @Test fun `inches length is properly formatted`() {
    val length = Length.ofInches(999.99999)

    AutoLengthFormatter.format(length, context) shouldBe "1000.00in"
  }
}

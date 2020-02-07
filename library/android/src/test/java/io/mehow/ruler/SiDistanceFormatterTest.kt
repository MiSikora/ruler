package io.mehow.ruler

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import io.kotlintest.shouldBe
import io.mehow.ruler.SiLengthUnit.Gigameter
import io.mehow.ruler.SiLengthUnit.Kilometer
import io.mehow.ruler.SiLengthUnit.Megameter
import io.mehow.ruler.SiLengthUnit.Meter
import io.mehow.ruler.SiLengthUnit.Micrometer
import io.mehow.ruler.SiLengthUnit.Millimeter
import io.mehow.ruler.SiLengthUnit.Nanometer
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SiDistanceFormatterTest {
  private val context: Context get() = ApplicationProvider.getApplicationContext()

  @Test fun `gigameters are properly formatted`() {
    val distance = Distance.ofGigameters(1) +
        Distance.ofMegameters(1) +
        Distance.ofKilometers(1) +
        Distance.ofMeters(1) +
        Distance.ofMillimeters(1) +
        Distance.ofMicrometers(1) +
        Distance.ofNanometers(1)

    val formattedDistance = distance.formatFloored(context, Gigameter)

    formattedDistance shouldBe "1Gm"
  }

  @Test fun `megameters are properly formatted`() {
    val distance = Distance.ofGigameters(1) +
        Distance.ofMegameters(1) +
        Distance.ofKilometers(1) +
        Distance.ofMeters(1) +
        Distance.ofMillimeters(1) +
        Distance.ofMicrometers(1) +
        Distance.ofNanometers(1)

    val formattedDistance = distance.formatFloored(context, Megameter)

    formattedDistance shouldBe "1001Mm"
  }

  @Test fun `kilometers are properly formatted`() {
    val distance = Distance.ofGigameters(1) +
        Distance.ofMegameters(1) +
        Distance.ofKilometers(1) +
        Distance.ofMeters(1) +
        Distance.ofMillimeters(1) +
        Distance.ofMicrometers(1) +
        Distance.ofNanometers(1)

    val formattedDistance = distance.formatFloored(context, Kilometer)

    formattedDistance shouldBe "1001001km"
  }

  @Test fun `meters are properly formatted`() {
    val distance = Distance.ofGigameters(1) +
        Distance.ofMegameters(1) +
        Distance.ofKilometers(1) +
        Distance.ofMeters(1) +
        Distance.ofMillimeters(1) +
        Distance.ofMicrometers(1) +
        Distance.ofNanometers(1)

    val formattedDistance = distance.formatFloored(context, Meter)

    formattedDistance shouldBe "1001001001m"
  }

  @Test fun `millimeters are properly formatted`() {
    val distance = Distance.ofGigameters(1) +
        Distance.ofMegameters(1) +
        Distance.ofKilometers(1) +
        Distance.ofMeters(1) +
        Distance.ofMillimeters(1) +
        Distance.ofMicrometers(1) +
        Distance.ofNanometers(1)

    val formattedDistance = distance.formatFloored(context, Millimeter)

    formattedDistance shouldBe "1001001001001mm"
  }

  @Test fun `micrometers are properly formatted`() {
    val distance = Distance.ofGigameters(1) +
        Distance.ofMegameters(1) +
        Distance.ofKilometers(1) +
        Distance.ofMeters(1) +
        Distance.ofMillimeters(1) +
        Distance.ofMicrometers(1) +
        Distance.ofNanometers(1)

    val formattedDistance = distance.formatFloored(context, Micrometer)

    formattedDistance shouldBe "1001001001001001µm"
  }

  @Test fun `nanometers are properly formatted`() {
    val distance = Distance.ofGigameters(1) +
        Distance.ofMegameters(1) +
        Distance.ofKilometers(1) +
        Distance.ofMeters(1) +
        Distance.ofMillimeters(1) +
        Distance.ofMicrometers(1) +
        Distance.ofNanometers(1)

    val formattedDistance = distance.formatFloored(context, Nanometer)

    formattedDistance shouldBe "1001001001001001001nm"
  }

  @Test fun `gigameters are formatting zeros properly`() {
    val distance = Distance.ofMegameters(999) +
        Distance.ofKilometers(999) +
        Distance.ofMeters(999) +
        Distance.ofMillimeters(999) +
        Distance.ofMicrometers(999) +
        Distance.ofNanometers(999)

    val formattedDistance = distance.formatFloored(context, Gigameter)

    formattedDistance shouldBe "0Gm"
  }

  @Test fun `megameters are formatting zeros properly`() {
    val distance = Distance.ofKilometers(999) +
        Distance.ofMeters(999) +
        Distance.ofMillimeters(999) +
        Distance.ofMicrometers(999) +
        Distance.ofNanometers(999)

    val formattedDistance = distance.formatFloored(context, Megameter)

    formattedDistance shouldBe "0Mm"
  }

  @Test fun `kilometers are formatting zeros properly`() {
    val distance = Distance.ofMeters(999) +
        Distance.ofMillimeters(999) +
        Distance.ofMicrometers(999) +
        Distance.ofNanometers(999)

    val formattedDistance = distance.formatFloored(context, Kilometer)

    formattedDistance shouldBe "0km"
  }

  @Test fun `meters are formatting zeros properly`() {
    val distance = Distance.ofMillimeters(999) +
        Distance.ofMicrometers(999) +
        Distance.ofNanometers(999)

    val formattedDistance = distance.formatFloored(context, Meter)

    formattedDistance shouldBe "0m"
  }

  @Test fun `millimeters are formatting zeros properly`() {
    val distance = Distance.ofMicrometers(999) +
        Distance.ofNanometers(999)

    val formattedDistance = distance.formatFloored(context, Millimeter)

    formattedDistance shouldBe "0mm"
  }

  @Test fun `micrometers are formatting zeros properly`() {
    val distance = Distance.ofNanometers(999)

    val formattedDistance = distance.formatFloored(context, Micrometer)

    formattedDistance shouldBe "0µm"
  }

  @Test fun `nanometers are formatting zeros properly`() {
    val distance = Distance.Zero

    val formattedDistance = distance.formatFloored(context, Nanometer)

    formattedDistance shouldBe "0nm"
  }
}

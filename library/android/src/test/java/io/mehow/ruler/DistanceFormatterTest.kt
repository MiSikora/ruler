package io.mehow.ruler

import android.content.Context
import android.content.res.Configuration
import androidx.test.core.app.ApplicationProvider
import io.kotlintest.shouldBe
import io.mehow.ruler.ImperialDistanceUnit.Foot
import io.mehow.ruler.ImperialDistanceUnit.Inch
import io.mehow.ruler.ImperialDistanceUnit.Mile
import io.mehow.ruler.ImperialDistanceUnit.Yard
import io.mehow.ruler.SiDistanceUnit.Gigameter
import io.mehow.ruler.SiDistanceUnit.Kilometer
import io.mehow.ruler.SiDistanceUnit.Megameter
import io.mehow.ruler.SiDistanceUnit.Meter
import io.mehow.ruler.SiDistanceUnit.Micrometer
import io.mehow.ruler.SiDistanceUnit.Millimeter
import io.mehow.ruler.SiDistanceUnit.Nanometer
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.Locale

@RunWith(RobolectricTestRunner::class)
class DistanceFormatterTest {
  private val context: Context get() = ApplicationProvider.getApplicationContext()

  @Test fun `nanometer distance is properly formatted`() {
    val length = Length.ofNanometers(123456789)
    val distance = length.toDistance(Nanometer)

    val formattedDistance = distance.format(context, converter = null)

    formattedDistance shouldBe "123456789.00nm"
  }

  @Test fun `micrometer distance is properly formatted`() {
    val length = Length.ofMicrometers(666) + Length.ofNanometers(333)
    val distance = length.toDistance(Micrometer)

    val formattedDistance = distance.format(context, converter = null)

    formattedDistance shouldBe "666.33µm"
  }

  @Test fun `millimeter distance is properly formatted`() {
    val length = Length.ofMeters(20) + Length.ofMillimeters(789)
    val distance = length.toDistance(Millimeter)

    val formattedDistance = distance.format(context, converter = null)

    formattedDistance shouldBe "20789.00mm"
  }

  @Test fun `meter distance is properly formatted`() {
    val length = Length.ofMeters(20) + Length.ofMillimeters(789)
    val distance = length.toDistance(Meter)

    val formattedDistance = distance.format(context, converter = null)

    formattedDistance shouldBe "20.79m"
  }

  @Test fun `kilometer distance is properly formatted`() {
    val length = Length.ofKilometers(1331) + Length.ofNanometers(500)
    val distance = length.toDistance(Kilometer)

    val formattedDistance = distance.format(context, converter = null)

    formattedDistance shouldBe "1331.00km"
  }

  @Test fun `megameter distance is properly formatted`() {
    val length = Length.ofMegameters(600) + Length.ofMeters(28000)
    val distance = length.toDistance(Megameter)

    val formattedDistance = distance.format(context, converter = null)

    formattedDistance shouldBe "600.03Mm"
  }

  @Test fun `gigameter distance is properly formatted`() {
    val length = Length.ofGigameters(2_573_213)
    val distance = length.toDistance(Gigameter)

    val formattedDistance = distance.format(context, converter = null)

    formattedDistance shouldBe "2573213.00Gm"
  }

  @Test fun `inches distance is properly formatted`() {
    val length = Length.ofInches(86)
    val distance = length.toDistance(Inch)

    val formattedDistance = distance.format(context, converter = null)

    formattedDistance shouldBe "86.00in"
  }

  @Test fun `foot distance is properly formatted`() {
    val length = Length.ofFeet(111)
    val distance = length.toDistance(Foot)

    val formattedDistance = distance.format(context, converter = null)

    formattedDistance shouldBe "111.00ft"
  }

  @Test fun `yard distance is properly formatted`() {
    val length = Length.ofYards(8912)
    val distance = length.toDistance(Yard)

    val formattedDistance = distance.format(context, converter = null)

    formattedDistance shouldBe "8912.00yd"
  }

  @Test fun `mile distance is properly formatted`() {
    val length = Length.ofMiles(69)
    val distance = length.toDistance(Mile)

    val formattedDistance = distance.format(context, converter = null)

    formattedDistance shouldBe "69.00mi"
  }

  @Test fun `formatting uses provided separator`() {
    val length = Length.ofMeters(1)
    val distance = length.toDistance(Meter)

    val formattedDistance = distance.format(context, unitSeparator = "-", converter = null)

    formattedDistance shouldBe "1.00-m"
  }

  @Test fun `formatting uses provided resource provider`() {
    val length = Length.ofMeters(1)
    val distance = length.toDistance(Meter)

    val formattedDistance = distance.format(
        context,
        resourceProvider = object : DistanceUnitResourceProvider<DistanceUnit> {
          override fun resource(unit: DistanceUnit) = R.string.io_mehow_ruler_yards
        },
        converter = null
    )

    formattedDistance shouldBe "1.00yd"
  }

  @Test fun `formatting uses kilometers for large values with SI units`() {
    val length = Length.ofGigameters(1)
    val distance = length.toDistance(Gigameter)

    val formattedDistance = distance.format(context)

    formattedDistance shouldBe "1000000.00km"
  }

  @Test fun `formatting uses meters for small values with SI units`() {
    val length = Length.ofMillimeters(987)
    val distance = length.toDistance(Millimeter)

    val formattedDistance = distance.format(context)

    formattedDistance shouldBe "0.99m"
  }

  @Test fun `formatting uses provided converter`() {
    val length = Length.ofMillimeters(987)
    val distance = length.toDistance(Millimeter)

    val formattedDistance = distance.format(
        context,
        converter = object : DistanceConverter {
          override fun convert(distance: Distance<*>) = distance.withUnit(Nanometer)
        }
    )

    formattedDistance shouldBe "987000000.00nm"
  }

  @Test fun `auto formatting uses system locale by default`() {
    // Only language of locale can be set with @Config and we need to set country.
    val config = context.resources.configuration
    val localizedConfig = Configuration(config).apply { setLocale(Locale.US) }
    val localizedContext = context.createConfigurationContext(localizedConfig)

    val length = Length.ofInches(30)
    val distance = length.toDistance(Meter)

    val formattedDistance = distance.formatLocalized(localizedContext)

    formattedDistance shouldBe "2.50ft"
  }

  @Test @Config(qualifiers = "pl")
  fun `provided locale overwrites system ones for auto formatting`() {
    val length = Length.ofYards(3)
    val distance = length.toDistance(Meter)

    val formattedDistance = distance.formatLocalized(context, locale = Locale.US)

    formattedDistance shouldBe "3.00yd"
  }
}

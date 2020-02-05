package io.mehow.ruler

import android.content.Context
import android.content.res.Configuration
import androidx.test.core.app.ApplicationProvider
import io.kotlintest.shouldBe
import io.mehow.ruler.ImperialLengthUnit.Foot
import io.mehow.ruler.ImperialLengthUnit.Inch
import io.mehow.ruler.ImperialLengthUnit.Mile
import io.mehow.ruler.ImperialLengthUnit.Yard
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
import java.util.Locale

@RunWith(RobolectricTestRunner::class)
class RulerTest {
  private val context: Context get() = ApplicationProvider.getApplicationContext()

  @Test fun `nanometer length is properly formatted`() {
    val distance = Distance.ofNanometers(123456789)
    val length = distance.toLength(Nanometer)

    val formattedLength = length.format(context, converter = null)

    formattedLength shouldBe "123456789.00nm"
  }

  @Test fun `micrometer length is properly formatted`() {
    val distance = Distance.ofMicrometers(666) + Distance.ofNanometers(333)
    val length = distance.toLength(Micrometer)

    val formattedLength = length.format(context, converter = null)

    formattedLength shouldBe "666.33µm"
  }

  @Test fun `millimeter length is properly formatted`() {
    val distance = Distance.ofMeters(20) + Distance.ofMillimeters(789)
    val length = distance.toLength(Millimeter)

    val formattedLength = length.format(context, converter = null)

    formattedLength shouldBe "20789.00mm"
  }

  @Test fun `meter length is properly formatted`() {
    val distance = Distance.ofMeters(20) + Distance.ofMillimeters(789)
    val length = distance.toLength(Meter)

    val formattedLength = length.format(context, converter = null)

    formattedLength shouldBe "20.79m"
  }

  @Test fun `kilometer length is properly formatted`() {
    val distance = Distance.ofKilometers(1331) + Distance.ofNanometers(500)
    val length = distance.toLength(Kilometer)

    val formattedLength = length.format(context, converter = null)

    formattedLength shouldBe "1331.00km"
  }

  @Test fun `megameter length is properly formatted`() {
    val distance = Distance.ofMegameters(600) + Distance.ofMeters(28000)
    val length = distance.toLength(Megameter)

    val formattedLength = length.format(context, converter = null)

    formattedLength shouldBe "600.03Mm"
  }

  @Test fun `gigameter length is properly formatted`() {
    val distance = Distance.ofGigameters(2_573_213)
    val length = distance.toLength(Gigameter)

    val formattedLength = length.format(context, converter = null)

    formattedLength shouldBe "2573213.00Gm"
  }

  @Test fun `inches length is properly formatted`() {
    val distance = Distance.ofInches(86)
    val length = distance.toLength(Inch)

    val formattedLength = length.format(context, converter = null)

    formattedLength shouldBe "2yd 1ft"
  }

  @Test fun `length length is properly formatted`() {
    val distance = Distance.ofFeet(111)
    val length = distance.toLength(Foot)

    val formattedLength = length.format(context, converter = null)

    formattedLength shouldBe "37yd"
  }

  @Test fun `yard length is properly formatted`() {
    val distance = Distance.ofMiles(4) + Distance.ofYards(666)
    val length = distance.toLength(Yard)

    val formattedLength = length.format(context, converter = null)

    formattedLength shouldBe "4mi 666yd"
  }

  @Test fun `mile length is properly formatted`() {
    val distance = Distance.ofMiles(69)
    val length = distance.toLength(Mile)

    val formattedLength = length.format(context, converter = null)

    formattedLength shouldBe "69mi"
  }

  @Test fun `formatting uses provided separator`() {
    val distance = Distance.ofMeters(1)
    val length = distance.toLength(Meter)

    val formattedLength = length.format(context, separator = "-", converter = null)

    formattedLength shouldBe "1.00-m"
  }

  @Test fun `formatting uses provided resource provider`() {
    val distance = Distance.ofMeters(1)
    val length = distance.toLength(Meter)

    val formattedLength = length.format(
        context,
        converter = null,
        formatter = object : LengthFormatter {
          override fun Length<*>.format(context: Context, separator: String): String? {
            return context.getString(R.string.io_mehow_ruler_yards, measuredLength, separator)
          }
        }
    )

    formattedLength shouldBe "1.00yd"
  }

  @Test fun `formatting uses kilometers for large values with SI units`() {
    // Only language of locale can be set with @Config and we need to set country.
    val config = context.resources.configuration
    val localizedConfig = Configuration(config).apply { setLocale(Locale.UK) }
    val localizedContext = context.createConfigurationContext(localizedConfig)

    val distance = Distance.ofGigameters(1)
    val length = distance.toLength(Gigameter)

    val formattedLength = length.format(localizedContext)

    formattedLength shouldBe "1000000.00km"
  }

  @Test fun `formatting uses meters for small values with SI units`() {
    // Only language of locale can be set with @Config and we need to set country.
    val config = context.resources.configuration
    val localizedConfig = Configuration(config).apply { setLocale(Locale.UK) }
    val localizedContext = context.createConfigurationContext(localizedConfig)

    val distance = Distance.ofMillimeters(987)
    val length = distance.toLength(Millimeter)

    val formattedLength = length.format(localizedContext)

    formattedLength shouldBe "0.99m"
  }

  @Test fun `formatting uses provided converter`() {
    val distance = Distance.ofMillimeters(987)
    val length = distance.toLength(Millimeter)

    val formattedLength = length.format(
        context,
        converter = object : LengthConverter {
          override fun Length<*>.convert(context: Context) = length.withUnit(Nanometer)
        }
    )

    formattedLength shouldBe "987000000.00nm"
  }

  @Test fun `auto formatting uses system locale by default`() {
    // Only language of locale can be set with @Config and we need to set country.
    val config = context.resources.configuration
    val localizedConfig = Configuration(config).apply { setLocale(Locale.US) }
    val localizedContext = context.createConfigurationContext(localizedConfig)

    val distance = Distance.ofFeet(4)
    val length = distance.toLength(Meter)

    val formattedLength = length.format(localizedContext)

    formattedLength shouldBe "1yd 1ft"
  }
}

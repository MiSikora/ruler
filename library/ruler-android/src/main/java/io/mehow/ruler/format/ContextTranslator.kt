package io.mehow.ruler.format

import android.content.Context
import android.os.Build.VERSION
import io.mehow.ruler.ImperialLengthUnit
import io.mehow.ruler.ImperialLengthUnit.Foot
import io.mehow.ruler.ImperialLengthUnit.Inch
import io.mehow.ruler.ImperialLengthUnit.Mile
import io.mehow.ruler.ImperialLengthUnit.Yard
import io.mehow.ruler.LengthUnit
import io.mehow.ruler.R
import io.mehow.ruler.SiLengthUnit
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
import java.util.Locale

/**
 * Translator that uses a supplied [Context] to obtain localization information.
 */
public class ContextTranslator(
  private val context: Context,
) : Translator {
  override val locale: Locale
    get() = when {
      VERSION.SDK_INT >= 24 -> context.resources.configuration.locales[0]
      else -> @Suppress("DEPRECATION") context.resources.configuration.locale
    }

  override fun symbol(unit: LengthUnit<*>): String = when (unit) {
    is SiLengthUnit -> unit.resource
    is ImperialLengthUnit -> unit.resource
  }.let(context::getString)

  private val SiLengthUnit.resource
    get() = when (this) {
      Nanometer -> R.string.io_mehow_ruler_nanometers
      Micrometer -> R.string.io_mehow_ruler_micrometers
      Millimeter -> R.string.io_mehow_ruler_millimeters
      Centimeter -> R.string.io_mehow_ruler_centimeters
      Decimeter -> R.string.io_mehow_ruler_decimeters
      Meter -> R.string.io_mehow_ruler_meters
      Decameter -> R.string.io_mehow_ruler_decameters
      Hectometer -> R.string.io_mehow_ruler_hectometers
      Kilometer -> R.string.io_mehow_ruler_kilometers
      Megameter -> R.string.io_mehow_ruler_megameters
      Gigameter -> R.string.io_mehow_ruler_gigameters
    }

  private val ImperialLengthUnit.resource
    get() = when (this) {
      Inch -> R.string.io_mehow_ruler_inches
      Foot -> R.string.io_mehow_ruler_feet
      Yard -> R.string.io_mehow_ruler_yards
      Mile -> R.string.io_mehow_ruler_miles
    }
}

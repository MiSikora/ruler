package io.mehow.ruler

import android.content.Context
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

internal object ResourcesLengthFormatter : LengthFormatter {
  override fun Length<*>.format(context: Context, separator: String): String? {
    return unit.resource()
        ?.let { resource -> context.getString(resource, measuredLength, separator) }
  }

  private fun LengthUnit.resource(): Int? = when (this) {
    is SiLengthUnit -> this.resource()
    is ImperialLengthUnit -> this.resource()
    else -> null
  }

  private fun SiLengthUnit.resource() = when (this) {
    Nanometer -> R.string.io_mehow_ruler_nanometers
    Micrometer -> R.string.io_mehow_ruler_micrometers
    Millimeter -> R.string.io_mehow_ruler_millimeters
    Meter -> R.string.io_mehow_ruler_meters
    Kilometer -> R.string.io_mehow_ruler_kilometers
    Megameter -> R.string.io_mehow_ruler_megameters
    Gigameter -> R.string.io_mehow_ruler_gigameters
  }

  private fun ImperialLengthUnit.resource() = when (this) {
    Inch -> R.string.io_mehow_ruler_inches
    Foot -> R.string.io_mehow_ruler_feet
    Yard -> R.string.io_mehow_ruler_yards
    Mile -> R.string.io_mehow_ruler_miles
  }
}

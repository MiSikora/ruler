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

/**
 * Formatter that applies a resource based on a unit type in [Length].
 */
public object AutoLengthFormatter : LengthFormatter {
  override fun Length<*>.format(unitSeparator: String, context: Context): String = when (val unit = unit) {
    is SiLengthUnit -> context.getString(unit.resource, measure.toDouble(), unitSeparator)
    is ImperialLengthUnit -> context.getString(unit.resource, measure.toDouble(), unitSeparator)
  }

  private val SiLengthUnit.resource
    get() = when (this) {
      Nanometer -> R.string.io_mehow_ruler_nanometers
      Micrometer -> R.string.io_mehow_ruler_micrometers
      Millimeter -> R.string.io_mehow_ruler_millimeters
      Meter -> R.string.io_mehow_ruler_meters
      Kilometer -> R.string.io_mehow_ruler_kilometers
      Megameter -> R.string.io_mehow_ruler_megameters
      Gigameter -> R.string.io_mehow_ruler_gigameters
    }

  private val ImperialLengthUnit.resource
    get() = when (this) {
      Mile -> R.string.io_mehow_ruler_miles
      Yard -> R.string.io_mehow_ruler_yards
      Foot -> R.string.io_mehow_ruler_feet
      Inch -> R.string.io_mehow_ruler_inches
    }
}

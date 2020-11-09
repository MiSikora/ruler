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

internal object AutoLengthFormatter : LengthFormatter {
  @Volatile internal var useImperialFormatter = true

  override fun Length<*>.format(context: Context, separator: String): String? {
    return when (unit) {
      is SiLengthUnit -> {
        val siUnit = unit as SiLengthUnit
        context.getString(siUnit.resource, measure.toDouble(), separator)
      }
      is ImperialLengthUnit -> if (!useImperialFormatter) {
        val imperialUnit = unit as ImperialLengthUnit
        context.getString(imperialUnit.resource, measure.toDouble(), separator)
      } else ImperialDistanceFormatter.Builder()
          .withMiles(separator)
          .withYards(separator)
          .withFeet(separator)
          .withInches(separator)
          .build()
          .format(distance, context)
      else -> null
    }
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

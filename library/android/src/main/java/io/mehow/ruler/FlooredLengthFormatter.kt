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

public object FlooredLengthFormatter : LengthFormatter {
  override fun Length<*>.format(
    unitSeparator: String,
    context: Context,
  ): String = context.getString(unit.partResource, measure.toLong(), unitSeparator)

  private val LengthUnit<*>.partResource
    get() = when (this) {
      is SiLengthUnit -> partResource
      is ImperialLengthUnit -> partResource
    }

  private val SiLengthUnit.partResource
    get() = when (this) {
      Nanometer -> R.string.io_mehow_ruler_nanometers_part
      Micrometer -> R.string.io_mehow_ruler_micrometers_part
      Millimeter -> R.string.io_mehow_ruler_millimeters_part
      Meter -> R.string.io_mehow_ruler_meters_part
      Kilometer -> R.string.io_mehow_ruler_kilometers_part
      Megameter -> R.string.io_mehow_ruler_megameters_part
      Gigameter -> R.string.io_mehow_ruler_gigameters_part
    }

  private val ImperialLengthUnit.partResource
    get() = when (this) {
      Inch -> R.string.io_mehow_ruler_inches_part
      Foot -> R.string.io_mehow_ruler_feet_part
      Yard -> R.string.io_mehow_ruler_yards_part
      Mile -> R.string.io_mehow_ruler_miles_part
    }
}

public fun Distance.formatFloored(
  context: Context,
  unitSeparator: String = "",
): String = when {
  context.useImperialUnits -> toLength(Yard)
  else -> toLength(Meter)
}.formatFloored(context, unitSeparator)

public fun Length<*>.formatFloored(
  context: Context,
  unitSeparator: String = "",
): String = format(context, unitSeparator, converter = null, FlooredLengthFormatter)

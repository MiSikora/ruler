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

internal object FlooredLengthFormatter : LengthFormatter {
  override fun Length<*>.format(context: Context, separator: String): String? {
    return when (unit) {
      is SiLengthUnit -> {
        val siUnit = unit as SiLengthUnit
        context.getString(siUnit.partResource, siUnit.unitsPart(distance), separator)
      }
      is ImperialLengthUnit -> {
        val imperialUnit = unit as ImperialLengthUnit
        context.getString(imperialUnit.partResource, imperialUnit.unitsPart(distance), separator)
      }
      else -> null
    }
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

  private fun SiLengthUnit.unitsPart(distance: Distance): Long {
    return when (this) {
      Nanometer -> distance.metersPart * 1_000_000_000 + distance.nanosPart
      Micrometer -> distance.metersPart * 1_000_000 + distance.nanosPart / 1_000
      Millimeter -> distance.metersPart * 1_000 + distance.nanosPart / 1_000_000
      Meter -> distance.metersPart
      Kilometer -> distance.metersPart / 1_000
      Megameter -> distance.metersPart / 1_000_000
      Gigameter -> distance.metersPart / 1_000_000_000
    }
  }

  private val ImperialLengthUnit.partResource
    get() = when (this) {
      Inch -> R.string.io_mehow_ruler_inches_part
      Foot -> R.string.io_mehow_ruler_feet_part
      Yard -> R.string.io_mehow_ruler_yards_part
      Mile -> R.string.io_mehow_ruler_miles_part
    }

  private fun ImperialLengthUnit.unitsPart(distance: Distance): Long {
    val inches = (distance.exactTotalMeters * inchesInMeter).toLong()
    return when (this) {
      Inch -> inches
      Foot -> inches / 12
      Yard -> inches / 36
      Mile -> inches / 63_360
    }
  }

  private val inchesInMeter = 39.3700787402.toBigDecimal()
}

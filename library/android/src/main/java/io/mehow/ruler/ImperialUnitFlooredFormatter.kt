package io.mehow.ruler

import android.content.Context
import io.mehow.ruler.ImperialLengthUnit.Foot
import io.mehow.ruler.ImperialLengthUnit.Inch
import io.mehow.ruler.ImperialLengthUnit.Mile
import io.mehow.ruler.ImperialLengthUnit.Yard

internal class ImperialUnitFlooredFormatter(
  private val imperialUnit: ImperialLengthUnit
) : LengthFormatter {
  override fun Length<*>.format(context: Context, separator: String): String? {
    return context.getString(imperialUnit.partResource, imperialUnit.unitsPart(distance), separator)
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

  private companion object {
    private val inchesInMeter = 39.3700787402.toBigDecimal()
  }
}

package io.mehow.ruler

import android.content.Context
import io.mehow.ruler.SiLengthUnit.Kilometer
import io.mehow.ruler.SiLengthUnit.Meter

/**
 * Converter that applies best fitting [unit][LengthUnit] to a [Length].
 */
public object AutoFitLengthConverter : LengthConverter {
  private val siUnits = listOf(Meter, Kilometer)

  override fun Length<*>.convert(context: Context): Length<*> = when (val unit = unit) {
    is SiLengthUnit -> withUnit(unit).withFittingUnit(siUnits)
    is ImperialLengthUnit -> withUnit(unit).withFittingUnit()
  }
}

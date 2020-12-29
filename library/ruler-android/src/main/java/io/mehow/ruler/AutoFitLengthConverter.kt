package io.mehow.ruler

import android.content.Context
import io.mehow.ruler.ImperialLengthUnit.Yard
import io.mehow.ruler.SiLengthUnit.Kilometer
import io.mehow.ruler.SiLengthUnit.Meter

/**
 * Converter that applies best fitting [unit][LengthUnit] to a [Length].
 */
public object AutoFitLengthConverter : LengthConverter {
  private val siUnits = listOf(Meter, Kilometer)

  override fun Length<*>.convert(context: Context): Length<*> = when (unit) {
    is SiLengthUnit -> withUnit(Meter).withFittingUnit(siUnits)
    is ImperialLengthUnit -> withUnit(Yard).withFittingUnit()
  }
}

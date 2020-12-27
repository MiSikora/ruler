package io.mehow.ruler

import android.content.Context
import io.mehow.ruler.ImperialLengthUnit.Yard
import io.mehow.ruler.SiLengthUnit.Kilometer
import io.mehow.ruler.SiLengthUnit.Meter

public object AutoFitLengthConverter : LengthConverter {
  override fun Length<*>.convert(context: Context): Length<*> = when (unit) {
    is SiLengthUnit -> withUnit(Meter).withAutoUnit().coerceUnitIn(Meter, Kilometer)
    is ImperialLengthUnit -> withUnit(Yard).withAutoUnit()
  }
}

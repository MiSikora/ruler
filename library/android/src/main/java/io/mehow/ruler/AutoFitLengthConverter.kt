package io.mehow.ruler

import android.content.Context
import io.mehow.ruler.ImperialLengthUnit.Yard
import io.mehow.ruler.SiLengthUnit.Kilometer
import io.mehow.ruler.SiLengthUnit.Meter

object AutoFitLengthConverter : LengthConverter {
  @Suppress("UNCHECKED_CAST")
  override fun Length<*>.convert(context: Context): Length<*>? {
    val length = if (context.preferredLocale.isImperial) withUnit(Yard) else withUnit(Meter)
    val autoLength = length.withAutoUnit()
    return when (autoLength.unit) {
      is SiLengthUnit -> (autoLength as Length<SiLengthUnit>).coerceUnitIn(Meter, Kilometer)
      else -> autoLength
    }
  }
}

package io.mehow.ruler

import io.mehow.ruler.SiLengthUnit.Kilometer
import io.mehow.ruler.SiLengthUnit.Meter

internal object BuiltInConverter : LengthConverter {
  @Suppress("UNCHECKED_CAST", "UseIfInsteadOfWhen")
  override fun convert(length: Length<*>): Length<*> {
    val autoDistance = length.withAutoUnit()
    return when (autoDistance.unit) {
      is SiLengthUnit -> (autoDistance as Length<SiLengthUnit>).coerceUnitIn(Meter, Kilometer)
      else -> autoDistance
    }
  }
}

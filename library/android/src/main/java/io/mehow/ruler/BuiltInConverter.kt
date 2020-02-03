package io.mehow.ruler

import io.mehow.ruler.SiDistanceUnit.Kilometer
import io.mehow.ruler.SiDistanceUnit.Meter

internal object BuiltInConverter : DistanceConverter {
  @Suppress("UNCHECKED_CAST", "UseIfInsteadOfWhen")
  override fun convert(distance: Distance<*>): Distance<*> {
    val autoDistance = distance.withAutoUnit()
    return when (autoDistance.unit) {
      is SiDistanceUnit -> (autoDistance as Distance<SiDistanceUnit>).coerceUnitIn(Meter, Kilometer)
      else -> autoDistance
    }
  }
}

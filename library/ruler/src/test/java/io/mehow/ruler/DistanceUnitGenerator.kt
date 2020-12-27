package io.mehow.ruler

import io.kotest.property.Exhaustive

internal object DistanceUnitGenerator : Exhaustive<LengthUnit<*>>() {
  override val values: List<LengthUnit<*>>
    get() = SiLengthUnit.values + ImperialLengthUnit.values

  fun createLength(distance: Distance, unit: LengthUnit<*>) = when (unit) {
    is SiLengthUnit -> distance.toLength(unit)
    is ImperialLengthUnit -> distance.toLength(unit)
  }
}

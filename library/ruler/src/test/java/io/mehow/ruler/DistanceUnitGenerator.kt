package io.mehow.ruler

import io.kotest.property.Exhaustive

object DistanceUnitGenerator : Exhaustive<LengthUnit<*>>() {
  override val values: List<LengthUnit<*>>
    get() {
      val siLengthUnits: Iterable<LengthUnit<*>> = SiLengthUnit.values().toList()
      val imperialLengthUnits: Iterable<LengthUnit<*>> = ImperialLengthUnit.values().toList()
      return siLengthUnits + imperialLengthUnits
    }

  fun createLength(distance: Distance, unit: LengthUnit<*>) = when (unit) {
    is SiLengthUnit -> distance.toLength(unit)
    is ImperialLengthUnit -> distance.toLength(unit)
    else -> error("Unknown unit: $unit")
  }
}

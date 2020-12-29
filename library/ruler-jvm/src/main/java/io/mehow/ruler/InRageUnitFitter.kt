package io.mehow.ruler

/**
 * Selects a unit based on a best fit of a [distance][Length.distance] in [unit bounds][LengthUnit.bounds].
 */
public object InRageUnitFitter : UnitFitter {
  override fun <T : LengthUnit<T>> findFit(
    units: Iterable<T>,
    length: Length<T>,
  ): T? = length.distance.abs().let { distance ->
    units.firstOrNull { distance in it }
  }
}

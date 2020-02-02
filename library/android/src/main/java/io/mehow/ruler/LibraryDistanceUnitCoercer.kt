package io.mehow.ruler

import io.mehow.ruler.SiDistanceUnit.Kilometer
import io.mehow.ruler.SiDistanceUnit.Meter

internal class LibraryDistanceUnitCoercer<T>
  : DistanceUnitCoercer<T> where T : DistanceUnit, T : Comparable<T>, T : Iterable<T> {
  @Suppress("UNCHECKED_CAST")
  override fun coerce(distance: Distance<T>) = when (distance.unit) {
    is SiDistanceUnit -> (distance as Distance<SiDistanceUnit>).coerceUnitIn(Meter, Kilometer)
    else -> distance
  }
}

package io.mehow.ruler

internal object BuiltInUnitFitter : UnitFitter {
  // While LogDistanceUnitFitter would suffice and put units in a correct range anyway,
  // InRangeUnitFitter gets there faster.
  private val delegates = sequenceOf(InRageUnitFitter, LogDistanceUnitFitter)

  override fun <T : LengthUnit<T>> findFit(
    units: Iterable<T>,
    length: Length<T>,
  ) = delegates.mapNotNull { it.findFit(units, length) }.firstOrNull()
}

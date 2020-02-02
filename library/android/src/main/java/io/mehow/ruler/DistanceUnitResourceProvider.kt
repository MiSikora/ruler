package io.mehow.ruler

interface DistanceUnitResourceProvider<T : DistanceUnit> {
  fun resource(unit: T): Int
}

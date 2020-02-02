package io.mehow.ruler

interface DistanceUnitCoercer<T> where T : DistanceUnit, T : Comparable<T>, T : Iterable<T> {
  fun coerce(distance: Distance<T>): Distance<*>
}

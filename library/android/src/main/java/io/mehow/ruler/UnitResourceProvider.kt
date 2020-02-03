package io.mehow.ruler

interface UnitResourceProvider<T : LengthUnit> {
  fun resource(unit: T): Int
}

package io.mehow.ruler

class Length<T> internal constructor(
  val distance: Distance,
  val unit: T
) : Comparable<Length<*>> where T : LengthUnit, T : Comparable<T>, T : Iterable<T> {
  val measuredLength = unit.toMeasuredLength(distance.exactTotalMeters)

  fun <R> withUnit(
    unit: R
  ): Length<R> where R : LengthUnit, R : Comparable<R>, R : Iterable<R> {
    return Length(distance, unit)
  }

  fun withAutoUnit(): Length<T> {
    return withUnit(unit.single { it.appliesRangeTo(distance.exactTotalMeters) })
  }

  @JvmSynthetic fun coerceUnitIn(range: ClosedRange<T>): Length<T> {
    require(!range.isEmpty()) { "Range cannot be empty!" }
    return when {
      unit > range.endInclusive -> Length(distance, range.endInclusive)
      unit < range.start -> Length(distance, range.start)
      else -> this
    }
  }

  fun coerceUnitIn(min: T, max: T): Length<T> {
    return coerceUnitIn(min..max)
  }

  fun coerceUnitAtLeastTo(min: T): Length<T> {
    return if (unit < min) Length(distance, min) else this
  }

  fun coerceUnitAtMostTo(max: T): Length<T> {
    return if (unit > max) Length(distance, max) else this
  }

  operator fun plus(other: Length<*>): Length<T> {
    return (distance + other.distance).toLength(unit)
  }

  operator fun plus(distance: Distance): Length<T> {
    return (this.distance + distance).toLength(unit)
  }

  operator fun minus(other: Length<*>): Length<T> {
    return (distance - other.distance).toLength(unit)
  }

  operator fun minus(distance: Distance): Length<T> {
    return (this.distance - distance).toLength(unit)
  }

  override fun compareTo(other: Length<*>): Int {
    return distance.compareTo(other.distance)
  }

  override fun equals(other: Any?): Boolean {
    if (other !is Length<*>) return false
    return distance == other.distance && unit == other.unit
  }

  override fun hashCode(): Int {
    return 31 * distance.hashCode() + unit.hashCode()
  }

  override fun toString(): String {
    return "Length(measuredLength=$measuredLength, unit=$unit)"
  }
}

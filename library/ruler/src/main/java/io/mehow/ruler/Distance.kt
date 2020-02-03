package io.mehow.ruler

class Distance<T> internal constructor(
  val length: Length,
  val unit: T
) : Comparable<Distance<*>> where T : DistanceUnit, T : Comparable<T>, T : Iterable<T> {
  val measuredLength = unit.toMeasuredLength(length.exactTotalMeters)

  fun <R> withUnit(
    unit: R
  ): Distance<R> where R : DistanceUnit, R : Comparable<R>, R : Iterable<R> {
    return Distance(length, unit)
  }

  fun withAutoUnit(): Distance<T> {
    return withUnit(unit.single { it.appliesRangeTo(length.exactTotalMeters) })
  }

  @JvmSynthetic fun coerceUnitIn(range: ClosedRange<T>): Distance<T> {
    require(!range.isEmpty()) { "Range cannot be empty!" }
    return when {
      unit > range.endInclusive -> Distance(length, range.endInclusive)
      unit < range.start -> Distance(length, range.start)
      else -> this
    }
  }

  fun coerceUnitIn(min: T, max: T): Distance<T> {
    return coerceUnitIn(min..max)
  }

  fun coerceUnitAtLeastTo(min: T): Distance<T> {
    return if (unit < min) Distance(length, min) else this
  }

  fun coerceUnitAtMostTo(max: T): Distance<T> {
    return if (unit > max) Distance(length, max) else this
  }

  operator fun plus(other: Distance<*>): Distance<T> {
    return (length + other.length).toDistance(unit)
  }

  operator fun plus(length: Length): Distance<T> {
    return (this.length + length).toDistance(unit)
  }

  operator fun minus(other: Distance<*>): Distance<T> {
    return (length - other.length).toDistance(unit)
  }

  operator fun minus(length: Length): Distance<T> {
    return (this.length - length).toDistance(unit)
  }

  override fun compareTo(other: Distance<*>): Int {
    return length.compareTo(other.length)
  }

  override fun equals(other: Any?): Boolean {
    if (other !is Distance<*>) return false
    return length == other.length && unit == other.unit
  }

  override fun hashCode(): Int {
    return 31 * length.hashCode() + unit.hashCode()
  }

  override fun toString(): String {
    return "Distance(measuredLength=$measuredLength, unit=$unit)"
  }
}

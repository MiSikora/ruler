package io.mehow.ruler

import java.math.BigDecimal

interface LengthUnit<T> : Comparable<T>, Iterable<T> where T : Enum<T>, T : LengthUnit<T> {
  @JvmDefault fun toDistance(value: Long) = toDistance(value.toDouble())

  fun toDistance(value: Double): Distance

  fun toMeasuredLength(meters: BigDecimal): BigDecimal

  fun appliesRangeTo(meters: BigDecimal): Boolean
}

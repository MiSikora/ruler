package io.mehow.ruler

import java.math.BigDecimal

interface LengthUnit<T> : Comparable<T>, Iterable<T> where T : Enum<T>, T : LengthUnit<T> {
  fun toDistance(value: Long): Distance

  fun toDistance(value: Double): Distance

  fun toMeasuredLength(meters: BigDecimal): BigDecimal

  fun appliesRangeTo(meters: BigDecimal): Boolean
}

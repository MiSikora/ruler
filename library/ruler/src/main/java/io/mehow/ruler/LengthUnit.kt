package io.mehow.ruler

import java.math.BigDecimal

interface LengthUnit<T> : Comparable<T>, Iterable<T> where T : LengthUnit<T>, T : Enum<T> {
  val meterRatio: BigDecimal

  operator fun contains(meters: BigDecimal): Boolean
}

package io.mehow.ruler

import java.math.BigDecimal

interface LengthUnit<T> : Comparable<T>, Iterable<T> where T : Enum<T>, T : LengthUnit<T> {
  val meterRatio: BigDecimal

  fun appliesRangeTo(meters: BigDecimal): Boolean
}

package io.mehow.ruler

import java.math.BigDecimal

public interface LengthUnit<T> : Comparable<T> where T : LengthUnit<T>, T : Enum<T> {
  public val meterRatio: BigDecimal

  public operator fun contains(meters: BigDecimal): Boolean
}

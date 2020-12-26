package io.mehow.ruler

import java.math.BigDecimal

public interface LengthUnit<T> : Comparable<T> where T : LengthUnit<T>, T : Enum<T> {
  public val meterRatio: BigDecimal

  public val lowerBound: Distance

  public val upperBound: Distance?
}

internal operator fun <T> LengthUnit<T>.contains(
  distance: Distance,
): Boolean where T : LengthUnit<T>, T : Enum<T> {
  val meters = distance.meters.abs()
  val inLowerBound = meters >= lowerBound.meters
  val inUpperBound = upperBound?.meters?.let { meters < it } != false
  return inLowerBound && inUpperBound
}

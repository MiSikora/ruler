package io.mehow.ruler

import java.math.BigDecimal

/**
 * A unit of measurement that can be applied to length.
 *
 * Set of units within the same measurement system cannot have overlapping bounds.
 */
public interface LengthUnit<T> : Comparable<T> where T : LengthUnit<T>, T : Enum<T> {
  /**
   * Amount of meters in 1 of this unit.
   */
  public val meterRatio: BigDecimal

  /**
   * Inclusive lowest value in which length can be expressed as 1 of this unit. If a unit expressing a length
   * is the lowest possible unit the bound should be [Distance.Zero].
   */
  public val lowerBound: Distance

  /**
   * Exclusive highest value in which length can be expressed before it can be expressed as 1 of next, logical unit.
   * If a unit expressing a length is the highest possible unit the bound should be `null`.
   */
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

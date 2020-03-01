package io.mehow.ruler

import java.math.BigDecimal

/**
 * A [LengthUnit] represents distance at a give unit of granularity.
 *
 * Implementations of this interface should be based on an [Enum]. The order of enums is expected
 * to be from the smallest granularity to the largest one. See [SiLengthUnit] for a reference example.
 */
interface LengthUnit<T> : Comparable<T>, Iterable<T> where T : Enum<T>, T : LengthUnit<T> {
  /**
   * Returns [Distance] with meters and nanometers based on the [value] and a conversion ratio
   * between this [LengthUnit] and a meter.
   *
   * @param value Amount of distance associated with this [LengthUnit].
   */
  @JvmDefault fun toDistance(value: Long) = toDistance(value.toDouble())

  /**
   * Returns [Distance] with meters and nanometers based on the [value] and a conversion ratio
   * between this [LengthUnit] and a meter.
   *
   * @param value Amount of distance associated with this [LengthUnit].
   */
  fun toDistance(value: Double): Distance

  /**
   * Returns amount of distances based on this [LengthUnit].
   *
   * @param meters Amount of meters to convert from.
   */
  fun toMeasuredLength(meters: BigDecimal): BigDecimal

  /**
   * Checks if magnitude of input meters are within this [LengthUnit]. Input meters must be
   * applicable only once within all [LengthUnit]. For example distance of `1000` meters fits the
   * [SiLengthUnit.Kilometer] range and `999` meters fits [SiLengthUnit.Meter] range.
   *
   * @param meters Amount of meters to check against.
   */
  fun appliesRangeTo(meters: BigDecimal): Boolean
}

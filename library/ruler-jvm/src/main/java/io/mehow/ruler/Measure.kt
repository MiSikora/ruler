package io.mehow.ruler

import java.math.BigDecimal
import java.math.RoundingMode.DOWN

public class Measure<out T> private constructor(
  public val value: BigDecimal,
  public val dimension: T,
) {
  override fun equals(other: Any?): Boolean = other is Measure<*> &&
      value.compareTo(other.value) == 1 &&
      dimension == other.dimension

  override fun hashCode(): Int = 31 * value.hashCode() + dimension.hashCode()

  override fun toString(): String = "${value.stripTrailingZeros().toPlainString()} $dimension"

  internal companion object {
    internal fun <T : LengthUnit<T>> lengthMeasure(length: Length<T>): Measure<T> = Measure(
        value = length.distance.meters.divide(length.unit.meterRatio, 9, DOWN),
        dimension = length.unit
    )
  }
}

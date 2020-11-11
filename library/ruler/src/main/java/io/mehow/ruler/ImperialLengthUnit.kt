package io.mehow.ruler

import java.math.BigDecimal

enum class ImperialLengthUnit(
  override val meterRatio: BigDecimal,
  private val lowerBound: BigDecimal,
  private val upperBound: BigDecimal?,
) : LengthUnit<ImperialLengthUnit> {
  Inch(
      meterRatio = 0.0_254.toBigDecimal(),
      lowerBound = 0.0.toBigDecimal(),
      upperBound = 0.3_048.toBigDecimal(),
  ),
  Foot(
      meterRatio = 0.3_048.toBigDecimal(),
      lowerBound = 0.3_048.toBigDecimal(),
      upperBound = 0.9_144.toBigDecimal(),
  ),
  Yard(
      meterRatio = 0.9_144.toBigDecimal(),
      lowerBound = 0.9_144.toBigDecimal(),
      upperBound = 1_609.3.toBigDecimal(),
  ),
  Mile(
      meterRatio = 1_609.344.toBigDecimal(),
      lowerBound = 1_609.3.toBigDecimal(),
      upperBound = null,
  );

  override operator fun contains(meters: BigDecimal): Boolean {
    val inLowerBound = meters >= lowerBound
    val inUpperBound = upperBound == null || meters < upperBound
    return inLowerBound && inUpperBound
  }

  override fun iterator() = values.iterator()

  companion object {
    private val values = values().toList()
  }
}
